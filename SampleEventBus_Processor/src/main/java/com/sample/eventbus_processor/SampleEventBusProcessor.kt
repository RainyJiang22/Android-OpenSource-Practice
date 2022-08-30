package com.sample.eventbus_processor

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.WildcardTypeName
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

/**
 * @author jiangshiyu
 * @date 2022/8/26
 */
class SampleEventBusProcessor : AbstractProcessor() {

    companion object {

        private const val PACKAGE_NAME = "com.sample.eventbus"

        private const val CLASS_NAME = "EventBusInject"

        private const val DOC = "这是自动生成的代码"
    }

    private lateinit var elementUtils: Elements

    private val methodsByClass = LinkedHashMap<TypeElement, MutableList<ExecutableElement>>()

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        elementUtils = processingEnv.elementUtils
    }

    private fun getClassAny(): TypeName {
        return ParameterizedTypeName.get(
            ClassName.get(Class::class.java),
            WildcardTypeName.subtypeOf(Any::class.java)
        )
    }


    /**
     * 通过collect拿到所有监听方法，保存到methodsByClass中，对方法进行签名校验
     * fixme 只能是实例方法，且必须是public的，最多且至少包含一个入参函数
     */
    private fun collectSubscribers(
        annotations: Set<TypeElement>, env: RoundEnvironment, messager: Messager
    ) {
        for (annotation in annotations) {
            val elements = env.getElementsAnnotatedWith(annotation)
            for (element in elements) {
                if (element is ExecutableElement) {
                    if (checkHasNoErrors(element, messager)) {
                        val classElement = element.enclosingElement as TypeElement
                        var list = methodsByClass[classElement]
                        if (list == null) {
                            list = mutableListOf()
                            methodsByClass[classElement] = list
                        }
                        list.add(element)
                    } else {
                        //@Event 只能用于修改方法
                        messager.printMessage(
                            Diagnostic.Kind.ERROR,
                            "@Event is only valid for methods",
                            element
                        )
                    }
                }
            }
        }
    }


    /**
     * 校验方法签名是否合法
     */
    private fun checkHasNoErrors(
        element: ExecutableElement,
        messager: Messager
    ): Boolean {
        //不能是静态方法
        if (element.modifiers.contains(Modifier.STATIC)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Event method must not be static", element)
            return false
        }

        //必须是public方法
        if (!element.modifiers.contains(Modifier.PUBLIC)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Event method must be public", element)
            return false
        }
        //方法最多且包含一个参数
        val parameters = element.parameters
        if (parameters.size != 1) {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Event method must have exactly 1 parameter",
                element
            )
            return false
        }
        return true
    }


    //生成subscriberIndex这个静态常量
    private fun generateSubscriberField(): FieldSpec {
        val subscriberIndex = ParameterizedTypeName.get(
            ClassName.get(Map::class.java),
            getClassAny(),
            ClassName.get(SubscriberInfo::class.java)
        )
        return FieldSpec.builder(subscriberIndex, "subscriberIndex")
            .addModifiers(
                Modifier.PRIVATE,
                Modifier.STATIC,
                Modifier.FINAL
            )
            .initializer(
                "new ${"$"}T<Class<?>, ${"$"}T>()",
                HashMap::class.java,
                SubscriberInfo::class.java
            )
            .build()
    }

    //生成静态方法块
    private fun generateInitializerBlock(builder: TypeSpec.Builder) {
        for (item in methodsByClass) {
            val methods = item.value
            if (methods.isEmpty()) {
                break
            }
            val codeBuilder = CodeBlock.builder()
            codeBuilder.add(
                "${"$"}T<${"$"}T> eventMethodInfoList = new ${"$"}T<${"$"}T>();",
                List::class.java,
                EventMethodInfo::class.java,
                ArrayList::class.java,
                EventMethodInfo::class.java
            )
            methods.forEach {
                val methodName = it.simpleName.toString()
                val eventType = it.parameters[0].asType()
                codeBuilder.add(
                    "eventMethodInfoList.add(new EventMethodInfo(${"$"}S, ${"$"}T.class));",
                    methodName,
                    eventType
                )
            }
            codeBuilder.add(
                "SubscriberInfo subscriberInfo = new SubscriberInfo(${"$"}T.class, eventMethodInfoList); putIndex(subscriberInfo);",
                item.key.asType()
            )
            builder.addInitializerBlock(
                codeBuilder.build()
            )
        }
    }

    //生成 putIndex 方法
    private fun generateMethodPutIndex(): MethodSpec {
        return MethodSpec.methodBuilder("putIndex")
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
            .returns(Void.TYPE)
            .addParameter(SubscriberInfo::class.java, "info")
            .addCode(
                CodeBlock.builder().add("subscriberIndex.put(info.getSubscriberClass() , info);")
                    .build()
            )
            .build()
    }

    /**
     * 生成 `getSubscriberInfo` 这个公开方法，用于运行时调用
     */
    private fun generateMethodGetSubscriberInfo(): MethodSpec {
        return MethodSpec.methodBuilder("getSubscriberInfo")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .returns(SubscriberInfo::class.java)
            .addParameter(getClassAny(), "subscribeClass")
            .addCode(
                CodeBlock.builder().add("subscriberIndex.put(info.getSubscriberClass() , info);")
                    .build()
            )
            .build()
    }

    /**
     * 完成EventInject构建
     */
    override fun process(
        set: MutableSet<out TypeElement>,
        roundEnvironment: RoundEnvironment
    ): Boolean {

        val messager = processingEnv.messager
        collectSubscribers(set, roundEnvironment, messager)
        if (methodsByClass.isEmpty()) {
            messager.printMessage(Diagnostic.Kind.WARNING, "No @Event annotations found")
        } else {
            val typeSpec = TypeSpec.classBuilder(CLASS_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc(DOC)
                .addField(generateSubscriberField())
                .addMethod(generateMethodPutIndex())
                .addMethod(generateMethodGetSubscriberInfo())

            generateInitializerBlock(typeSpec)
            val javaFile = JavaFile.builder(PACKAGE_NAME, typeSpec.build())
                .build()
            try {
                javaFile.writeTo(processingEnv.filer)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return true
    }
}
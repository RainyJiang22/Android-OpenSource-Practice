package com.rainy.rxlivedatahttp

import androidx.lifecycle.LiveData
import com.rainy.rxlivedatahttp.bean.HttpWrapBean
import com.rainy.rxlivedatahttp.exception.ServerCodeNoSuccessException
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Response.error
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author jiangshiyu
 * @date 2022/9/9
 */
class LiveDataCallAdapter<R>(private val responseType: Type) : CallAdapter<R, LiveData<R>> {
    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): LiveData<R> {
        return object : LiveData<R>() {

            private val started = AtomicBoolean(false)

            override fun onActive() {
                //避免重复请求
                if (started.compareAndSet(false,true)) {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            val body = response.body() as HttpWrapBean<*>
                            if (body.isSuccess) {
                                //成功状态，直接返回body
                                postValue(response.body())
                            } else {
                                //失败状态，返回格式化好的 HttpWrapBean 对象
                                postValue(HttpWrapBean.error(ServerCodeNoSuccessException(body)) as R)
                            }
                        }

                        override fun onFailure(call: Call<R>, t: Throwable) {
                        }

                    })
                }
            }

        }
    }
}
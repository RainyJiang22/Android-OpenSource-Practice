package sortKotlin

import kotlinx.coroutines.awaitAll
import kotlin.math.sin

/**
 * @author Jacky
 * @date 2023/1/12
 **/
object Sort {


    //冒泡排序，将较大的元素不断向右进行交换，第一次循环，最大的元素在最右边
    fun bubbleSort(array: IntArray) {
        var isBubble = false
        var i = array.size - 1
        while (i > 0 && !isBubble) {
            isBubble = true
            for (j in 0 until i) {
                if (array[j] > array[j + 1]) {
                    isBubble = false
                    swap(array, j, j + 1)
                }
            }
            i--
        }
    }


    //选择排序，每次循环找到最小的一个元素，和第一个进行替换
    fun selectSort(array: IntArray) {

        for (i in array.indices) {
            var min = i;

            for (j in i until array.size - 1) {
                if (array[j] < array[min]) {
                    min = j
                }
            }
            swap(array, i, min)
        }

    }


    //插入排序，每次先左边插入一个元素，使得左边元素依旧有序
    fun insertSort(array: IntArray) {

        for (i in 1 until array.size) {
            var j = i
            while (j > 0 && array[j] < array[j - 1]) {
                swap(array, j, j - 1)
                j--
            }
        }
    }


    //在插入排序的基础上,可以交换不相邻的逆序对,使得每次减少逆序对的数量大于，避免插入排序的局限性
    fun shellSort(array: IntArray) {
        val k = array.size
        var h = 1

        while (h < k / 3) {
            h = 3 * k + 1
        }

        while (h >= 1) {
            for (i in h until k) {
                var j = i
                while (j >= h && array[j] < array[j - h]) {
                    swap(array, j, j - h)
                    j -= h
                }
            }
            h /= 3
        }
    }


    /**
     * 归并排序
     * 思想就是将数组分成两个部分，然后分别排序，然后再合并起来
     * 1.自顶向下归并（使用递归）
     * 将一个大数组分成两个小数组去求解，每次都对半分成两个子问题，一般时间复杂度为0(NlogN)
     * 2.自顶向上归并（循环）
     * 归并那些微型数组，然后成对归并得到的微型数组
     * 时间复杂度O(nlogn) 空间复杂度O(N)
     */


    //排序
    private fun merge(array: IntArray, start: Int, middle: Int, end: Int) {

        //辅助数组
        val temp = intArrayOf(array.size)

        var i = start
        var j = middle + 1

        for (k in start..end) {
            if (i > middle) {
                temp[k] = array[j++]
            } else if (j > end) {
                temp[k] = array[i++]
            } else if (temp[i] <= temp[j]) {
                temp[k] = array[i++]
            } else {
                temp[k] = array[j++]
            }
        }
    }

    /**
     *
     *
     * 对数组array做若干次合并: 数组array的总长度为len，将它分为若干个长度为gap的子数组；
     * 将"每2个相邻的子数组" 进行合并排序。 先分成8个数组
     *
     * 参数说明:
     *     array -- 待排序的数组
     *     len -- 数组的长度
     *     gap -- 子数组的长度
     */
    private fun mergeGroups(array: IntArray, len: Int, gap: Int) {
        val twoLen = 2 * gap

        // 将"每2个相邻的子数组" 进行合并排序。

        var index = 0
        while (index + twoLen - 1 < len) {
            merge(array, index, index + gap - 1, index + twoLen - 1)
            index += twoLen
        }

        // 若 index +gap-1 < len-1，则剩余一个子数组没有配对。
        // 将该子数组合并到已排序的数组中。
        if (index + gap - 1 < len - 1) {
            merge(array, index, index + gap - 1, len - 1)
        }

    }

    //自顶向下，递归操作
    private fun mergeUpSort(array: IntArray, start: Int, end: Int) {
        if (end <= start) {
            return
        }
        val mid = start + (end - start) / 2

        mergeUpSort(array, start, mid)
        mergeUpSort(array, mid + 1, end)
        merge(array, start, mid, end)

    }

    fun mergeUpSort(array: IntArray) {
        mergeUpSort(array, 0, array.size - 1)
    }


    //自底向上，归并微型数组
    fun mergeDownSort(array: IntArray) {
        var n = 1
        while (n < array.size) {
            mergeGroups(array, array.size, n)
            n *= 2
        }
    }


    fun quickSort(array: IntArray) {
        array.shuffle()
        quickSort(array, 0, array.size - 1)
    }

    /**
     * 快速排序 找到一个切分基准元素，让它左边的数比它小，右边的数比它大
     */
    private fun quickSort(array: IntArray, l: Int, h: Int) {

        if (h <= l) {
            return
        }

        //获取基准元素
        val partition = partition(array, l, h)
        quickSort(array, 0, partition - 1)
        quickSort(array, partition + 1, h)
    }

    fun partition(array: IntArray, l: Int, h: Int): Int {

        var start = l
        var end = h
        val pivot = array[l]

        while (start < end) {

            //从右往左
            while (pivot <= array[end] && start < end) {
                end--
            }
            //从左到右
            while (pivot >= array[start] && start < end) {
                start++
            }

            if (start < end) {
                swap(array, start, end)
            }

        }
        swap(array, l, end)
        return end
    }

    //三路切分,存在大量元素，分成三部分，分别大于，等于，小于基准元素
    fun quickSortThreeWay(array: IntArray) {
        quickSortThreeWay(array, 0, array.size - 1)
    }


    private fun quickSortThreeWay(array: IntArray, low: Int, high: Int) {
        if (high <= low) {
            return
        }

        var start = low
        var middle = low + 1
        var end = high

        val pivot = array[low]


        while (middle <= end) {

            val cmp = array[middle].compareTo(pivot)

            when {
                cmp < 0 -> {
                    swap(array, start, middle)
                    start++
                    middle++
                }

                cmp > 0 -> {
                    swap(array, middle, end)
                    end--
                }

                else -> {
                    middle++
                }
            }
            quickSortThreeWay(array, low, start - 1)
            quickSortThreeWay(array, end + 1, high)
        }

    }


    /**
     * 堆排序
     * 1.将待排序列构成一个大顶堆，序列对应一个完全二叉树
    2.将堆调整为最大堆-即最大值在根节点(第一个节点)(2/N)
    3.将最大值(第一个节点)和最后一个未排序的节点交换位置
    4.循环递归上述 步骤2 和 步骤3 直到未排序的元素只剩一个，排序结束
     */
    fun heapSort(arr: IntArray) {

        val N = arr.size

        for (i in N / 2 - 1 downTo 0) {
            sink(arr, i, N)
        }

        for (j in N - 1 downTo 0) {
            swap(arr, 0, j)
            sink(arr, 0, j)
        }
    }

    //下沉操作
    private fun sink(array: IntArray, k: Int, N: Int) {
        var largest = k
        //左子树
        val l = 2 * k + 1
        //右子树
        val r = 2 * k + 2

        if (l < N && array[l] > array[largest]) {
            largest = l
        }

        if (r < N && array[r] > array[largest]) {
            largest = r
        }
        if (largest != k) {
            swap(array, k, largest)
            sink(array, largest, N)
        }
    }


    /**
     * 桶排序
     *
     * 1.设置一个定量的数组当作空桶
     * 2.遍历输入数据，把数据一个一个放到桶里去
     * 3.对每个不是空的桶进行排序
     * 4.拿出排好序的数据拼接起来
     */
    fun bucketSort(arr: IntArray): IntArray {
        val n = arr.size
        if (n <= 0) return arr

        // 1) Create n empty buckets
        val b = Array<ArrayList<Int>>(n) { ArrayList() }

        // 2) Put array elements in different buckets
        for (i in 0 until n) {
            val idx = arr[i].toInt() * n
            b[idx].add(arr[i])
        }

        // 3) Sort individual buckets
        for (i in 0 until n) {
            b[i].sort()
        }

        // 4) Concatenate all buckets into arr[]
        var index = 0
        for (i in 0 until n) {
            for (j in b[i].indices) {
                arr[index++] = b[i][j]
            }
        }
        return arr
    }


}
package com.rainy.flow.example.arch.array

/**
 * @author jiangshiyu
 * @date 2023/1/31
 */

/**
 *  - 关于数组是连续内存块的数据结构，插入效率比较第
 *  - 当然我们需要事先知道数组的长度
 *  - 删除元素效率也比较慢
 */

class ArrayQuestion {


    //将0移动到末尾
    fun moveZeros(arr: IntArray) {
        var idx = 0

        for (num in arr) {
            if (num != 0) {
                arr[idx++] = num
            }
        }

        while (idx < arr.size) {
            arr[idx++] = 0
        }
    }


    //改变矩阵维度
    fun matrixReplace(nums: Array<IntArray>, r: Int, c: Int): Array<IntArray> {
        val m = nums.size
        val n = nums[0].size

        if (m * n != r * c) {
            return nums
        }

        val reshapedNums: Array<IntArray> = Array(r) {
            IntArray(c)
        }

        var index = 0
        for (i in 0 until r) {
            for (j in 0 until c) {
                reshapedNums[i][j] = nums[index / n][index % n]
                index++
            }
        }
        return reshapedNums
    }


}
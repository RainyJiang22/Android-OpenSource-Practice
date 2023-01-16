package sortKotlin

/**
 * @author Jacky
 * @date 2023/1/12
 **/

fun swap(arr: IntArray, i: Int, j: Int) {
    val temp = arr[i]
    arr[i] = arr[j]
    arr[j] = temp
}
package sortKotlin

/**
 * @author Jacky
 * @date 2023/1/12
 **/


fun main() {


    val list = (0..100).shuffled().take(10).toIntArray()

    // Sort.bubbleSort(list)

    //Sort.selectSort(list)

//    Sort.insertSort(list)

    // Sort.shellSort(list)

//    Sort.mergeUpSort(list)

//    Sort.quickSort(list)

//    Sort.quickSortThreeWay(list)

//    Sort.heapSort(list)

//    Sort.bucketSort(list)

//    Sort.mergeDown(list)

   // Sort.heap(list)

    Sort.bucketSort(list,100)
    list.map { print("$it,") }
}
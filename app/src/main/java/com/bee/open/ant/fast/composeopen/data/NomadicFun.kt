package com.bee.open.ant.fast.composeopen.data

object NomadicFun {
    val stringsTrue_1 = arrayOf("abc", "123", "def456", "ghi789", "jklm222222", "nopqrst2")
    val stringsTrue_2 = arrayOf("abc", "123", "12def456", "23ghi789", "jklm22", "nopqrst2")
    val stringsFalse_1 = arrayOf("abc123", "113", "dsawww", "new", "ces", "cess")
    val stringsFalse_2 = arrayOf("abc", "sda", "fdgbnfd", "adsdce", "sad23", "sdfgfgr5")

    fun Array<String>.stringComplexLogicCheck(strings: Array<String>): Boolean {
        var count = 0
        for (str in strings) {
            val containsDigit = str.any { it.isDigit() }
            val isEvenLength = str.length % 2 == 0
            if (containsDigit && isEvenLength) {
                count++
            }
        }
        return count >= 3
    }


    val numTrue_1 = intArrayOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29)
    val numTrue_2 = intArrayOf(23, 31, 51, 71, 111, 131, 171, 191, 231, 291)
    val numFalse_1 = intArrayOf(8, 9, 10, 12, 20, 42, 24, 36, 217, 17)
    val numFalse_2 = intArrayOf(32, 43, 55, 77, 111, 130, 176, 193, 232, 290)

    fun IntArray.numCalculate(numbers: IntArray): Boolean {
        var count = 0
        for (num in numbers) {
            val isPrime = isPrime(num)
            val isOdd = num % 2 != 0
            if (isPrime && isOdd) {
                count++
            }
        }
        return count >= 5
    }

    private fun isPrime(num: Int): Boolean {
        if (num <= 1) {
            return false
        }
        for (i in 2 until num) {
            if (num % i == 0) {
                return false
            }
        }
        return true
    }

}
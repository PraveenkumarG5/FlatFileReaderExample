package com.tcs.poc.batch



// Extension function for String class to perform string division
operator fun String.div(other: String): String {
    // Helper function to check if a string can be formed by repeating a prefix
    fun canFormByRepeating(s: String, prefix: String): Boolean {
        if (prefix.isEmpty() || s.length % prefix.length != 0) return false
        val repeatCount = s.length / prefix.length
        return prefix.repeat(repeatCount) == s
    }

    // Find the greatest common prefix
    val minLength = minOf(this.length, other.length)
    val sb = StringBuilder()
    for (i in 0 until minLength) {
        if (this[i] == other[i]) {
            sb.append(this[i])
        } else {
            break
        }
    }

    val commonPrefix = sb.toString()

    // Iterate from the length of the common prefix down to 1
    for (i in commonPrefix.length downTo 1) {
        val prefix = commonPrefix.substring(0, i)
        if (canFormByRepeating(this, prefix) && canFormByRepeating(other, prefix)) {
            return prefix
        }
    }

    return ""
}

fun main() {
    println("aeaeae" / "aeae")  // Output: "ae"
    println("aeae" / "aeae")    // Output: "ae"
    println("aeaz" / "ac")      // Output: "a"
    println("aaasr" / "hhhh")   // Output: ""
}
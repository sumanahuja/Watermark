fun sum5(a1: Int, a2: Int, a3: Int=0, a4: Int=0, a5: Int=0): Int {
    return a1+a2+a3+a4+a5
}

fun main(){
    val numbers = readln().split(" ").toMutableList()
    when(numbers.size) {
        2 -> {
            val sum = sum5(numbers[0].toInt(), numbers[1].toInt())
            println(sum)
        }
        3 -> {
            val sum = sum5(numbers[0].toInt(), numbers[1].toInt(), numbers[2].toInt())
            println(sum)
        }
        4 -> {
            val sum = sum5(numbers[0].toInt(), numbers[1].toInt(), numbers[2].toInt(), numbers[3].toInt())
            println(sum)
        }
        5 -> {
            val sum = sum5(numbers[0].toInt(),numbers[1].toInt(), numbers[2].toInt(), numbers[3].toInt(), numbers[4].toInt())
            println(sum)
        }
    }
}
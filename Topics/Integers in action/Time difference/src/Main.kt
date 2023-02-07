fun main() {
//    val h1 = readln().toInt()
//    val m1 = readln().toInt()
//    val s1 = readln().toInt()
//    val h2 = readln().toInt()
//    val m2 = readln().toInt()
//    val s2 = readln().toInt()
//    val diff = (h2*60*60+m2*60+s2)-(h1*60*60+m1*60+s1)
//    println(diff)
//fun main() {
    val index = readLine()!!.toInt()
    val word = readLine()!!
    if(index in 0 until word.length)
        println(word[index])
    else
        throw Exception ("There isn't such an element in the given string, please fix the index!")
}
//}
import java.lang.ArithmeticException

fun main () {
try {
    println( convertStringToDouble("hello"))
} catch (e : NumberFormatException){
    println(e.message)
} catch (e1: ArithmeticException) {
    println(e1.message)
} catch (e: Exception) {
    println(e.message)
} 
// ...
}
fun convertStringToDouble(input: String): Double {
    /**
     * It returns a double value or 0 if an exception occurred
     */
    try{
        val find = input.toDouble()
    } catch(e: Exception){
        return 0.0
    }
    return input.toDouble()
}
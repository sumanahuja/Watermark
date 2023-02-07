import java.awt.Color
import java.awt.image.BufferedImage  

fun drawSquare(): BufferedImage {

    val w = 500
    val h = 500
    val bufferedImage = BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)
    val graphics = bufferedImage.createGraphics()
    
    graphics.color = Color.RED
    graphics.drawRect(100, 100, 300, 300)
        
    return bufferedImage
}

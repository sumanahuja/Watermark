import java.awt.Color
import java.awt.image.BufferedImage  

fun drawCircles(): BufferedImage {
    val w = 200
    val h = 200
    val bufferedImage = BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    val graphics = bufferedImage.createGraphics()
    
    graphics.color = Color.RED
    graphics.drawOval(50, 50, 100, 100)

    graphics.color = Color.YELLOW
    graphics.drawOval(50, 75, 100, 100)

    graphics.color = Color.GREEN
    graphics.drawOval(75, 50, 100, 100)

    graphics.color = Color.BLUE
    graphics.drawOval(75, 75, 100, 100)

    return bufferedImage

}

package watermark

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.system.exitProcess

//import javax.print.DocFlavor.STRING

fun main() {
    println("Input the image filename:")
    val imageName = readln()

    if (File(imageName).exists()) {
        findImageProperties(imageName)
    } else
        println("The file $imageName doesn't exist.")
}

fun findImageProperties( imageName:String) {
    val bufferedImage = ImageIO.read(File(imageName))
    // println("${bufferedImage.width } * ${bufferedImage.height}")

    if (bufferedImage.colorModel.numColorComponents != 3) {
        println("The number of image color components isn't 3.")

    } else if (bufferedImage.colorModel.pixelSize == 32 || bufferedImage.colorModel.pixelSize == 24) {
        var tRed :Int = -1
        var tGreen:Int = -1
        var tBlue:Int = -1
        println("Input the watermark image filename:")
        val watermarkImageName = readln()
        if (File(watermarkImageName).exists()) {

            val bufferedWatermarkImage = ImageIO.read(File(watermarkImageName))
           // println("${bufferedWatermarkImage.width } * ${bufferedWatermarkImage.height}")

            if(bufferedImage.width < bufferedWatermarkImage.width || bufferedImage.height < bufferedWatermarkImage.height)
            {
                println("The watermark's dimensions are larger.")
                exitProcess(0)

            }

            // println(bufferedWatermarkImage.colorModel.numColorComponents)
            if (bufferedWatermarkImage.colorModel.numColorComponents != 3) {
                println("The number of watermark color components isn't 3.")
            } else if (bufferedWatermarkImage.colorModel.pixelSize == 32 || bufferedWatermarkImage.colorModel.pixelSize == 24) {

                var alphaChoice = checkwaterImageAlpha(bufferedWatermarkImage)
                // if water Image has alpha and want to use it "yes"

                var transList = checkTransparencyChoise(bufferedWatermarkImage)
                //if water image has no alpha but has choice to use transpency color "yes/no"

                if (transList.size == 3) {
                    //var transparencyChoice1 = "yes"
                    tRed = transList[0].toInt()
                    tGreen = transList[1].toInt()
                    tBlue = transList[2].toInt()
                    //alphaChoice = "yes"
                    //println(alphaChoice)
                    if (tRed > 255 || tBlue > 255 || tGreen > 255 ||tRed < 0 || tBlue < 0 || tGreen < 0) {
                        println("The transparency color input is invalid.")
                        exitProcess(0)
                    }


                }


                println("Input the watermark transparency percentage (Integer 0-100):")
                val transPrecentage: String = readln()
                if (transPrecentage.toIntOrNull() == null) {
                    println("The transparency percentage isn't an integer number.")
                } else {
                    val trasPercentDigit = transPrecentage.toInt()
                    if (trasPercentDigit in 0..100) {

                        val positionAndValues = compareImages(bufferedImage,bufferedWatermarkImage)
                        val position:String
                        val ivalue:String
                        val jvalue:String

                        val p = positionAndValues.split(" ").toMutableList()
                        if(p.size == 1){
                            position="grid"
                            ivalue = "0"
                            jvalue = "0"
                        } else {
                            position = p[2]
                            ivalue = p[0]
                            jvalue = p[1]
                        }

                        println("Input the output image filename (jpg or png extension):")
                        val outputImageName = readln()

                        val extensionName = outputImageName.split(".").get(1)

                        if (extensionName == "jpg" || extensionName == "png" || extensionName == "jpeg") {



                            createOutputFile(bufferedImage, bufferedWatermarkImage, trasPercentDigit, outputImageName, alphaChoice, tRed, tGreen, tBlue,transList.size, position, ivalue.toInt(),jvalue.toInt() ,imageName)


                        } else
                            println("The output file extension isn't \"jpg\" or \"png\".")


                    } else
                        println("The transparency percentage is out of range.")
                }
                // }

            } else  {
                println("The watermark isn't 24 or 32-bit.")
            }
        } else {
            println("The file $watermarkImageName doesn't exist.")
        }

    } else
        println("The image isn't 24 or 32-bit.")
}
fun createGridOutFile(
    bufferedImageName: BufferedImage,
    bufferedWatermarkImageName: BufferedImage,
    weight: Int,
    outputImageName1: String,
    alphaChoice: String,
    tR: Int,
    tG: Int,
    tB: Int,
    transListSize: Int,
    position: String,
    ii: Int,
    jj: Int,
    imageName: String,
    output: BufferedImage
) {
    //val bufferedImageCopy= bufferedImageName
    val w1 = bufferedWatermarkImageName.width
    val h1 = bufferedWatermarkImageName.height
    try {
        // if(position == "single") {
        for (i in ii until w1 + ii) {
            for (j in jj until h1 + jj) {
                if (alphaChoice == "yes") {
                   //println("alpha YES")
                    val im = Color(bufferedImageName.getRGB(i, j))
                    var w = Color(bufferedWatermarkImageName.getRGB(i - ii, j - jj), true)
                    if (w.alpha == 0) {
                        output.setRGB(i, j, im.rgb)
                    }
                    if (w.alpha == 255) {
                        val color = Color(
                            (weight * w.red + (100 - weight) * im.red) / 100,
                            (weight * w.green + (100 - weight) * im.green) / 100,
                            (weight * w.blue + (100 - weight) * im.blue) / 100

                        )
                        output.setRGB(i, j, color.rgb)
                    }
                } else {
                   // println("alpha no")
                    val im = Color(bufferedImageName.getRGB(i, j))
                    val w = Color(bufferedWatermarkImageName.getRGB(i - ii, j - jj))

                    if (transListSize == 3) {
                      //  println("want trans")
                        if ((w.red == tR && w.green == tG && w.blue == tB))
                            output.setRGB(i, j, im.rgb)
                        else {
                            val color = Color(
                                (weight * w.red + (100 - weight) * im.red) / 100,
                                (weight * w.green + (100 - weight) * im.green) / 100,
                                (weight * w.blue + (100 - weight) * im.blue) / 100

                            )
                            output.setRGB(i, j, color.rgb)
                        }
                    } else {
                      //  println("dont want trans")
                        val color = Color(
                            (weight * w.red + (100 - weight) * im.red) / 100,
                            (weight * w.green + (100 - weight) * im.green) / 100,
                            (weight * w.blue + (100 - weight) * im.blue) / 100
                        )
                        output.setRGB(i, j, color.rgb)
                    }
                }
            }
            // }
        }
    } catch (e: Exception) { }
}

fun createOutputFile (bufferedImageName: BufferedImage, bufferedWatermarkImageName: BufferedImage , weight :Int, outputImageName1 : String, alphaChoice:String, tR:Int, tG:Int, tB: Int, transListSize:Int, position: String, ii: Int, jj:Int,imageName:String) {
    //var bufferedImageName = bufferedImageName
    val output = BufferedImage(bufferedImageName.width, bufferedImageName.height, BufferedImage.TYPE_INT_RGB)

    val w1 =  bufferedWatermarkImageName.width
    val h1 =  bufferedWatermarkImageName.height
    val outputFile = File(outputImageName1)  // Output the file
    if(position == "single") {
        for (i in 0 until output.width ) {
            for ( j in 0 until output.height) {
                val im = Color(bufferedImageName.getRGB(i, j))
                output.setRGB(i, j, im.rgb)
            }
        }
                for (i in ii until w1+ii ) {
                    for ( j in jj until h1+jj) {

                if (alphaChoice == "yes") {
                    val im = Color(output.getRGB(i, j))
                    var w = Color(bufferedWatermarkImageName.getRGB(i - ii, j - jj), true)

                    if (w.alpha == 0) {
                        output.setRGB(i, j, im.rgb)
                    }
                    if (w.alpha == 255) {
                        val color = Color(
                            (weight * w.red + (100 - weight) * im.red) / 100,
                            (weight * w.green + (100 - weight) * im.green) / 100,
                            (weight * w.blue + (100 - weight) * im.blue) / 100

                        )
                        output.setRGB(i, j, color.rgb)
                    }
                } else {
                    // println("$i $j")
                    val im = Color(output.getRGB(i, j))
                    val w = Color(bufferedWatermarkImageName.getRGB(i - ii, j - jj))

                    if (transListSize == 3) {
                        if ((w.red == tR && w.green == tG && w.blue == tB))
                            output.setRGB(i, j, im.rgb)
                        else {
                            val color = Color(
                                (weight * w.red + (100 - weight) * im.red) / 100,
                                (weight * w.green + (100 - weight) * im.green) / 100,
                                (weight * w.blue + (100 - weight) * im.blue) / 100

                            )
                            output.setRGB(i, j, color.rgb)
                        }
                    } else {
                        val color = Color(
                            (weight * w.red + (100 - weight) * im.red) / 100,
                            (weight * w.green + (100 - weight) * im.green) / 100,
                            (weight * w.blue + (100 - weight) * im.blue) / 100
                        )
                        output.setRGB(i, j, color.rgb)
                    }
                }
           // } else  output.setRGB(i, j, im.rgb)
            }
        }
    }
    else if (position == "grid") {
        //var BI:BufferedImage = bufferedImageName
        for (i in 0 until bufferedImageName.width step w1) {
            for (j in 0 until bufferedImageName.height step h1) {
                createGridOutFile(
                    bufferedImageName,
                    bufferedWatermarkImageName,
                    weight,
                    imageName,
                    alphaChoice,
                    tR,
                    tG,
                    tB,
                    transListSize,
                    position,
                    i,
                    j,
                    imageName,
                    output
                )
               // bufferedImageName=BI
               }

        }

    }
    ImageIO.write(output, outputImageName1.split(".").get(1), outputFile)  // Create an image using the BufferedImage instance data
    println("The watermarked image $outputImageName1 has been created.")
}
fun checkTransparencyChoise(bImage:BufferedImage) :MutableList<String> {
    var temp = mutableListOf<String>()
    if (bImage.transparency != 3) {
        println("Do you want to set a transparency color?")
        val transparencyChoice = readln()
        if(transparencyChoice == "yes")
        {
            println("Input a transparency color ([Red] [Green] [Blue]):")
            temp = readln().split(" ").toMutableList()
            if(temp.size <3 || temp.size >3 ) {
                println("The transparency color input is invalid.")
                exitProcess(0)
            }
        }
        return temp
    }
    return temp
}
fun checkwaterImageAlpha(bImage:BufferedImage): String {
    if (bImage.transparency == 3) {
        println("Do you want to use the watermark's Alpha channel?")
        val alphaChoice = readln()
        return alphaChoice
    }
    return "no"
}

fun compareImages(bufferedImage: BufferedImage, bufferedWatermarkImage: BufferedImage) :String {
    val iWidth = bufferedImage.width
    val wmiWidth = bufferedWatermarkImage.width
    val iHeight = bufferedImage.height
    val wmiHeight = bufferedWatermarkImage.height
    var input:MutableList<String>
    val result: String
    var string: String = ""
    //println("COMPARE")
    if(iWidth < wmiWidth || iHeight < wmiHeight)
    {
        println("The watermark's dimensions are larger.")
        exitProcess(0)

    } else { //askPosition Method

        val DiffX = bufferedImage.width - bufferedWatermarkImage.width
        val DiffY = bufferedImage.height - bufferedWatermarkImage.height

        println("Choose the position method (single, grid):")
        result = readln()
        if(result == "single" || result == "grid") {
            if(result == "single") {
                println("Input the watermark position ([x 0-$DiffX] [y 0-$DiffY]):)")
                input = readln().split(" ").toMutableList()
                try {
                    input[0].toInt()
                    input[1].toInt()

                } catch (e:Exception) {
                    println("The position input is invalid.")
                    exitProcess(0)
                }
                if (input.size > 2 || input.size < 2 || input[0].toInt() < 0 || input[0].toInt() >DiffX || input[1].toInt() < 0 || input[1].toInt() >DiffY) {
                    println("The position input is out of range.")
                    exitProcess(0)
                }
                else {

                    string = "${input[0]} ${input[1]} $result"
                    return string
                    // createSingleWatermarkedLogo(bufferedImage, bufferedWatermarkImage, DiffX, DiffY)
                }
            }
            else if(result == "grid") {
                string = "grid"
                return string
                //input.add( "grid")
            }
        } else {
            println("The position method input is invalid.")
            exitProcess(0)
        }


    }
    // string = "${input[0]} ${input[1]} $result"
    return string
}


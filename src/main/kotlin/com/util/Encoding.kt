package com.util

import com.models.ReleaseInfoBuilder
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import ws.schild.jave.Encoder
import ws.schild.jave.MultimediaObject
import ws.schild.jave.encode.AudioAttributes
import ws.schild.jave.encode.EncodingAttributes
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.plugins.jpeg.JPEGImageWriteParam
import javax.imageio.stream.FileImageOutputStream

const val coverDimension = 128

// kotlin can't save me now
fun saveJpeg(image: BufferedImage, path: String) {
    val flatImage = BufferedImage(coverDimension, coverDimension, BufferedImage.TYPE_INT_RGB)

    val graphics2D = flatImage.createGraphics()
    graphics2D.drawCheckerboard(coverDimension)
    graphics2D.drawImage(image, 0, 0, coverDimension, coverDimension, null)

    val jpegParam = JPEGImageWriteParam(null)
    jpegParam.compressionMode = ImageWriteParam.MODE_EXPLICIT
    jpegParam.compressionQuality = 0.4f

    val outStream = FileImageOutputStream(File(path))
    try {
        val jpegWriter = ImageIO.getImageWritersByFormatName("jpeg").next()
        jpegWriter.output = outStream
        jpegWriter.write(null, IIOImage(flatImage, null, null), jpegParam)
    } catch (ignored: Exception) {
    } finally {
        outStream.close()
    }
}

fun Graphics2D.drawCheckerboard(dimension: Int) {
    color = Color.WHITE
    fillRect(0, 0, dimension, dimension)

    color = Color(200, 200, 200)
    val checkerSize = (dimension + 15) / 16
    for (x in 0..<dimension step checkerSize) {
        for (y in 0..<dimension step checkerSize) {
            if (x / checkerSize % 2 == y / checkerSize % 2) fillRect(x, y, checkerSize, checkerSize)
        }
    }
}

suspend fun saveMp3(channel: ByteReadChannel, path: String, trackNr: Int, infoBuilder: ReleaseInfoBuilder) {
    val tmpFile = File("$path/$trackNr.tmp")
    val mp3File = File("$path/$trackNr.mp3")

    try {
        channel.copyAndClose(tmpFile.writeChannel())

        val multimediaObject = MultimediaObject(tmpFile)
        infoBuilder.trackLengths[trackNr] = (multimediaObject.info.duration / 1000).toInt()

        val attributes = EncodingAttributes().setAudioAttributes(AudioAttributes().setBitRate(30_000).setChannels(1))
        val encoder = Encoder()
        encoder.encode(multimediaObject, mp3File, attributes)

    } catch (ignored: Exception) {
    }

    tmpFile.delete()
}
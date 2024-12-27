package com.util

import com.models.ReleaseInfoBuilder
import io.ktor.util.cio.*
import io.ktor.util.logging.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ws.schild.jave.Encoder
import ws.schild.jave.MultimediaObject
import ws.schild.jave.encode.AudioAttributes
import ws.schild.jave.encode.EncodingAttributes
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.plugins.jpeg.JPEGImageWriteParam
import javax.imageio.stream.FileImageOutputStream

val log = KtorSimpleLogger("Encoding")

const val coverDimension = 128

// kotlin can't save you now
suspend fun saveJpeg(providerChannel: ByteReadChannel, path: String) = coroutineScope {
    launch(Dispatchers.IO) {
        val bytes = providerChannel.toByteArray()
        val image = ImageIO.read(ByteArrayInputStream(bytes)) ?: null

        val flatImage = BufferedImage(coverDimension, coverDimension, BufferedImage.TYPE_INT_RGB)

        val graphics2D = flatImage.createGraphics()
        graphics2D.drawCheckerboard(coverDimension)
        if (image != null) {
            graphics2D.drawImage(image, 0, 0, coverDimension, coverDimension, null)
        }

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

suspend fun saveMp3(
    providerChannel: ByteReadChannel, path: String, trackNr: Int, infoBuilder: ReleaseInfoBuilder
) = coroutineScope {
    launch(Dispatchers.IO) {
        val tempFile = File("$path/$trackNr.tmp")
        val mp3File = File("$path/$trackNr.mp3")

        try {
            // ¯\_(ツ)_/¯
            providerChannel.copyAndClose(tempFile.writeChannel())
            for (i in 0..20) {
                if (tempFile.exists()) break
                delay(5)
            }

            val multimediaObject = MultimediaObject(tempFile)
            val trackLengthSeconds = (multimediaObject.info.duration / 1000).toInt()
            infoBuilder.trackLengths[trackNr] = trackLengthSeconds

            val attributes = EncodingAttributes().setAudioAttributes(
                AudioAttributes().setSamplingRate(24_000).setBitRate(8_000).setChannels(1)
            )
            val encoder = Encoder()
            encoder.encode(multimediaObject, mp3File, attributes)
            log.debug("Track $trackNr of length $trackLengthSeconds encoded")
        } catch (exception: Exception) {
            log.debug("Track $trackNr exception: ${exception.message}")
        } finally {
            tempFile.delete()
        }
    }
}
package ua.antonsher.mypictool.filewriter

import spock.lang.Specification
import ua.antonsher.mypictool.ImageFileWriter

import java.awt.image.BufferedImage

class JavaxImageFileWriterTest extends Specification {
    def "smoke test saveAsJpeg"() {
        given:
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB)
        ImageFileWriter writer = new JavaxImageFileWriter()
        File tempFile = File.createTempFile("test-", ".jpg")
        when:
        writer.saveAsJpeg(image, tempFile, 300)
        then:
        tempFile != null
        tempFile.length() > 0
        cleanup:
        tempFile?.delete()
    }
}

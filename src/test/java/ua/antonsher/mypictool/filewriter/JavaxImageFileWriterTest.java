package ua.antonsher.mypictool.filewriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.antonsher.mypictool.ImageFileWriter;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.junit.Assert.*;

public class JavaxImageFileWriterTest {
    private BufferedImage image_10x10_black;
    private ImageFileWriter writer;
    private File tempFile;

    @Before
    public void setup() throws Exception {
        image_10x10_black = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        writer = new JavaxImageFileWriter();
        tempFile = File.createTempFile("test-", ".jpg");
    }

    @After
    public void cleanup() {
        if (tempFile != null) {
            tempFile.delete();
        }
    }

    @Test
    public void smokeTestSaveJpeg() {
        writer.saveAsJpeg(image_10x10_black, tempFile, 300);
        assertTrue("File not empty", tempFile.length() > 0);
    }
}

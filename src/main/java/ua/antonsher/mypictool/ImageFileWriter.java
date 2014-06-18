package ua.antonsher.mypictool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface ImageFileWriter {
    void saveAsJpeg(BufferedImage image, File targetFile, int dpi) throws IOException;
}

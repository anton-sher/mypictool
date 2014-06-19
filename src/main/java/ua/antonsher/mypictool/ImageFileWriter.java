package ua.antonsher.mypictool;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A facade for saving images. Encapsulates the boilerplate.
 */
public interface ImageFileWriter {
    void saveAsJpeg(@Nonnull BufferedImage image, @Nonnull File targetFile, int dpi);
}

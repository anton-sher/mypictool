package ua.antonsher.mypictool;

import ua.antonsher.mypictool.exceptions.SaveImageException;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A facade for saving images. Encapsulates the boilerplate.
 */
public interface ImageFileWriter {
    /**
     * @param image image to save in a file.
     * @param targetFile file in which the image is saved.
     * @param dpi DPI information to add to file metadata. Size of the saved image are not affected by this parameter.
     */
    void saveAsJpeg(@Nonnull BufferedImage image, @Nonnull File targetFile, int dpi) throws SaveImageException;
}

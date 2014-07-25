package ua.antonsher.mypictool.filewriter;

import ua.antonsher.mypictool.exceptions.SaveImageException;

import javax.annotation.Nonnull;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * A facade for saving images. Encapsulates the boilerplate.
 */
public interface ImageFileWriter {
    /**
     * @param image image to save in a file.
     * @param targetFile file in which the image is saved.
     * @param dpi DPI information to add to file metadata. Size of the saved image are not affected by this parameter.
     */
    void saveAsJpeg(@Nonnull final BufferedImage image, @Nonnull final File targetFile, final int dpi) throws SaveImageException;
}

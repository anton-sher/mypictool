package ua.antonsher.mypictool.filewriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.antonsher.mypictool.Conversions;
import ua.antonsher.mypictool.ImageFileWriter;
import ua.antonsher.mypictool.exceptions.SaveImageException;

import javax.annotation.Nonnull;
import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Image writer based on the javax.imageio classes.
 */
public class JavaxImageFileWriter implements ImageFileWriter {
    private static final Logger logger = LoggerFactory.getLogger(JavaxImageFileWriter.class);

    public static final String FORMAT_NAME = "JPEG";

    /**
     * @param image image to save in a file.
     * @param targetFile file in which the image is saved.
     * @param dpi DPI information to add to file metadata. Size of the saved image are not affected by this parameter.
     * @throws SaveImageException if something goes wrong
     */
    @Override
    public void saveAsJpeg(@Nonnull BufferedImage image, @Nonnull File targetFile, int dpi) {
        logger.info("Saving image as JPEG to {} with DPI {}", targetFile, dpi);

        ImageWriter writer = null;
        IIOMetadata metadata = null;
        ImageWriteParam writeParam = null;
        logger.debug("Looking up image writer");
        ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName(FORMAT_NAME);
        while (it.hasNext()) {
            writer = it.next();
            writeParam = writer.getDefaultWriteParam();
            metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
            if (metadata != null && !metadata.isReadOnly() && metadata.isStandardMetadataFormatSupported()) {
                break;
            }
        }

        if (writer != null && metadata != null && writeParam != null) {
            logger.debug("Image writer found: {}", writer);

            double pixelSize = Conversions.getPixelSizeMm(dpi);
            // Javax ImageIO JPEG writer requires decimeters, not millimeters.
            pixelSize /= 100;

            IIOMetadataNode horizontal = new IIOMetadataNode("HorizontalPixelSize");
            horizontal.setAttribute("value", Double.toString(pixelSize));

            IIOMetadataNode vertical = new IIOMetadataNode("VerticalPixelSize");
            vertical.setAttribute("value", Double.toString(pixelSize));

            IIOMetadataNode dim = new IIOMetadataNode("Dimension");
            dim.appendChild(horizontal);
            dim.appendChild(vertical);

            IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
            root.appendChild(dim);

            try {
                metadata.mergeTree("javax_imageio_1.0", root);
                try (ImageOutputStream stream = ImageIO.createImageOutputStream(targetFile)) {
                    writer.setOutput(stream);
                    writer.write(metadata, new IIOImage(image, null, metadata), writeParam);
                }
            } catch (IOException e) {
                throw new SaveImageException("Error saving JPEG file", e);
            }
        } else {
            throw new SaveImageException("Error saving JPEG file: no writer found");
        }
    }
}

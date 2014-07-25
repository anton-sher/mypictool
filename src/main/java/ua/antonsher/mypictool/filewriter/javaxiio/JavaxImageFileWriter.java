package ua.antonsher.mypictool.filewriter.javaxiio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.antonsher.mypictool.exceptions.SaveImageException;
import ua.antonsher.mypictool.filewriter.ImageFileWriter;
import ua.antonsher.mypictool.util.Conversions;

import javax.annotation.Nonnull;
import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import javax.inject.Inject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Image writer based on the javax.imageio classes.
 */
public class JavaxImageFileWriter implements ImageFileWriter {
    private static final Logger logger = LoggerFactory.getLogger(JavaxImageFileWriter.class);

    private static final String FORMAT_NAME = "JPEG";
    private static final int MM_IN_DM = 100;
    private static final String ROOT_NODE = "javax_imageio_1.0";
    private static final String HORIZONTAL_PIXEL_SIZE_NODE = "HorizontalPixelSize";
    private static final String VERTICAL_PIXEL_SIZE_NODE = "VerticalPixelSize";
    private static final String DIMENSION_NODE = "Dimension";
    private static final String VALUE_TAG = "value";

    @Inject
    public JavaxImageFileWriter() {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAsJpeg(@Nonnull final BufferedImage image, @Nonnull final File targetFile, final int dpi) {
        logger.info("Saving image as JPEG to {} with DPI {}", targetFile, dpi);

        final WriterWithParam writerWithParam = findJpegWriter();

        // Javax ImageIO JPEG writer requires decimeters, not millimeters.
        final double pixelSize = Conversions.getPixelSizeMm(dpi) / MM_IN_DM;

        final IIOMetadataNode horizontal = new IIOMetadataNode(HORIZONTAL_PIXEL_SIZE_NODE);
        horizontal.setAttribute(VALUE_TAG, Double.toString(pixelSize));

        final IIOMetadataNode vertical = new IIOMetadataNode(VERTICAL_PIXEL_SIZE_NODE);
        vertical.setAttribute(VALUE_TAG, Double.toString(pixelSize));

        final IIOMetadataNode dim = new IIOMetadataNode(DIMENSION_NODE);
        dim.appendChild(horizontal);
        dim.appendChild(vertical);

        final IIOMetadataNode root = new IIOMetadataNode(ROOT_NODE);
        root.appendChild(dim);

        try {
            writerWithParam.metadata.mergeTree(ROOT_NODE, root);
            try (ImageOutputStream stream = ImageIO.createImageOutputStream(targetFile)) {
                writerWithParam.writer.setOutput(stream);
                IIOImage iioImage = new IIOImage(image, null, writerWithParam.metadata);
                writerWithParam.writer.write(writerWithParam.metadata, iioImage, writerWithParam.writeParam);
            }
        } catch (IOException e) {
            throw new SaveImageException("Error saving JPEG file", e);
        }
    }

    private static final class WriterWithParam {
        final ImageWriter writer;
        final IIOMetadata metadata;
        final ImageWriteParam writeParam;

        private WriterWithParam(ImageWriter writer, IIOMetadata metadata, ImageWriteParam writeParam) {
            this.writer = writer;
            this.metadata = metadata;
            this.writeParam = writeParam;
        }
    }

    private WriterWithParam findJpegWriter() {
        ImageWriter writer = null;
        IIOMetadata metadata = null;
        ImageWriteParam writeParam = null;

        logger.trace("Looking up image writer");
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
            logger.trace("Image writer found: {}", writer);
            return new WriterWithParam(writer, metadata, writeParam);
        } else {
            throw new SaveImageException("Error saving JPEG file: no writer found");
        }
    }
}

package ua.antonsher.mypictool.filewriter;

import ua.antonsher.mypictool.DpiUtil;
import ua.antonsher.mypictool.ImageFileWriter;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class JavaxImageFileWriter implements ImageFileWriter {
    @Override
    public void saveAsJpeg(BufferedImage image, File targetFile, int dpi) throws IOException {
        final String formatName = "JPEG";

        ImageWriter writer = null;
        IIOMetadata metadata = null;
        ImageWriteParam writeParam = null;
        for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName(formatName); iw.hasNext();) {
            writer = iw.next();
            writeParam = writer.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
            metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
            if (metadata != null && !metadata.isReadOnly() && metadata.isStandardMetadataFormatSupported()) {
                break;
            }
        }

        if (writer != null && metadata != null && writeParam != null) {
            double pixelSize = DpiUtil.getPixelSizeMm(dpi);
            // HACK Java ImageIO JPEG writer requires decimeters, not millimeters.
            pixelSize /= 100;

            IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
            horiz.setAttribute("value", Double.toString(pixelSize));

            IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
            vert.setAttribute("value", Double.toString(pixelSize));

            IIOMetadataNode dim = new IIOMetadataNode("Dimension");
            dim.appendChild(horiz);
            dim.appendChild(vert);

            IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
            root.appendChild(dim);

            metadata.mergeTree("javax_imageio_1.0", root);
            try (ImageOutputStream stream = ImageIO.createImageOutputStream(targetFile)) {
                writer.setOutput(stream);
                writer.write(metadata, new IIOImage(image, null, metadata), writeParam);
            }
        }
    }
}

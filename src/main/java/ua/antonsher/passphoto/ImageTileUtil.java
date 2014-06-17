package ua.antonsher.passphoto;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImageTileUtil {

    public static final double MM_IN_INCH = 25.4f;

    public static BufferedImage buildTiledImage(Image input, int width, int height, int smallWidth, int smallHeight) {
        List<Integer> xPositions = calculateImagePositions(width, smallWidth);
        List<Integer> yPositions = calculateImagePositions(height, smallHeight);

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = result.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        for (Integer xPosition : xPositions) {
            for (Integer yPosition : yPositions) {
                graphics.drawImage(input, xPosition, yPosition, smallWidth, smallHeight, null);
            }
        }
        graphics.dispose();

        return result;
    }

    private static List<Integer> calculateImagePositions(int length, int smallLength) {
        List<Integer> positions = new ArrayList<>();
        int n = length / smallLength;
        int skip = length % smallLength / (n + 1);
        for (int i = 0; i < n; ++i) {
            positions.add(skip + (smallLength + skip) * i);
        }
        return positions;
    }

    private static int mmToPixel(int mm, int dpi) {
        return (int) (mm / MM_IN_INCH * dpi);
    }

    private static void saveAsJpeg(BufferedImage image, File targetFile, int dpi) throws IOException {
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
            double pixelSize = MM_IN_INCH / dpi;
            // Java ImageIO JPEG writer requires decimeters, not millimeters.
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

    public static void main(String[] args) {
        for (String arg : args) {
            File file = new File(arg);
            System.out.println("Processing " + file);
            if (file.isFile()) {
                try {
                    BufferedImage input = ImageIO.read(file);
                    int dpi = 300;
                    BufferedImage image = buildTiledImage(input, mmToPixel(150, dpi), mmToPixel(100, dpi), mmToPixel(35, dpi), mmToPixel(45, dpi));
                    String newName = file.getName().replaceAll("(\\.[^.]+)$", "") + "-10x15.jpg";
                    File saveFile = new File(newName);
                    saveAsJpeg(image, saveFile, dpi);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

package ua.antonsher.mypictool.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.antonsher.mypictool.Conversions;
import ua.antonsher.mypictool.ImageFileWriter;
import ua.antonsher.mypictool.LayoutUtil;
import ua.antonsher.mypictool.TiledImageBuilder;
import ua.antonsher.mypictool.filewriter.JavaxImageFileWriter;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Place multiple copies of an image on the white canvas, tiling it evenly.
 */
public class CreateTiledImage {
    private static final Logger logger = LoggerFactory.getLogger(CreateTiledImage.class);
    private static final int DPI = 300;
    private static final Dimension DIMENSION_35x45_MM = new Dimension(35, 45);
    private static final Dimension DIMENSION_10x15_CM = new Dimension(150, 100);
    private static final String FILE_EXTENSION_REGEX = "(\\.[^.]+)$";
    private static final String FILE_NAME_ENDING = "-10x15.jpg";
    private static final String DATE_FORMAT = "YYYY-MM-dd HH:mm:ss";

    public void createTiledImage(File file) throws IOException {
        logger.debug("Reading file {}", file);

        final BufferedImage input = ImageIO.read(file);
        final Dimension canvasDimension = Conversions.mmToPixel(DIMENSION_10x15_CM, DPI);
        final Dimension tileDimension = Conversions.mmToPixel(DIMENSION_35x45_MM, DPI);
        logger.debug("Dimensions of canvas: {}, tile: {}", canvasDimension, tileDimension);

        final String headerCaption = currentTimeCaption();
        logger.debug("Building tiled image with caption '{}'", headerCaption);
        final TiledImageBuilder imageBuilder = new TiledImageBuilder(new LayoutUtil(), canvasDimension, headerCaption, DPI);
        final BufferedImage image = imageBuilder.build(input, tileDimension);

        final String outputFileName = file.getName().replaceAll(FILE_EXTENSION_REGEX, "") + FILE_NAME_ENDING;
        final File outputFile = new File(outputFileName);

        logger.info("Writing output file {}", outputFile);
        final ImageFileWriter imageFileWriter = createImageWriter();
        imageFileWriter.saveAsJpeg(image, outputFile, DPI);
    }

    private ImageFileWriter createImageWriter() {
        return new JavaxImageFileWriter();
    }

    private String currentTimeCaption() {
        return new SimpleDateFormat(DATE_FORMAT).format(new Date());
    }
}

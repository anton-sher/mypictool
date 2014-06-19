package ua.antonsher.mypictool.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.antonsher.mypictool.Conversions;
import ua.antonsher.mypictool.ImageFileWriter;
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
 * Main class of the pic tool. Provides a CLI interface for tiling a single picture on a larger canvas.
 */
public class MyPicTool {
    private static final Logger logger = LoggerFactory.getLogger(MyPicTool.class);

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Tiled image creation tool.");
            System.out.println("Please specify input image files separated by spaces.");
        }
        for (String arg : args) {
            File file = new File(arg);
            logger.info("Processing input file {}", file);
            if (file.isFile()) {
                try {
                    createTiledImage(file);
                    logger.info("Processing input file {} finished", file);
                } catch (Exception e) {
                    logger.warn("Error processing input file {}", file, e);
                }
            }
        }
    }

    private static void createTiledImage(File file) throws IOException {
        logger.debug("Reading file {}", file);
        BufferedImage input = ImageIO.read(file);
        int dpi = 300;
        Dimension canvasDimension = Conversions.mmToPixel(new Dimension(150, 100), dpi);
        Dimension tileDimension = Conversions.mmToPixel(new Dimension(35, 45), dpi);
        logger.debug("Dimensions of canvas: {}, tile: {}", canvasDimension, tileDimension);

        String headerCaption = currentTimeCaption();
        logger.debug("Building tiled image with caption '{}'", headerCaption);
        TiledImageBuilder imageBuilder = new TiledImageBuilder(canvasDimension, headerCaption, dpi);
        BufferedImage image = imageBuilder.build(input, tileDimension);

        String fileExtensionRegex = "(\\.[^.]+)$";
        String outputFileName = file.getName().replaceAll(fileExtensionRegex, "") + "-10x15.jpg";
        File outputFile = new File(outputFileName);
        logger.info("Writing output file {}", outputFile);
        ImageFileWriter imageFileWriter = createImageWriter();
        imageFileWriter.saveAsJpeg(image, outputFile, dpi);
    }

    private static ImageFileWriter createImageWriter() {
        return new JavaxImageFileWriter();
    }

    private static String currentTimeCaption() {
        return new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
    }
}

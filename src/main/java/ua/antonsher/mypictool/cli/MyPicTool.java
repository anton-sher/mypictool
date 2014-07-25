package ua.antonsher.mypictool.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.antonsher.mypictool.actions.CreateTiledImage;
import ua.antonsher.mypictool.filewriter.javaxiio.JavaxImageFileWriterFactory;

import java.io.File;

/**
 * Main class of the pic tool. Provides a CLI interface for some supported actions.
 */
public class MyPicTool {
    private static final Logger logger = LoggerFactory.getLogger(MyPicTool.class);

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Tiled image creation tool. Places 35x45mm tiles on 150x100 mm canvas.");
            System.out.println("Please specify input image files separated by spaces.");
        }
        for (final String arg : args) {
            final File file = new File(arg);
            logger.info("Processing input file {}", file);
            if (file.isFile()) {
                try {
                    final CreateTiledImage createTiledImage = new CreateTiledImage(new JavaxImageFileWriterFactory());
                    createTiledImage.createTiledImage(file);
                    logger.info("Processing input file {} finished", file);
                } catch (Exception e) {
                    logger.warn("Error processing input file {}", file, e);
                }
            }
        }
    }

}

package ua.antonsher.mypictool;

import ua.antonsher.mypictool.filewriter.JavaxImageFileWriter;

import javax.imageio.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class MyPicTool {
    public static void main(String[] args) {
        for (String arg : args) {
            File file = new File(arg);
            System.out.println("Processing " + file);
            if (file.isFile()) {
                try {
                    BufferedImage input = ImageIO.read(file);
                    int dpi = 300;
                    BufferedImage image = new TiledImageBuilder().buildTiledImage(input,
                            DpiUtil.mmToPixel(new Dimension(150, 100), dpi),
                            DpiUtil.mmToPixel(new Dimension(35, 45), dpi));
                    String newName = file.getName().replaceAll("(\\.[^.]+)$", "") + "-10x15.jpg";
                    ImageFileWriter imageFileWriter = new JavaxImageFileWriter();
                    imageFileWriter.saveAsJpeg(image, new File(newName), dpi);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

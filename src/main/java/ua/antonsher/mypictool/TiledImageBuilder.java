package ua.antonsher.mypictool;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class TiledImageBuilder {
    public BufferedImage buildTiledImage(Image input, Dimension canvasDimension, Dimension tileDimension) {
        java.util.List<Integer> xPositions = calculateTilePositions(canvasDimension.width, tileDimension.width);
        java.util.List<Integer> yPositions = calculateTilePositions(canvasDimension.height, tileDimension.height);

        BufferedImage canvas = new BufferedImage(canvasDimension.width, canvasDimension.height, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = canvas.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Integer xPosition : xPositions) {
            for (Integer yPosition : yPositions) {
                graphics.drawImage(input, xPosition, yPosition, tileDimension.width, tileDimension.height, null);
            }
        }
        graphics.dispose();

        return canvas;
    }

    private static java.util.List<Integer> calculateTilePositions(int canvasLength, int tileLength) {
        java.util.List<Integer> positions = new ArrayList<>();
        int fittingTilesCount = canvasLength / tileLength;
        int distanceBetweenTiles = canvasLength % tileLength / (fittingTilesCount + 1);
        for (int i = 0; i < fittingTilesCount; ++i) {
            positions.add(distanceBetweenTiles + (tileLength + distanceBetweenTiles) * i);
        }
        return positions;
    }
}

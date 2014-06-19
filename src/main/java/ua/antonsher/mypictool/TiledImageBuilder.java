package ua.antonsher.mypictool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class TiledImageBuilder {
    private static final Font FONT_BASE = new Font(Font.SANS_SERIF, Font.PLAIN, 10);

    private final Dimension canvasDimension;
    private final String headerCaption;
    private final Font font;

    public TiledImageBuilder(@Nonnull Dimension canvasDimension, @Nullable String headerCaption, int dpi) {
        this.canvasDimension = canvasDimension;
        this.headerCaption = headerCaption;
        double scale = DpiUtil.scaleFactorFromStandardDpi(dpi);
        this.font = FONT_BASE.deriveFont(AffineTransform.getScaleInstance(scale, scale));
    }

    public BufferedImage build(@Nonnull Image input, @Nonnull Dimension tileDimension) {
        BufferedImage canvas = new BufferedImage(canvasDimension.width, canvasDimension.height, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = canvas.createGraphics();
        graphics.getDeviceConfiguration().getNormalizingTransform();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        final int headerHeight;
        if (headerCaption != null) {
            graphics.setFont(font);
            FontMetrics fontMetrics = graphics.getFontMetrics(font);
            headerHeight = fontMetrics.getHeight() + 2;
            graphics.setColor(Color.BLACK);
            graphics.drawString(headerCaption, 1, fontMetrics.getAscent() + 1);
        } else {
            headerHeight = 0;
        }

        java.util.List<Integer> xPositions = LayoutUtil.calculateTilePositions(0, canvasDimension.width, tileDimension.width);
        java.util.List<Integer> yPositions = LayoutUtil.calculateTilePositions(headerHeight, canvasDimension.height, tileDimension.height);
        for (Integer xPosition : xPositions) {
            for (Integer yPosition : yPositions) {
                graphics.drawImage(input, xPosition, yPosition, tileDimension.width, tileDimension.height, null);
            }
        }
        graphics.dispose();

        return canvas;
    }
}

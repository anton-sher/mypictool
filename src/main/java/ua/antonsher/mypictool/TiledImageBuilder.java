package ua.antonsher.mypictool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.antonsher.mypictool.layout.LayoutUtil;
import ua.antonsher.mypictool.util.Conversions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Creates a tiled image with even rectangular tile distribution and optional caption above.
 * Good for preparing passport photo printing.
 */
public class TiledImageBuilder {
    private static final Logger logger = LoggerFactory.getLogger(TiledImageBuilder.class);

    private static final Font FONT_BASE = new Font(Font.SANS_SERIF, Font.PLAIN, 10);

    private final Dimension canvasDimension;
    private final String headerCaption;
    private final Font font;
    private final LayoutUtil layoutUtil;

    /**
     * @param canvasDimension dimensions of the output picture, in pixels.
     * @param headerCaption   caption to add above. Added caption shifts all the tiles down below.
     * @param dpi             resolution of the output image. Required for proper font scaling.
     */
    public TiledImageBuilder(@Nonnull final LayoutUtil layoutUtil, @Nonnull final Dimension canvasDimension, @Nullable final String headerCaption, final int dpi) {
        this.layoutUtil = layoutUtil;
		this.canvasDimension = canvasDimension;
        this.headerCaption = headerCaption;
        double scale = Conversions.scaleFactorFromStandardDpi(dpi);
        this.font = FONT_BASE.deriveFont(AffineTransform.getScaleInstance(scale, scale));
    }

    /**
     * Builds an image with evenly distributed tiles.
     *
     * @param tile          tile image.
     * @param tileDimension size of the tile. Image will be scaled to this size.
     * @return new image with tiles and header caption placed on it.
     */
    public BufferedImage build(@Nonnull final Image tile, @Nonnull final Dimension tileDimension) {
        logger.debug("Building tiled image with tile dimension {}", tileDimension);
        final BufferedImage canvas = new BufferedImage(canvasDimension.width, canvasDimension.height, BufferedImage.TYPE_INT_RGB);
        logger.trace("Created image canvas");

        final Graphics2D graphics = canvas.createGraphics();
        logger.trace("Filling canvas background");
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        final int headerHeight;
        if (headerCaption != null) {
            graphics.setFont(font);
            final FontMetrics fontMetrics = graphics.getFontMetrics(font);
            headerHeight = fontMetrics.getHeight() + 2;
            graphics.setColor(Color.BLACK);
            final int headerPosition = fontMetrics.getAscent() + 1;
            logger.trace("Writing canvas header caption {} at {}, {}", headerCaption, 1, headerPosition);
            graphics.drawString(headerCaption, 1, headerPosition);
        } else {
            logger.trace("No header caption");
            headerHeight = 0;
        }

        final java.util.List<Integer> xPositions = layoutUtil.getEvenDistributionPositions(0, canvasDimension.width, tileDimension.width);
        final java.util.List<Integer> yPositions = layoutUtil.getEvenDistributionPositions(headerHeight, canvasDimension.height, tileDimension.height);
        logger.trace("Calculated positions. x: {}, y: {}", xPositions, yPositions);
        for (final Integer x : xPositions) {
            for (final Integer y : yPositions) {
                logger.trace("Placing image at ({}, {})", x, y);
                graphics.drawImage(tile, x, y, tileDimension.width, tileDimension.height, null);
            }
        }

        logger.trace("Cleaning up");
        graphics.dispose();
        logger.trace("Tiled image created");
        return canvas;
    }
}

package ua.antonsher.mypictool;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculation helper for laying out shapes on some canvas.
 */
public class LayoutUtil {
    /**
     * Calculate positions for placing smaller segments of same length evenly distributed on the larger segment.
     * Only guaranteed to work for positive input values.
     *
     * <pre>
     * x---------------x==============x--x==============x--x==============x--x
     * |---offset---|--|--tileLength--|--|--tileLength--|--|--tileLength--|--|
     * |-------------------------canvasLength--------------------------------|
     * </pre>
     *
     * @param offset length on the beginning that shouldn't be used for tiling.
     * @param canvasLength outer segment length.
     * @param tileLength inner segment length.
     * @return list of positions of left end of tiles to place. May be empty.
     */
    public @Nonnull List<Integer> getEvenDistributionPositions(final int offset, final int canvasLength, final int tileLength) {
        final int effectiveCanvasLength = canvasLength - offset;
        final int fittingTilesCount = effectiveCanvasLength / tileLength;
        final int distanceBetweenTiles = effectiveCanvasLength % tileLength / (fittingTilesCount + 1);

        final List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < fittingTilesCount; ++i) {
            positions.add(offset + distanceBetweenTiles + (tileLength + distanceBetweenTiles) * i);
        }
        return positions;
    }

    /**
     * Calculates a result dimension of an item that is downscaled into the canvas.
     * @param itemDimension dimension of an item to fit onto the canvas.
     * @param canvasDimension dimension into which to fit the item.
     * @param border width of the border to leave when fitting the item.
     * @return resulting dimensions of the item after the downscaling.
     */
    public @Nonnull Dimension fitInto(@Nonnull final Dimension itemDimension, @Nonnull final Dimension canvasDimension, final int border) {
        final int canvasWidth = canvasDimension.width - 2 * border;
        final int canvasHeight = canvasDimension.height - 2 * border;

        if (itemDimension.width <= canvasWidth && itemDimension.height <= canvasHeight) {
            return itemDimension;
        }

        final float widthRatio = (float)canvasWidth / itemDimension.width;
        final float heightRatio = (float)canvasHeight / itemDimension.height;
        final float ratio = Math.min(widthRatio, heightRatio);

        return new Dimension((int)(itemDimension.width * ratio), (int)(itemDimension.height * ratio));
    }
}

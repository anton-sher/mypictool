package ua.antonsher.mypictool;

import javax.annotation.Nonnull;
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
    public static @Nonnull List<Integer> getEvenDistributionPositions(int offset, int canvasLength, int tileLength) {
        int effectiveCanvasLength = canvasLength - offset;
        int fittingTilesCount = effectiveCanvasLength / tileLength;
        int distanceBetweenTiles = effectiveCanvasLength % tileLength / (fittingTilesCount + 1);

        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < fittingTilesCount; ++i) {
            positions.add(offset + distanceBetweenTiles + (tileLength + distanceBetweenTiles) * i);
        }
        return positions;
    }
}

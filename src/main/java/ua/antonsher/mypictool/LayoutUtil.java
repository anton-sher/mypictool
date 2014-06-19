package ua.antonsher.mypictool;

import java.util.ArrayList;
import java.util.List;

public class LayoutUtil {
    public static List<Integer> calculateTilePositions(int offset, int canvasLength, int tileLength) {
        java.util.List<Integer> positions = new ArrayList<>();
        int effectiveCanvasLength = canvasLength - offset;
        int fittingTilesCount = effectiveCanvasLength / tileLength;
        int distanceBetweenTiles = effectiveCanvasLength % tileLength / (fittingTilesCount + 1);
        for (int i = 0; i < fittingTilesCount; ++i) {
            positions.add(offset + distanceBetweenTiles + (tileLength + distanceBetweenTiles) * i);
        }
        return positions;
    }
}

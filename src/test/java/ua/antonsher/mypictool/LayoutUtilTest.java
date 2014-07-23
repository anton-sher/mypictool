package ua.antonsher.mypictool;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LayoutUtilTest {
    private final int offset;
    private final int canvasSize;
    private final int tileSize;
    private final List<Integer> positions;

    public LayoutUtilTest(int offset, int canvasSize, int tileSize, List<Integer> positions) {
        this.offset = offset;
        this.canvasSize = canvasSize;
        this.tileSize = tileSize;
        this.positions = positions;
    }

    @Test
    public void test() {
        assertEquals(positions, new LayoutUtil().getEvenDistributionPositions(offset, canvasSize, tileSize));
    }

    @Parameters(name = "Canvas size {1} tiled to {2} with offset {0} yields {3}")
    public static List<Object[]> parameters() {
        return asList(new Object[][]{
                {2, 10, 5, asList(3)},
                {2, 14, 5, asList(2, 7)},
                {2, 12, 10, asList(2)},
                {2, 10, 10, asList()},
                {0, 10, 5, asList(0, 5)},
                {0, 14, 5, asList(1, 7)},
                {0, 10, 10, asList(0)},
                {0, 10, 11, asList()},
        });
    }
}

package ua.antonsher.mypictool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import ua.antonsher.mypictool.util.Conversions;

import com.google.common.collect.HashMultiset;
import com.google.common.primitives.Ints;

public class TiledImageBuilderTest {
	private LayoutUtil layoutUtil;
	
	@Before
	public void setup() {
		layoutUtil = new LayoutUtil();
	}

    @Test
    public void testTiledImageWithoutCaptionContainsProperColorPoints() throws Exception {
        TiledImageBuilder builder = new TiledImageBuilder(layoutUtil, new Dimension(45, 34), null, (int) Conversions.STANDARD_DPI);
        BufferedImage tile = makeGreenTile();
        BufferedImage tiledImage = builder.build(tile, new Dimension(10, 10));
        ImageIO.write(tiledImage, "JPEG", new File("out0.jpg"));
        assertNotNull(tiledImage);
        assertEquals(tiledImage.getRGB(0, 0), Color.WHITE.getRGB());
        assertEquals(tiledImage.getRGB(1, 1), Color.GREEN.getRGB());
        assertEquals(tiledImage.getRGB(2, 2), Color.GREEN.getRGB());
        assertEquals(tiledImage.getRGB(11, 11), Color.WHITE.getRGB());
        assertEquals(tiledImage.getRGB(12, 9), Color.GREEN.getRGB());
        assertEquals(tiledImage.getRGB(43, 32), Color.GREEN.getRGB());
        assertEquals(tiledImage.getRGB(44, 33), Color.WHITE.getRGB());
        assertEquals(tiledImage.getRGB(0, 33), Color.WHITE.getRGB());
    }

    @Test
    public void testTiledImageWithCaptionContainsEightTiles() throws Exception {
        TiledImageBuilder builder = new TiledImageBuilder(layoutUtil, new Dimension(45, 40), "123", (int) Conversions.STANDARD_DPI);
        BufferedImage tile = makeGreenTile();
        BufferedImage tiledImage = builder.build(tile, new Dimension(10, 10));
        ImageIO.write(tiledImage, "JPEG", new File("out1.jpg"));
        assertNotNull(tiledImage);
        int[] rgb = new int[45 * 40];
        tiledImage.getRGB(0, 0, 45, 40, rgb, 0, 45);
        assertEquals(800, HashMultiset.create(Ints.asList(rgb)).count(Color.GREEN.getRGB()));
    }

    private static BufferedImage makeGreenTile() {
        BufferedImage tile = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = tile.createGraphics();
        graphics.setColor(Color.GREEN);
        graphics.fillRect(0, 0, 10, 10);
        graphics.dispose();
        return tile;
    }

}

package ua.antonsher.mypictool

import spock.lang.Specification

import javax.imageio.ImageIO
import java.awt.Color
import java.awt.Dimension
import java.awt.image.BufferedImage

class TiledImageBuilderTestOld extends Specification {
    def "tiled image without caption contains properly color points"() {
        given:
        def builder = new TiledImageBuilder(new Dimension(45, 34), null, (int)Conversions.STANDARD_DPI)
        BufferedImage tile = makeGreenTile()
        when:
        def tiledImage = builder.build(tile, new Dimension(10, 10))
        ImageIO.write(tiledImage, "JPEG", new File("out0.jpg"))
        then:
        tiledImage != null
        tiledImage.getRGB(0, 0) == Color.WHITE.getRGB()
        tiledImage.getRGB(1, 1) == Color.GREEN.getRGB()
        tiledImage.getRGB(2, 2) == Color.GREEN.getRGB()
        tiledImage.getRGB(11, 11) == Color.WHITE.getRGB()
        tiledImage.getRGB(12, 9) == Color.GREEN.getRGB()
        tiledImage.getRGB(43, 32) == Color.GREEN.getRGB()
        tiledImage.getRGB(44, 33) == Color.WHITE.getRGB()
        tiledImage.getRGB(0, 33) == Color.WHITE.getRGB()
    }

    def "tiled image with caption contains 8 tiles"() {
        given:
        def builder = new TiledImageBuilder(new Dimension(45, 40), "123", (int)Conversions.STANDARD_DPI)
        def tile = makeGreenTile()
        when:
        def tiledImage = builder.build(tile, new Dimension(10, 10))
        ImageIO.write(tiledImage, "JPEG", new File("out1.jpg"))
        then:
        tiledImage != null
        int[] rgb = new int[45 * 40]
        tiledImage.getRGB(0, 0, 45, 40, rgb, 0, 45)
        def rgbList = rgb as List

        rgbList.grep{it == Color.GREEN.RGB}.size() == 800
    }

    private static BufferedImage makeGreenTile() {
        def tile = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB)
        def graphics = tile.createGraphics()
        graphics.setColor(Color.GREEN)
        graphics.fillRect(0, 0, 10, 10)
        graphics.dispose()
        tile
    }
}

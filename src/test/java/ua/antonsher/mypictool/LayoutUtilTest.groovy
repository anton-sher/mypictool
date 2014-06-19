package ua.antonsher.mypictool

import spock.lang.Specification
import spock.lang.Unroll

class LayoutUtilTest extends Specification {
    @Unroll("Canvas size #canvasSize tiled to #tileSize yields #positions")
    def "CalculateTilePositions"() {
        expect:
        positions == LayoutUtil.getEvenDistributionPositions(0, canvasSize, tileSize)
        where:
        canvasSize | tileSize | positions
        10         | 5        | [0, 5]
        14         | 5        | [1, 7]
        10         | 10       | [0]
        10         | 11       | []
    }

    @Unroll("Canvas size #canvasSize tiled to #tileSize with offset #offset yields #positions")
    def "CalculateTilePositions with offset"() {
        expect:
        positions == LayoutUtil.getEvenDistributionPositions(offset, canvasSize, tileSize)
        where:
        offset | canvasSize | tileSize | positions
        2      | 10         | 5        | [3]
        2      | 14         | 5        | [2, 7]
        2      | 12         | 10       | [2]
        2      | 10         | 10       | []
    }
}

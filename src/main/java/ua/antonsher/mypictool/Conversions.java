package ua.antonsher.mypictool;

import java.awt.*;

/**
 * Methods for converting between pixels and physical length taking DPI values into account.
 */
public class Conversions {
    public static final double MM_IN_INCH = 25.4f;
    public static final double STANDARD_DPI = 72.;

    public static int mmToPixel(int mm, int dpi) {
        return (int) (mm / MM_IN_INCH * dpi);
    }

    public static Dimension mmToPixel(Dimension dimensionMm, int dpi) {
        return new Dimension(mmToPixel(dimensionMm.width, dpi), mmToPixel(dimensionMm.height, dpi));
    }

    /**
     * Calculate pixel size in millimeters for given DPI.
     */
    public static double getPixelSizeMm(int dpi) {
        return MM_IN_INCH / dpi;
    }

    /**
     * Get a scale factor for transforming a shape in standard 72 DPI resolution to given DPI value.
     */
    public static double scaleFactorFromStandardDpi(int dpi) {
        return dpi / STANDARD_DPI;
    }
}

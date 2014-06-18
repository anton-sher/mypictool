package ua.antonsher.mypictool;

import java.awt.*;

public class DpiUtil {
    public static final double MM_IN_INCH = 25.4f;

    public static int mmToPixel(int mm, int dpi) {
        return (int) (mm / MM_IN_INCH * dpi);
    }

    public static Dimension mmToPixel(Dimension dimensionMm, int dpi) {
        return new Dimension(mmToPixel(dimensionMm.width, dpi), mmToPixel(dimensionMm.height, dpi));
    }

    public static double getPixelSizeMm(int dpi) {
        return MM_IN_INCH / dpi;
    }

    public static double scaleFromStandardDpi(int dpi) {
        return dpi / 72.;
    }
}

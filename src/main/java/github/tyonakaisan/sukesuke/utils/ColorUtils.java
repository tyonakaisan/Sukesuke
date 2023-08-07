package github.tyonakaisan.sukesuke.utils;

import net.kyori.adventure.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.awt.*;

@DefaultQualifier(NonNull.class)
public class ColorUtils {

    public static TextColor getGradientColor(double percentage, String startColor, String endColor) {
        Color start = Color.decode(startColor);
        Color end = Color.decode(endColor);

        int red = (int) (end.getRed() + percentage * (start.getRed() - end.getRed()));
        int green = (int) (end.getGreen() + percentage * (start.getGreen() - end.getGreen()));
        int blue = (int) (end.getBlue() + percentage * (start.getBlue() - end.getBlue()));

        return TextColor.color(red, green, blue);
    }
}

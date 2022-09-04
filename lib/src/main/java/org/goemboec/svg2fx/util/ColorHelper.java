package org.goemboec.svg2fx.util;

import javafx.scene.paint.Color;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Locale;

public class ColorHelper
{

    public static Color cssColorNameToColor(String name)
    {
        return Arrays.stream(Color.class.getDeclaredFields())
            .filter(f -> {
                var modifiers = f.getModifiers();
                return
                    Modifier.isPublic(modifiers) &&
                        Modifier.isFinal(modifiers) &&
                        Modifier.isStatic(modifiers) &&
                        (f.getType() == Color.class) &&
                        f.getName().toLowerCase().equals(name.toLowerCase());
            })
            .findFirst().map(f -> {
                try {
                    return (Color)f.get(null);
                }
                catch(IllegalAccessException ex)
                {
                    return Color.BLACK;
                }
            }).orElse(Color.BLACK);
    }

    public static Color colorWithOpacity(Color color, double opacity)
    {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

    public static String colorToCssValue(Color color)
    {
        return String.format(Locale.US,"rgba(%d, %d, %d, %f)",
                (int)(255.0*color.getRed() + 0.5),
                (int)(255.0*color.getGreen() + 0.5),
                (int)(255.0*color.getBlue() + 0.5),
                color.getOpacity());
    }

    private ColorHelper()
    {
        throw new RuntimeException("Don't instantiate me.");
    }


}

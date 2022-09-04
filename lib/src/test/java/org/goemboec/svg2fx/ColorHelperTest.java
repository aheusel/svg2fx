package org.goemboec.svg2fx;

import static org.junit.Assert.*;

import javafx.scene.paint.Color;
import org.goemboec.svg2fx.util.ColorHelper;
import org.junit.Test;

public class ColorHelperTest
{

    @Test
    public void colorWithOpacityTest() {
        var color = ColorHelper.colorWithOpacity(Color.SADDLEBROWN, 0.75);
        assertEquals(Color.SADDLEBROWN.getRed(), color.getRed(), 0.0001);
        assertEquals(Color.SADDLEBROWN.getGreen(), color.getGreen(), 0.0001);
        assertEquals(Color.SADDLEBROWN.getBlue(), color.getBlue(), 0.0001);
        assertEquals(0.75, color.getOpacity(), 0.0001);
        assertEquals(1.0, Color.SADDLEBROWN.getOpacity(), 0.0001);
    }

    @Test
    public void colorToCssValueTest() {

        var res = ColorHelper.colorToCssValue(ColorHelper.cssColorNameToColor("lightcyan"));

    }

    @Test
    public void cssColorNameToColorTest() {

        var res = ColorHelper.cssColorNameToColor("lightcyan");
        assertEquals(Color.LIGHTCYAN, res);

        res = ColorHelper.cssColorNameToColor("dodgerblue");
        assertEquals(Color.DODGERBLUE, res);

        res = ColorHelper.cssColorNameToColor("transparent");
        assertEquals(Color.TRANSPARENT, res);

        res = ColorHelper.cssColorNameToColor("foo");
        assertEquals(Color.BLACK, res);

    }


}

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 by Alexander Heusel
 * 
 * This file is part of svgfx.
 *
 * svgfx is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *
 * svgfx is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of svgfx includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of svgfx. Neither the copyright statement nor the attribution
 * may be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do two things regarding copyright notice
 * and author attribution.
 *
 * First, the copyright notice must remain. It must be reproduced in any program
 * that uses svgfx.
 *
 * Second, add an additional notice, stating that you modified svgfx. A suitable
 * notice might read "svgfx source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 */

package org.goemboec.svg2fx;

import javafx.scene.Node;
import javafx.scene.effect.Effect;
import org.goemboec.svg2fx.interfaces.StylePropertyVisitor;

/**
 *
 * @author Alexander Heusel
 */
public abstract class StylePropertyBuilder implements StylePropertyVisitor
{

    protected Node node;
    protected DefsBuilder defs;
    
    protected StylePropertyBuilder(Node node, DefsBuilder defs)
    {
        this.node = node;
        this.defs = defs;
    }
    
    
    @Override
    public void visitAlignmentBaseline(String value)
    {
    }

    @Override
    public void visitBaselineShift(String value)
    {
    }

    @Override
    public void visitClip(String value)
    {
    }

    @Override
    public void visitClipPath(String value)
    {
    }

    @Override
    public void visitClipRule(String value)
    {
    }

    @Override
    public void visitColor(String value)
    {
    }

    @Override
    public void visitColorInterpolation(String value)
    {
    }

    @Override
    public void visitColorInterpolationFilters(String value)
    {
    }

    @Override
    public void visitColorProfile(String value)
    {
    }

    @Override
    public void visitColorRendering(String value)
    {
    }

    @Override
    public void visitCursor(String value)
    {
    }

    @Override
    public void visitDirection(String value)
    {
    }

    @Override
    public void visitDisplay(String value)
    {
    }

    @Override
    public void visitDominantBaseline(String value)
    {
    }

    @Override
    public void visitEnableBackground(String value)
    {
    }

    @Override
    public void visitFill(String value)
    {
    }

    @Override
    public void visitFillOpacity(String value)
    {
    }

    @Override
    public void visitFillRule(String value)
    {
    }

    @Override
    public void visitFilter(String value)
    {
        String style = node.getStyle();
        
        if(!style.isEmpty())
        {
            style += ";";
        }
        
        if(!value.equals("none"))
        {
            value = value.substring(value.indexOf("#") + 1);
            value = value.substring(0, value.indexOf(")")).trim();
            node.setEffect((Effect)defs.getDefs().get(value));
        }        
    }

    @Override
    public void visitFloodColor(String value)
    {
    }

    @Override
    public void visitFloodOpacity(String value)
    {
    }

    @Override
    public void visitFont(String value)
    {
    }

    @Override
    public void visitFontFamily(String value)
    {
    }

    @Override
    public void visitFontSize(String value)
    {
    }

    @Override
    public void visitFontSizeAdjust(String value)
    {
    }

    @Override
    public void visitFontStretch(String value)
    {
    }

    @Override
    public void visitFontStyle(String value)
    {
    }

    @Override
    public void visitFontVariant(String value)
    {
    }

    @Override
    public void visitFontWeight(String value)
    {
    }

    @Override
    public void visitGlyphOrientationHorizontal(String value)
    {
    }

    @Override
    public void visitGlyphOrientationVertical(String value)
    {
    }

    @Override
    public void visitImageRendering(String value)
    {
    }

    @Override
    public void visitKerning(String value)
    {
    }

    @Override
    public void visitLetterSpacing(String value)
    {
    }

    @Override
    public void visitLightingColor(String value)
    {
    }

    @Override
    public void visitMarker(String value)
    {
    }

    @Override
    public void visitMarkerEnd(String value)
    {
    }

    @Override
    public void visitMarkerMid(String value)
    {
    }

    @Override
    public void visitMarkerStart(String value)
    {
    }

    @Override
    public void visitMask(String value)
    {
    }

    @Override
    public void visitOpacity(String value)
    {
    }

    @Override
    public void visitOverflow(String value)
    {
    }

    @Override
    public void visitPointerEvents(String value)
    {
    }

    @Override
    public void visitShapeRendering(String value)
    {
    }

    @Override
    public void visitStopColor(String value)
    {
    }

    @Override
    public void visitStopOpacity(String value)
    {
    }

    @Override
    public void visitStroke(String value)
    {
    }

    @Override
    public void visitStrokeDasharray(String value)
    {
    }

    @Override
    public void visitStrokeDashoffset(String value)
    {
    }

    @Override
    public void visitStrokeLinecap(String value)
    {
    }

    @Override
    public void visitStrokeLinejoin(String value)
    {
    }

    @Override
    public void visitStrokeMiterlimit(String value)
    {
    }

    @Override
    public void visitStrokeOpacity(String value)
    {
    }

    @Override
    public void visitStrokeWidth(String value)
    {
    }

    @Override
    public void visitTextAnchor(String value)
    {
    }

    @Override
    public void visitTextDecoration(String value)
    {
    }

    @Override
    public void visitTextRendering(String value)
    {
    }

    @Override
    public void visitUnicodeBidi(String value)
    {
    }

    @Override
    public void visitVisibility(String value)
    {
    }

    @Override
    public void visitWordSpacing(String value)
    {
    }

    @Override
    public void visitWritingMode(String value)
    {
    }
    
}

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

package svg2fx.interfaces;

/**
 *
 * @author Alexander Heusel
 */
public interface StylePropertyVisitor
{
    public void visitAlignmentBaseline(String value);
    public void visitBaselineShift(String value);
    public void visitClip(String value);
    public void visitClipPath(String value);
    public void visitClipRule(String value);
    public void visitColor(String value);
    public void visitColorInterpolation(String value);
    public void visitColorInterpolationFilters(String value);
    public void visitColorProfile(String value);
    public void visitColorRendering(String value);
    public void visitCursor(String value);
    public void visitDirection(String value);
    public void visitDisplay(String value);
    public void visitDominantBaseline(String value);
    public void visitEnableBackground(String value);
    public void visitFill(String value);
    public void visitFillOpacity(String value);
    public void visitFillRule(String value);
    public void visitFilter(String value);
    public void visitFloodColor(String value);
    public void visitFloodOpacity(String value);
    public void visitFont(String value);
    public void visitFontFamily(String value);
    public void visitFontSize(String value);
    public void visitFontSizeAdjust(String value);
    public void visitFontStretch(String value);
    public void visitFontStyle(String value);
    public void visitFontVariant(String value);
    public void visitFontWeight(String value);
    public void visitGlyphOrientationHorizontal(String value);
    public void visitGlyphOrientationVertical(String value);
    public void visitImageRendering(String value);
    public void visitKerning(String value);
    public void visitLetterSpacing(String value);
    public void visitLightingColor(String value);
    public void visitMarker(String value);
    public void visitMarkerEnd(String value);
    public void visitMarkerMid(String value);
    public void visitMarkerStart(String value);
    public void visitMask(String value);
    public void visitOpacity(String value);
    public void visitOverflow(String value);
    public void visitPointerEvents(String value);
    public void visitShapeRendering(String value);
    public void visitStopColor(String value);
    public void visitStopOpacity(String value);
    public void visitStroke(String value);
    public void visitStrokeDasharray(String value);
    public void visitStrokeDashoffset(String value);
    public void visitStrokeLinecap(String value);
    public void visitStrokeLinejoin(String value);
    public void visitStrokeMiterlimit(String value);
    public void visitStrokeOpacity(String value);
    public void visitStrokeWidth(String value);
    public void visitTextAnchor(String value);
    public void visitTextDecoration(String value);
    public void visitTextRendering(String value);
    public void visitUnicodeBidi(String value);
    public void visitVisibility(String value);
    public void visitWordSpacing(String value);
    public void visitWritingMode(String value);


}

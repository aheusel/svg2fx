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

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Path;
import org.w3c.dom.svg.SVGCircleElement;
import org.w3c.dom.svg.SVGEllipseElement;
import org.w3c.dom.svg.SVGFilterElement;
import org.w3c.dom.svg.SVGGElement;
import org.w3c.dom.svg.SVGLineElement;
import org.w3c.dom.svg.SVGPathElement;
import org.w3c.dom.svg.SVGPathSegArcAbs;
import org.w3c.dom.svg.SVGPathSegArcRel;
import org.w3c.dom.svg.SVGPathSegClosePath;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicAbs;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicRel;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicSmoothAbs;
import org.w3c.dom.svg.SVGPathSegCurvetoCubicSmoothRel;
import org.w3c.dom.svg.SVGPathSegCurvetoQuadraticAbs;
import org.w3c.dom.svg.SVGPathSegCurvetoQuadraticRel;
import org.w3c.dom.svg.SVGPathSegCurvetoQuadraticSmoothAbs;
import org.w3c.dom.svg.SVGPathSegCurvetoQuadraticSmoothRel;
import org.w3c.dom.svg.SVGPathSegLinetoAbs;
import org.w3c.dom.svg.SVGPathSegLinetoHorizontalAbs;
import org.w3c.dom.svg.SVGPathSegLinetoHorizontalRel;
import org.w3c.dom.svg.SVGPathSegLinetoRel;
import org.w3c.dom.svg.SVGPathSegLinetoVerticalAbs;
import org.w3c.dom.svg.SVGPathSegLinetoVerticalRel;
import org.w3c.dom.svg.SVGPathSegMovetoAbs;
import org.w3c.dom.svg.SVGPathSegMovetoRel;
import org.w3c.dom.svg.SVGPatternElement;
import org.w3c.dom.svg.SVGPolylineElement;
import org.w3c.dom.svg.SVGRectElement;
import org.goemboec.svg2fx.interfaces.DocumentVisitor;
import org.goemboec.svg2fx.interfaces.ElementVisitor;

import java.util.Map;

/**
 *
 * @author Alexander Heusel
 */

public class TreeBuilder implements DocumentVisitor
{

    private Group rootGroup;
    private Group currentGroup;
    private StylePropertyBuilder currentAttributeVisitor;
    private ElementVisitor elementVisitor;
    private PathBuilder pathSegVisitor;
    private DefsBuilder defsVisitor;
    
    public TreeBuilder()
    {
        rootGroup = null;
        currentGroup = null;
        currentAttributeVisitor = null;
        elementVisitor = new NodeBuilder(this);
        pathSegVisitor = new PathBuilder();
        defsVisitor = new DefsBuilder();        
    }
    
    public Group getRoot()
    {
        return rootGroup;
    }
    
    public void pushNode(Node newNode)
    {
        
        // If we have no rootGroup newNode must be a Group node.
        // Otherwise add the the new node to current group and set it as current
        // node and create the matching AttributeBuilder.
        if(rootGroup == null)
        {
            rootGroup = (Group)newNode;
        }
        else
        {
            currentGroup.getChildren().add(newNode);
            currentAttributeVisitor = TreeBuilderAttributeVisitorFactory.createBuilder(newNode, defsVisitor);
        }
        
        // If we have a path we need to initialize the PatchBuilder.
        if(newNode instanceof Path)
        {
            pathSegVisitor.pushPath((Path)newNode);
        }
        
        // If we have a group we need to descent one level in the tree.
        if(newNode instanceof Group)
        {
            currentGroup = (Group)newNode;
        }
        
    }
        
    public void closeGroup()
    {        
        // We have to ascend on level in the tree.
        currentGroup = (Group)currentGroup.getParent();
    }
    
    @Override
    public Map<String, String> visitSVGCircleElement(SVGCircleElement ce)
    {
        return elementVisitor.visitSVGCircleElement(ce);
    }

    @Override
    public Map<String, String> visitSVGEllipseElement(SVGEllipseElement ee)
    {
        return elementVisitor.visitSVGEllipseElement(ee);
    }

    @Override
    public Map<String, String> visitSVGLineElement(SVGLineElement le)
    {
        return elementVisitor.visitSVGLineElement(le);
    }

    @Override
    public Map<String, String> visitSVGRectElement(SVGRectElement re)
    {
        return elementVisitor.visitSVGRectElement(re);
    }

    @Override
    public Map<String, String> visitSVGPolylineElement(SVGPolylineElement pe)
    {
        return elementVisitor.visitSVGPolylineElement(pe);
    }

    @Override
    public Map<String, String> visitSVGPathElement(SVGPathElement pe)
    {
        return elementVisitor.visitSVGPathElement(pe);
    }

    @Override
    public Map<String, String> visitSVGGElement(SVGGElement svgGroup)
    {
        return elementVisitor.visitSVGGElement(svgGroup);
    }

    @Override
    public Map<String, String> visitSVGGElementClose(SVGGElement svgGroup)
    {
        return elementVisitor.visitSVGGElementClose(svgGroup);
    }
    
    @Override
    public void visitAlignmentBaseline(String value)
    {
        currentAttributeVisitor.visitAlignmentBaseline(value);
    }

    @Override
    public void visitBaselineShift(String value)
    {
        currentAttributeVisitor.visitBaselineShift(value);
    }

    @Override
    public void visitClip(String value)
    {
        currentAttributeVisitor.visitClip(value);
    }

    @Override
    public void visitClipPath(String value)
    {
        currentAttributeVisitor.visitClipPath(value);
    }

    @Override
    public void visitClipRule(String value)
    {
        currentAttributeVisitor.visitClipRule(value);
    }

    @Override
    public void visitColor(String value)
    {
        currentAttributeVisitor.visitColor(value);
    }

    @Override
    public void visitColorInterpolation(String value)
    {
        currentAttributeVisitor.visitColorInterpolation(value);
    }

    @Override
    public void visitColorInterpolationFilters(String value)
    {
        currentAttributeVisitor.visitColorInterpolationFilters(value);
    }

    @Override
    public void visitColorProfile(String value)
    {
        currentAttributeVisitor.visitColorProfile(value);
    }

    @Override
    public void visitColorRendering(String value)
    {
        currentAttributeVisitor.visitColorRendering(value);
    }

    @Override
    public void visitCursor(String value)
    {
        currentAttributeVisitor.visitCursor(value);
    }

    @Override
    public void visitDirection(String value)
    {
        currentAttributeVisitor.visitDirection(value);
    }

    @Override
    public void visitDisplay(String value)
    {
        currentAttributeVisitor.visitDisplay(value);
    }

    @Override
    public void visitDominantBaseline(String value)
    {
        currentAttributeVisitor.visitDominantBaseline(value);
    }

    @Override
    public void visitEnableBackground(String value)
    {
        currentAttributeVisitor.visitEnableBackground(value);
    }

    @Override
    public void visitFill(String value)
    {
        currentAttributeVisitor.visitFill(value);
    }

    @Override
    public void visitFillOpacity(String value)
    {
        currentAttributeVisitor.visitFillOpacity(value);
    }

    @Override
    public void visitFillRule(String value)
    {
        currentAttributeVisitor.visitFillRule(value);
    }

    @Override
    public void visitFilter(String value)
    {
        currentAttributeVisitor.visitFilter(value);
    }

    @Override
    public void visitFloodColor(String value)
    {
        currentAttributeVisitor.visitFloodColor(value);
    }

    @Override
    public void visitFloodOpacity(String value)
    {
        currentAttributeVisitor.visitFloodOpacity(value);
    }

    @Override
    public void visitFont(String value)
    {
        currentAttributeVisitor.visitFont(value);
    }

    @Override
    public void visitFontFamily(String value)
    {
        currentAttributeVisitor.visitFontFamily(value);
    }

    @Override
    public void visitFontSize(String value)
    {
        currentAttributeVisitor.visitFontSize(value);
    }

    @Override
    public void visitFontSizeAdjust(String value)
    {
        currentAttributeVisitor.visitFontSizeAdjust(value);
    }

    @Override
    public void visitFontStretch(String value)
    {
        currentAttributeVisitor.visitFontStretch(value);
    }

    @Override
    public void visitFontStyle(String value)
    {
        currentAttributeVisitor.visitFontStyle(value);
    }

    @Override
    public void visitFontVariant(String value)
    {
        currentAttributeVisitor.visitFontVariant(value);
    }

    @Override
    public void visitFontWeight(String value)
    {
        currentAttributeVisitor.visitFontWeight(value);
    }

    @Override
    public void visitGlyphOrientationHorizontal(String value)
    {
        currentAttributeVisitor.visitGlyphOrientationHorizontal(value);
    }

    @Override
    public void visitGlyphOrientationVertical(String value)
    {
        currentAttributeVisitor.visitGlyphOrientationVertical(value);
    }

    @Override
    public void visitImageRendering(String value)
    {
        currentAttributeVisitor.visitImageRendering(value);
    }

    @Override
    public void visitKerning(String value)
    {
        currentAttributeVisitor.visitKerning(value);
    }

    @Override
    public void visitLetterSpacing(String value)
    {
        currentAttributeVisitor.visitLetterSpacing(value);
    }

    @Override
    public void visitLightingColor(String value)
    {
        currentAttributeVisitor.visitLightingColor(value);
    }

    @Override
    public void visitMarker(String value)
    {
        currentAttributeVisitor.visitMarker(value);
    }

    @Override
    public void visitMarkerEnd(String value)
    {
        currentAttributeVisitor.visitMarkerEnd(value);
    }

    @Override
    public void visitMarkerMid(String value)
    {
        currentAttributeVisitor.visitMarkerMid(value);
    }

    @Override
    public void visitMarkerStart(String value)
    {
        currentAttributeVisitor.visitMarkerStart(value);
    }

    @Override
    public void visitMask(String value)
    {
        currentAttributeVisitor.visitMask(value);
    }

    @Override
    public void visitOpacity(String value)
    {
        currentAttributeVisitor.visitOpacity(value);
    }

    @Override
    public void visitOverflow(String value)
    {
        currentAttributeVisitor.visitOverflow(value);
    }

    @Override
    public void visitPointerEvents(String value)
    {
        currentAttributeVisitor.visitPointerEvents(value);
    }

    @Override
    public void visitShapeRendering(String value)
    {
        currentAttributeVisitor.visitShapeRendering(value);
    }

    @Override
    public void visitStopColor(String value)
    {
        currentAttributeVisitor.visitStopColor(value);
    }

    @Override
    public void visitStopOpacity(String value)
    {
        currentAttributeVisitor.visitStopOpacity(value);
    }

    @Override
    public void visitStroke(String value)
    {
        currentAttributeVisitor.visitStroke(value);
    }

    @Override
    public void visitStrokeDasharray(String value)
    {
        currentAttributeVisitor.visitStrokeDasharray(value);
    }

    @Override
    public void visitStrokeDashoffset(String value)
    {
        currentAttributeVisitor.visitStrokeDashoffset(value);
    }

    @Override
    public void visitStrokeLinecap(String value)
    {
        currentAttributeVisitor.visitStrokeLinecap(value);
    }

    @Override
    public void visitStrokeLinejoin(String value)
    {
        currentAttributeVisitor.visitStrokeLinejoin(value);
    }

    @Override
    public void visitStrokeMiterlimit(String value)
    {
        currentAttributeVisitor.visitStrokeMiterlimit(value);
    }

    @Override
    public void visitStrokeOpacity(String value)
    {
        currentAttributeVisitor.visitStrokeOpacity(value);
    }

    @Override
    public void visitStrokeWidth(String value)
    {
        currentAttributeVisitor.visitStrokeWidth(value);
    }

    @Override
    public void visitTextAnchor(String value)
    {
        currentAttributeVisitor.visitTextAnchor(value);
    }

    @Override
    public void visitTextDecoration(String value)
    {
        currentAttributeVisitor.visitTextDecoration(value);
    }

    @Override
    public void visitTextRendering(String value)
    {
        currentAttributeVisitor.visitTextRendering(value);
    }

    @Override
    public void visitUnicodeBidi(String value)
    {
        currentAttributeVisitor.visitUnicodeBidi(value);
    }

    @Override
    public void visitVisibility(String value)
    {
        currentAttributeVisitor.visitVisibility(value);
    }

    @Override
    public void visitWordSpacing(String value)
    {
        currentAttributeVisitor.visitWordSpacing(value);
    }

    @Override
    public void visitWritingMode(String value)
    {
        currentAttributeVisitor.visitWritingMode(value);
    }

    @Override
    public void visitSVGPathSegMovetoAbs(SVGPathSegMovetoAbs svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegMovetoAbs(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegMovetoRel(SVGPathSegMovetoRel svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegMovetoRel(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegLinetoAbs(SVGPathSegLinetoAbs svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegLinetoAbs(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegLinetoRel(SVGPathSegLinetoRel svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegLinetoRel(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegLinetoHorizontalAbs(SVGPathSegLinetoHorizontalAbs svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegLinetoHorizontalAbs(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegLinetoHorizontalRel(SVGPathSegLinetoHorizontalRel svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegLinetoHorizontalRel(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegLinetoVerticalAbs(SVGPathSegLinetoVerticalAbs svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegLinetoVerticalAbs(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegLinetoVerticalRel(SVGPathSegLinetoVerticalRel svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegLinetoVerticalRel(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegCurvetoCubicAbs(SVGPathSegCurvetoCubicAbs svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegCurvetoCubicAbs(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegCurvetoCubicRel(SVGPathSegCurvetoCubicRel svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegCurvetoCubicRel(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegCurvetoCubicSmoothAbs(SVGPathSegCurvetoCubicSmoothAbs svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegCurvetoCubicSmoothAbs(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegCurvetoCubicSmoothRel(SVGPathSegCurvetoCubicSmoothRel svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegCurvetoCubicSmoothRel(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegCurvetoQuadraticAbs(SVGPathSegCurvetoQuadraticAbs svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegCurvetoQuadraticAbs(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegCurvetoQuadraticRel(SVGPathSegCurvetoQuadraticRel svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegCurvetoQuadraticRel(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegCurvetoQuadraticSmoothAbs(SVGPathSegCurvetoQuadraticSmoothAbs svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegCurvetoQuadraticSmoothAbs(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegCurvetoQuadraticSmoothRel(SVGPathSegCurvetoQuadraticSmoothRel svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegCurvetoQuadraticSmoothRel(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegArcAbs(SVGPathSegArcAbs svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegArcAbs(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegArcRel(SVGPathSegArcRel svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegArcRel(svgPathSegm);
    }

    @Override
    public void visitSVGPathSegClosePath(SVGPathSegClosePath svgPathSegm)
    {
        pathSegVisitor.visitSVGPathSegClosePath(svgPathSegm);
    }

    @Override
    public void visitSVGPatternElement(SVGPatternElement pe)
    {
        defsVisitor.visitSVGPatternElement(pe);
    }

    @Override
    public void visitSVGFilterElement(SVGFilterElement fe)
    {
        defsVisitor.visitSVGFilterElement(fe);
    }


    
}

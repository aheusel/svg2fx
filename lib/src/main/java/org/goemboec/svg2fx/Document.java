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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.batik.bridge.*;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.goemboec.svg2fx.interfaces.DocumentVisitor;
import org.goemboec.svg2fx.interfaces.StylePropertyVisitor;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.ViewCSS;
import org.w3c.dom.svg.*;

import static org.goemboec.svg2fx.util.AttributeHelper.*;

/**
 *
 * @author Alexander Heusel
 */
public class Document
{

    private SAXSVGDocumentFactory f;
    private SVGDocument doc;
    private UserAgent userAgent;
    private DocumentLoader loader;
    private BridgeContext ctx;
    private GVTBuilder builder;
    private GraphicsNode rootGN;
    private SVGSVGElement svgRoot;
    private ViewCSS vcss;

    public Document(InputStream stream) throws IOException
    {
        f = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
        doc = f.createSVGDocument(null, stream);

        // Boot the CSS engine
        userAgent = new UserAgentAdapter();
        loader = new DocumentLoader(userAgent);
        ctx = new BridgeContext(userAgent, loader);
        ctx.setDynamicState(BridgeContext.DYNAMIC);
        builder = new GVTBuilder();
        rootGN = builder.build(ctx, doc);

        svgRoot = doc.getRootElement();
        vcss = (ViewCSS) doc.getDocumentElement();
    }

    public void visit(DocumentVisitor db)
    {
        visitDefs(db);
        visitGroup(db, null);
    }
    
    private void visitDefs(DocumentVisitor db)
    {
        // Visit all definition of the svg-document
        NodeList nl = svgRoot.getElementsByTagName("defs");
        for(int i = 0; i < nl.getLength(); i++)
        {
            if(nl.item(i) instanceof SVGDefsElement)
            {
                SVGDefsElement svgDefs = (SVGDefsElement)nl.item(i);
                NodeList dnl = svgDefs.getChildNodes();
                for(int j = 0; j < dnl.getLength(); j++)
                {
                    if(dnl.item(j) instanceof SVGPatternElement)
                    {
                        db.visitSVGPatternElement((SVGPatternElement)dnl.item(j));
                    }
                    else if(dnl.item(j) instanceof SVGFilterElement)
                    {
                        db.visitSVGFilterElement((SVGFilterElement)dnl.item(j));
                    }   
                }
            }
        }
    }
    
    private Map<String, String> visitGroup(DocumentVisitor db, SVGGElement group)
    {

        var res = db.visitSVGGElement(group);
        NodeList nl = group == null ? svgRoot.getChildNodes() : group.getChildNodes();

        for(int i = 0; i < nl.getLength(); i++)
        {
            if(nl.item(i) instanceof SVGGElement g)
            {
                var attr = visitGroup(db, g);
                visitStyleAttributes(db, g.getStyle(), attr);
            }
            else if(nl.item(i) instanceof SVGPathElement path)
            {
                var attr = visitPathSeg(db, path);
                visitStyleAttributes(db, path.getStyle(), attr);
            }
            else if(nl.item(i) instanceof SVGCircleElement circle)
            {
                var attr = db.visitSVGCircleElement(circle);
                visitStyleAttributes(db, circle.getStyle(), attr);
            }
            else if(nl.item(i) instanceof SVGEllipseElement ellipse)
            {
                var attr = db.visitSVGEllipseElement(ellipse);
                visitStyleAttributes(db, ellipse.getStyle(), attr);
            }
            else if(nl.item(i) instanceof SVGRectElement rect)
            {
                var attr = db.visitSVGRectElement(rect);
                visitStyleAttributes(db, rect.getStyle(), attr);
            }
            else if(nl.item(i) instanceof SVGLineElement line)
            {
                var attr = db.visitSVGLineElement(line);
                visitStyleAttributes(db, line.getStyle(), attr);
            }
            else if(nl.item(i) instanceof SVGPolylineElement polyLine)
            {
                var attr = db.visitSVGPolylineElement(polyLine);
                visitStyleAttributes(db, polyLine.getStyle(), attr);
            }
        }

        db.visitSVGGElementClose(group);

        return res;
    }
    
    private Map<String, String> visitPathSeg(DocumentVisitor db, SVGPathElement svgPath)
    {
        var res = db.visitSVGPathElement(svgPath);
        

        SVGPathSegList psl = svgPath.getPathSegList();
        SVGPathSeg ps;
        
        for(int i = 0; i < psl.getNumberOfItems(); i++)
        {
            ps = psl.getItem(i);
            
            switch(ps.getPathSegType())
            {
                case SVGPathSeg.PATHSEG_MOVETO_ABS:
                    db.visitSVGPathSegMovetoAbs((SVGPathSegMovetoAbs)ps);
                    break;
                case SVGPathSeg.PATHSEG_MOVETO_REL:
                    db.visitSVGPathSegMovetoRel((SVGPathSegMovetoRel)ps);
                    break;
                case SVGPathSeg.PATHSEG_LINETO_ABS:
                    db.visitSVGPathSegLinetoAbs((SVGPathSegLinetoAbs)ps);
                    break;
                case SVGPathSeg.PATHSEG_LINETO_REL:
                    db.visitSVGPathSegLinetoRel((SVGPathSegLinetoRel)ps);
                    break;
                case SVGPathSeg.PATHSEG_LINETO_HORIZONTAL_ABS:
                    db.visitSVGPathSegLinetoHorizontalAbs((SVGPathSegLinetoHorizontalAbs)ps);
                    break;
                case SVGPathSeg.PATHSEG_LINETO_HORIZONTAL_REL:
                    db.visitSVGPathSegLinetoHorizontalRel((SVGPathSegLinetoHorizontalRel)ps);
                    break;
                case SVGPathSeg.PATHSEG_LINETO_VERTICAL_ABS:
                    db.visitSVGPathSegLinetoVerticalAbs((SVGPathSegLinetoVerticalAbs)ps);
                    break;
                case SVGPathSeg.PATHSEG_LINETO_VERTICAL_REL:
                    db.visitSVGPathSegLinetoVerticalRel((SVGPathSegLinetoVerticalRel)ps);
                    break;
                case SVGPathSeg.PATHSEG_CURVETO_CUBIC_ABS:
                    db.visitSVGPathSegCurvetoCubicAbs((SVGPathSegCurvetoCubicAbs)ps);
                    break;
                case SVGPathSeg.PATHSEG_CURVETO_CUBIC_REL:
                    db.visitSVGPathSegCurvetoCubicRel((SVGPathSegCurvetoCubicRel)ps);
                    break;
                case SVGPathSeg.PATHSEG_CURVETO_CUBIC_SMOOTH_ABS:
                    db.visitSVGPathSegCurvetoCubicSmoothAbs((SVGPathSegCurvetoCubicSmoothAbs)ps);
                    break;
                case SVGPathSeg.PATHSEG_CURVETO_CUBIC_SMOOTH_REL:
                    db.visitSVGPathSegCurvetoCubicSmoothRel((SVGPathSegCurvetoCubicSmoothRel)ps);
                    break;
                case SVGPathSeg.PATHSEG_CURVETO_QUADRATIC_ABS:
                    db.visitSVGPathSegCurvetoQuadraticAbs((SVGPathSegCurvetoQuadraticAbs)ps);
                    break;
                case SVGPathSeg.PATHSEG_CURVETO_QUADRATIC_REL:
                    db.visitSVGPathSegCurvetoQuadraticRel((SVGPathSegCurvetoQuadraticRel)ps);
                    break;
                case SVGPathSeg.PATHSEG_CURVETO_QUADRATIC_SMOOTH_ABS:
                    db.visitSVGPathSegCurvetoQuadraticSmoothAbs((SVGPathSegCurvetoQuadraticSmoothAbs)ps);
                    break;
                case SVGPathSeg.PATHSEG_CURVETO_QUADRATIC_SMOOTH_REL:
                    db.visitSVGPathSegCurvetoQuadraticSmoothRel((SVGPathSegCurvetoQuadraticSmoothRel)ps);
                    break;
                case SVGPathSeg.PATHSEG_ARC_ABS:
                    db.visitSVGPathSegArcAbs((SVGPathSegArcAbs)ps);
                    break;
                case SVGPathSeg.PATHSEG_ARC_REL:
                    db.visitSVGPathSegArcRel((SVGPathSegArcRel)ps);
                    break;
                case SVGPathSeg.PATHSEG_CLOSEPATH:
                    db.visitSVGPathSegClosePath((SVGPathSegClosePath)ps);
                    break;
                default:
                    throw new java.lang.IllegalStateException("Unknown type of SVGPathSegm in SVGPathElement!");
            }    
        }

        return res;
    }    



    private void visitStyleAttributes(StylePropertyVisitor db, CSSStyleDeclaration style, Map<String, String> attr)
    {

        var attributes = mergeWithCSS(attr, CSSStyleDeclarationToMap(style));


        // Dispatch values.
        for(var key : attributes.keySet())
        {
            var value = attributes.get(key);
            switch(key)
            {
                case "alignment-baseline":
                    db.visitAlignmentBaseline(value);
                    break;
                case "baseline-shift":
                    db.visitClip(value);
                    break;
                case "clip-path":
                    db.visitClipPath(value);
                    break;
                case "clip-rule":
                    db.visitClipRule(value);
                    break;
                case "clip":
                    db.visitClip(value);
                    break;
                case "color-interpolation-filters":
                    db.visitColorInterpolationFilters(value);
                    break;
                case "color-interpolation":
                    db.visitColorInterpolation(value);
                    break;
                case "color-profile":
                    db.visitColorProfile(value);
                    break;
                case "color-rendering":
                    db.visitColorRendering(value);
                    break;
                case "color":
                    db.visitColor(value);
                    break;
                case "cursor":
                    db.visitCursor(value);
                    break;
                case "direction":
                    db.visitDirection(value);
                    break;
                case "display":
                    db.visitDisplay(value);
                    break;
                case "dominant-baseline":
                    db.visitDominantBaseline(value);
                    break;
                case "enable-background":
                    db.visitEnableBackground(value);
                    break;
                case "fill-opacity":
                    db.visitFillOpacity(value);
                    break;
                case "fill-rule":
                    db.visitFillRule(value);
                    break;
                case "fill":
                    db.visitFill(value);
                    break;
                case "filter":
                    db.visitFilter(value);
                    break;
                case "flood-color":
                    db.visitFloodColor(value);
                    break;
                case "flood-opacity":
                    db.visitFloodOpacity(value);
                    break;
                case "font-family":
                    db.visitFontFamily(value);
                    break;
                case "font-size-adjust":
                    db.visitFontSizeAdjust(value);
                    break;
                case "font-size":
                    db.visitFontSize(value);
                    break;
                case "font-stretch":
                    db.visitFontStretch(value);
                    break;
                case "font-style":
                    db.visitFontStyle(value);
                    break;
                case "font-variant":
                    db.visitFontVariant(value);
                    break;
                case "font-weight":
                    db.visitFontWeight(value);
                    break;
                case "glyph-orientation-horizontal":
                    db.visitGlyphOrientationHorizontal(value);
                    break;
                case "glyph-orientation-vertical":
                    db.visitGlyphOrientationVertical(value);
                    break;
                case "image-rendering":
                    db.visitImageRendering(value);
                    break;
                case "kerning":
                    db.visitKerning(value);
                    break;
                case "letter-spacing":
                    db.visitLetterSpacing(value);
                    break;
                case "lighting-color":
                    db.visitLightingColor(value);
                    break;
                case "marker-end":
                    db.visitMarkerEnd(value);
                    break;
                case "marker-mid":
                    db.visitMarkerMid(value);
                    break;
                case "marker-start":
                    db.visitMarkerStart(value);
                    break;
                case "mask":
                    db.visitMask(value);
                    break;
                case "opacity":
                    db.visitOpacity(value);
                    break;
                case "overflow":
                    db.visitOverflow(value);
                    break;
                case "pointer-events":
                    db.visitPointerEvents(value);
                    break;
                case "shape-rendering":
                    db.visitShapeRendering(value);
                    break;
                case "stop-color":
                    db.visitStopColor(value);
                    break;
                case "stop-opacity":
                    db.visitStopOpacity(value);
                    break;
                case "stroke-dasharray":
                    db.visitStrokeDasharray(value);
                    break;
                case "stroke-dashoffset":
                    db.visitStrokeDashoffset(value);
                    break;
                case "stroke-linecap":
                    db.visitStrokeLinecap(value);
                    break;
                case "stroke-linejoin":
                    db.visitStrokeLinejoin(value);
                    break;
                case "stroke-miterlimit":
                    db.visitStrokeMiterlimit(value);
                    break;
                case "stroke-opacity":
                    db.visitStrokeOpacity(value);
                    break;
                case "stroke-width":
                    db.visitStrokeWidth(value);
                    break;
                case "stroke":
                    db.visitStroke(value);
                    break;
                case "text-anchor":
                    db.visitTextAnchor(value);
                    break;
                case "text-decoration":
                    db.visitTextDecoration(value);
                    break;
                case "text-rendering":
                    db.visitTextRendering(value);
                    break;
                case "unicode-bidi":
                    db.visitUnicodeBidi(value);
                    break;
                case "visibility":
                    db.visitVisibility(value);
                    break;
                case "word-spacing":
                    db.visitWordSpacing(value);
                    break;
                case "writing-mode":
                    db.visitWritingMode(value);
                    break;
                default:
                    //System.out.format("Unsupported style property: %s ignored.\n", properties[i][0]);
            }
        }

    }
    
}



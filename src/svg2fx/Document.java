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

package svg2fx;

import java.io.IOException;
import java.io.InputStream;
import javafx.scene.shape.Path;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.css.ViewCSS;
import org.w3c.dom.svg.SVGCircleElement;
import org.w3c.dom.svg.SVGDefsElement;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGEllipseElement;
import org.w3c.dom.svg.SVGFilterElement;
import org.w3c.dom.svg.SVGGElement;
import org.w3c.dom.svg.SVGLineElement;
import org.w3c.dom.svg.SVGPathElement;
import org.w3c.dom.svg.SVGPathSeg;
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
import org.w3c.dom.svg.SVGPathSegList;
import org.w3c.dom.svg.SVGPathSegMovetoAbs;
import org.w3c.dom.svg.SVGPathSegMovetoRel;
import org.w3c.dom.svg.SVGPatternElement;
import org.w3c.dom.svg.SVGPolylineElement;
import org.w3c.dom.svg.SVGRectElement;
import org.w3c.dom.svg.SVGSVGElement;
import svg2fx.interfaces.DocumentVisitor;
import svg2fx.interfaces.StylePropertyVisitor;

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
    
    private void visitGroup(DocumentVisitor db, SVGGElement group)
    {

        db.visitSVGGElement(group);
        NodeList nl = group == null ? svgRoot.getChildNodes() : group.getChildNodes();

        for(int i = 0; i < nl.getLength(); i++)
        {
            if(nl.item(i) instanceof SVGGElement)
            {
                visitGroup(db, (SVGGElement) nl.item(i));
                visitStyleAttributes(db, ((SVGGElement) nl.item(i)).getAttributeNode("style"));
            }
            else if(nl.item(i) instanceof SVGPathElement)
            {
                visitPathSeg(db, (SVGPathElement) nl.item(i));
                visitStyleAttributes(db, ((SVGPathElement) nl.item(i)).getAttributeNode("style"));
            }
            else if(nl.item(i) instanceof SVGCircleElement)
            {
                db.visitSVGCircleElement((SVGCircleElement) nl.item(i));
                visitStyleAttributes(db, ((SVGCircleElement) nl.item(i)).getAttributeNode("style"));
            }
            else if(nl.item(i) instanceof SVGEllipseElement)
            {
                db.visitSVGEllipseElement((SVGEllipseElement) nl.item(i));
                visitStyleAttributes(db, ((SVGEllipseElement) nl.item(i)).getAttributeNode("style"));
            }
            else if(nl.item(i) instanceof SVGRectElement)
            {
                db.visitSVGRectElement((SVGRectElement) nl.item(i));
                visitStyleAttributes(db, ((SVGRectElement) nl.item(i)).getAttributeNode("style"));
            }
            else if(nl.item(i) instanceof SVGLineElement)
            {
                db.visitSVGLineElement((SVGLineElement) nl.item(i));
                visitStyleAttributes(db, ((SVGLineElement) nl.item(i)).getAttributeNode("style"));
            }
            else if(nl.item(i) instanceof SVGPolylineElement)
            {
                db.visitSVGPolylineElement((SVGPolylineElement) nl.item(i));
                visitStyleAttributes(db, ((SVGPolylineElement) nl.item(i)).getAttributeNode("style"));
            }
        }

        db.visitSVGGElementClose(group);
       
    }
    
    private void visitPathSeg(DocumentVisitor db, SVGPathElement svgPath)
    {
        db.visitSVGPathElement(svgPath);
        

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
    }    
    
    private void visitStyleAttributes(StylePropertyVisitor db, Node el)
    {
        if(el != null)
        {
            String[] parts = el.getNodeValue().split(";");
            String[][] properties = new String[parts.length][];
            for(int i = 0; i < parts.length; i++)
            {
                properties[i] = parts[i].split(":");
                
                // remove all whitespace and force lower case.
                properties[i][0] = properties[i][0].trim().toLowerCase();
                properties[i][1] = properties[i][1].trim().toLowerCase();
            }
            
            for(int i = 0; i < properties.length; i++)
            {
                switch(properties[i][0])
                {
                    case "alignment-baseline":
                        db.visitAlignmentBaseline(properties[i][1]);
                        break;
                    case "baseline-shift":
                        db.visitClip(properties[i][1]);
                        break;
                    case "clip-path":
                        db.visitClipPath(properties[i][1]);
                        break;
                    case "clip-rule":
                        db.visitClipRule(properties[i][1]);
                        break;
                    case "clip":
                        db.visitClip(properties[i][1]);
                        break;
                    case "color-interpolation-filters":
                        db.visitColorInterpolationFilters(properties[i][1]);
                        break;
                    case "color-interpolation":
                        db.visitColorInterpolation(properties[i][1]);
                        break;
                    case "color-profile":
                        db.visitColorProfile(properties[i][1]);
                        break;
                    case "color-rendering":
                        db.visitColorRendering(properties[i][1]);
                        break;
                    case "color":
                        db.visitColor(properties[i][1]);
                        break;
                    case "cursor":
                        db.visitCursor(properties[i][1]);
                        break;
                    case "direction":
                        db.visitDirection(properties[i][1]);
                        break;
                    case "display":
                        db.visitDisplay(properties[i][1]);
                        break;
                    case "dominant-baseline":
                        db.visitDominantBaseline(properties[i][1]);
                        break;
                    case "enable-background":
                        db.visitEnableBackground(properties[i][1]);
                        break;
                    case "fill-opacity":
                        db.visitFillOpacity(properties[i][1]);
                        break;
                    case "fill-rule":
                        db.visitFillRule(properties[i][1]);
                        break;
                    case "fill":
                        db.visitFill(properties[i][1]);
                        break;
                    case "filter":
                        db.visitFilter(properties[i][1]);
                        break;
                    case "flood-color":
                        db.visitFloodColor(properties[i][1]);
                        break;
                    case "flood-opacity":
                        db.visitFloodOpacity(properties[i][1]);
                        break;
                    case "font-family":
                        db.visitFontFamily(properties[i][1]);
                        break;
                    case "font-size-adjust":
                        db.visitFontSizeAdjust(properties[i][1]);
                        break;
                    case "font-size":
                        db.visitFontSize(properties[i][1]);
                        break;
                    case "font-stretch":
                        db.visitFontStretch(properties[i][1]);
                        break;
                    case "font-style":
                        db.visitFontStyle(properties[i][1]);
                        break;
                    case "font-variant":
                        db.visitFontVariant(properties[i][1]);
                        break;
                    case "font-weight":
                        db.visitFontWeight(properties[i][1]);
                        break;
                    case "glyph-orientation-horizontal":
                        db.visitGlyphOrientationHorizontal(properties[i][1]);
                        break;
                    case "glyph-orientation-vertical":
                        db.visitGlyphOrientationVertical(properties[i][1]);
                        break;
                    case "image-rendering":
                        db.visitImageRendering(properties[i][1]);
                        break;
                    case "kerning":
                        db.visitKerning(properties[i][1]);
                        break;
                    case "letter-spacing":
                        db.visitLetterSpacing(properties[i][1]);
                        break;
                    case "lighting-color":
                        db.visitLightingColor(properties[i][1]);
                        break;
                    case "marker-end":
                        db.visitMarkerEnd(properties[i][1]);
                        break;
                    case "marker-mid":
                        db.visitMarkerMid(properties[i][1]);
                        break;
                    case "marker-start":
                        db.visitMarkerStart(properties[i][1]);
                        break;
                    case "mask":
                        db.visitMask(properties[i][1]);
                        break;
                    case "opacity":
                        db.visitOpacity(properties[i][1]);
                        break;
                    case "overflow":
                        db.visitOverflow(properties[i][1]);
                        break;
                    case "pointer-events":
                        db.visitPointerEvents(properties[i][1]);
                        break;
                    case "shape-rendering":
                        db.visitShapeRendering(properties[i][1]);
                        break;
                    case "stop-color":
                        db.visitStopColor(properties[i][1]);
                        break;
                    case "stop-opacity":
                        db.visitStopOpacity(properties[i][1]);
                        break;
                    case "stroke-dasharray":
                        db.visitStrokeDasharray(properties[i][1]);
                        break;
                    case "stroke-dashoffset":
                        db.visitStrokeDashoffset(properties[i][1]);
                        break;
                    case "stroke-linecap":
                        db.visitStrokeLinecap(properties[i][1]);
                        break;
                    case "stroke-linejoin":
                        db.visitStrokeLinejoin(properties[i][1]);
                        break;
                    case "stroke-miterlimit":
                        db.visitStrokeMiterlimit(properties[i][1]);
                        break;
                    case "stroke-opacity":
                        db.visitStrokeOpacity(properties[i][1]);
                        break;
                    case "stroke-width":
                        db.visitStrokeWidth(properties[i][1]);
                        break;
                    case "stroke":
                        db.visitStroke(properties[i][1]);
                        break;
                    case "text-anchor":
                        db.visitTextAnchor(properties[i][1]);
                        break;
                    case "text-decoration":
                        db.visitTextDecoration(properties[i][1]);
                        break;
                    case "text-rendering":
                        db.visitTextRendering(properties[i][1]);
                        break;
                    case "unicode-bidi":
                        db.visitUnicodeBidi(properties[i][1]);
                        break;
                    case "visibility":
                        db.visitVisibility(properties[i][1]);
                        break;
                    case "word-spacing":
                        db.visitWordSpacing(properties[i][1]);
                        break;
                    case "writing-mode":
                        db.visitWritingMode(properties[i][1]);
                        break;
                    default:
                        //System.out.format("Unsupportet style property: %s ignored.\n", properties[i][0]);
                }
            }
            
        }
    }
    
}



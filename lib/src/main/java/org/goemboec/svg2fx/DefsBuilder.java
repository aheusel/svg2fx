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

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.*;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGFEGaussianBlurElement;
import org.w3c.dom.svg.SVGFilterElement;
import org.w3c.dom.svg.SVGImageElement;
import org.w3c.dom.svg.SVGPatternElement;
import org.goemboec.svg2fx.interfaces.DefsVisitor;

/**
 *
 * @author Alexander Heusel
 */
public class DefsBuilder implements DefsVisitor
{

    private HashMap<String, Object> defs = new HashMap<>();
    
    public DefsBuilder()
    {
    }
    
    
    @Override
    public void visitSVGPatternElement(SVGPatternElement pe)
    {
        String id = pe.getId();
        
        double dx = 0.0;
        double dy = 0.0;
        ArrayList<Transform> tr = Tools.decodeSVGTransforms(pe.getAttribute("patternTransform"));
        for(int i = 0; i < tr.size(); i++)
        {
            if(tr.get(i) instanceof Translate)
            {
                dx += ((Translate)tr.get(i)).getX();
                dy += ((Translate)tr.get(i)).getY();
            }
        }
            
        double width = pe.getWidth().getBaseVal().getValue();
        double height = pe.getHeight().getBaseVal().getValue();
        
        Image image = null;
        NodeList il = pe.getChildNodes();
        for(int k = 0; k < il.getLength(); k++)
        {            
            if(il.item(k) instanceof SVGImageElement)
            {
                  image = visitSVGImageElement((SVGImageElement)il.item(k));
                  break;
            }                            
        }

        if(image == null)
        {
            throw new java.lang.IllegalStateException("Unsupportet pattern-type!");
        }
        else
        {
            ImagePattern imgPat = new ImagePattern(image, dx, dy, width, height, false);
            defs.put(id, imgPat);
            //System.out.format("[ImagePattern] id: %s, x: %f, y: %f, width:%f, height: %f\n", id, imgPat.getX(), imgPat.getY(), imgPat.getWidth(), imgPat.getHeight());
        }
        
    }

    private Image visitSVGImageElement(SVGImageElement ie)
    {
        // data:image/jpeg;base64,
        
        int startIdx = ie.getHref().getBaseVal().indexOf("base64");
        String mimeType = ie.getHref().getBaseVal().substring(0, startIdx);
        mimeType = mimeType.substring(mimeType.indexOf(":") + 1);
        mimeType = mimeType.substring(0, mimeType.indexOf(";")).trim(); 
        
        startIdx = ie.getHref().getBaseVal().indexOf(",", startIdx) + 1;
        
        String base64Data = ie.getHref().getBaseVal().substring(startIdx).trim();
        
        byte[] newBytes = Base64.decode(base64Data);
                
        //Image img = new Image(new ByteArrayInputStream(newBytes), ie.getWidth().getBaseVal().getValue(), ie.getHeight().getBaseVal().getValue(), false, false);
        Image img = new Image(new ByteArrayInputStream(newBytes));
        //System.out.format("[Image] width: %f , height %f, MiME-type: %s, length: %d, base64 starts with: %s...\n", img.getWidth(), img.getHeight(), mimeType, newBytes.length, base64Data.substring(0, 7));
        
        return img;
    }
    
    private GaussianBlur visitSVGFEGaussianBlurElement(SVGFEGaussianBlurElement gb)
    {
        final double inkscapeCompensationFactor = 3.0; 
        return new GaussianBlur(inkscapeCompensationFactor*Double.parseDouble(gb.getAttribute("stdDeviation")));
    }

    @Override
    public void visitSVGFilterElement(SVGFilterElement fe)
    {
        String id = fe.getId();

        GaussianBlur gb = null;
        NodeList nl = fe.getChildNodes();
        for(int k = 0; k < nl.getLength(); k++)
        {
            if(nl.item(k) instanceof SVGFEGaussianBlurElement)
            {
                gb = visitSVGFEGaussianBlurElement((SVGFEGaussianBlurElement)nl.item(k));
                break;
            }
        }
        
        if(gb == null)
        {
            throw new java.lang.IllegalStateException("Unsupportet filter-type!");
        }
        else
        {
            defs.put(id, gb);
            //System.out.format("[GaussianBlur] id: %s, radius: %f\n", id, gb.getRadius());
        }
        
    }

    
    public Map<String, Object> getDefs()
    {
        return Collections.unmodifiableMap(defs);
    }

    
}

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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

/**
 *
 * @author Alexander Heusel
 */
public class ShapeStylePropertyBuilder extends StylePropertyBuilder
{
    protected ShapeStylePropertyBuilder(Node node, DefsBuilder defs)
    {
        super(node, defs);
    }
    
    @Override
    public void visitFill(String value)
    {
        String style = node.getStyle();
        
        if(!style.isEmpty())
        {
            style += ";";
        }
        
        if(value.equals("none"))
        {
            node.setStyle(style + " -fx-fill: null");
        }
        else if(value.startsWith("url"))
        {
            value = value.substring(value.indexOf("#") + 1);
            value = value.substring(0, value.indexOf(")")).trim();
            ((Shape)node).setFill((Paint)defs.getDefs().get(value));
        }
        else
        {
            node.setStyle(style + " -fx-fill: " + value);
        }
    }
    
    @Override
    public void visitStroke(String value)
    {
        String style = node.getStyle();
        
        if(!style.isEmpty())
        {
            style += ";";
        }
        
        if(value.equals("none"))
        {
            node.setStyle(style + " -fx-stroke: null");
        }
        else
        {
            node.setStyle(style + " -fx-stroke: " + value);
        }
        
    }    
   
    @Override
    public void visitStrokeWidth(String value)
    {
        
        String style = node.getStyle();
        
        if(!style.isEmpty())
        {
            style += ";";
        }
        
        if(value.equals("none"))
        {
            node.setStyle(style + " -fx-width: null");
        }
        else
        {
//            if(value.equals("1px"))
//            {
//                System.out.println(node.getId());
//            }
//            
//            double dval = Double.parseDouble(value);
//            final double inkscapeCompensationFactor = 0.5;
            node.setStyle(style + " -fx-stroke-width: " + value);            
        }
        
    }    
    
//            {"stroke-dasharray", "-fx-stroke-dash-array"},
//            {"stroke-dashoffset", "-fx-stroke-dash-offset"},
//            {"stroke-linecap", "-fx-stroke-line-cap"},
//            {"stroke-linejoin", "-fx-stroke-line-join"},
//            {"stroke-miterlimit", "-fx-stroke-miter-limit"},
//            {"stroke-width", "-fx-stroke-width"}
    
}

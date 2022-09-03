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
import javafx.scene.shape.*;

/**
 *
 * @author Alexander Heusel
 */
public class TreeBuilderAttributeVisitorFactory
{
    public static StylePropertyBuilder createBuilder(Node node, DefsBuilder defs)
    {
        if(node instanceof Group)
        {
            return new GroupStylePropertyBuilder(node, defs);
        }
        else if(node instanceof Path)
        {
            return new PathStylePropertyBuilder(node, defs);
        }
        else if(node instanceof Line)
        {
            return new LineStylePropertyBuilder(node, defs);
        }
        else if(node instanceof Rectangle)
        {
            return new RectangleStylePropertyBuilder(node, defs);
        }
        else if(node instanceof Circle)
        {
            return new CircleStylePropertyBuilder(node, defs);
        }
        else if(node instanceof Ellipse)
        {
            return new EllipseStylePropertyBuilder(node, defs);
        }
        else if(node instanceof Polyline)
        {
            return new PolylineStylePropertyBuilder(node, defs);
        }
        else
        {
            throw new java.lang.IllegalArgumentException("Unsupported node type.");
        }        
    }
}

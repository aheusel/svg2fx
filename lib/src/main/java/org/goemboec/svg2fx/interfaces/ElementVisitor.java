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

package org.goemboec.svg2fx.interfaces;

import org.w3c.dom.svg.*;

/**
 *
 * @author Alexander Heusel
 */
public interface ElementVisitor
{
    public void visitSVGCircleElement(SVGCircleElement ce);
    public void visitSVGEllipseElement(SVGEllipseElement ee);
    public void visitSVGLineElement(SVGLineElement le);
    public void visitSVGRectElement(SVGRectElement re);
    public void visitSVGPolylineElement(SVGPolylineElement pe);
    public void visitSVGPathElement(SVGPathElement pe);
    public void visitSVGGElement(SVGGElement svgGroup);
    public void visitSVGGElementClose(SVGGElement svgGroup);
}

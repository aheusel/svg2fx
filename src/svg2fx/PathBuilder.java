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

import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.VLineTo;
import org.w3c.dom.svg.*;
import svg2fx.interfaces.PathSegVisitor;

/**
 *
 * @author Alexander Heusel
 */
public class PathBuilder implements PathSegVisitor
{
    private double x;
    private double y;
    private Path path;

    public PathBuilder()
    {
        this.x = 0.0;
        this.y = 0.0;
        path = null;
    }
    
    public void pushPath(Path path)
    {
        this.x = 0.0;
        this.y = 0.0;
        this.path = path;
    }
    
    private static PathElement last(Path p)
    {
        return p.getElements().get(p.getElements().size());
    }
    
    private void update(ArcTo pe)
    {
        
        if(pe.isAbsolute())
        {
            x = pe.getX();
            y = pe.getY();
        }
        else
        {
            x += pe.getX();
            y += pe.getY();
        }
        path.getElements().add(pe);
    }

    private void update(CubicCurveTo pe)
    {
        if(pe.isAbsolute())
        {
            x = pe.getX();
            y = pe.getY();
        }
        else
        {
            x += pe.getX();
            y += pe.getY();
        }
        path.getElements().add(pe);
    }

    private void update(HLineTo pe)
    {
        if(pe.isAbsolute())
        {
            x = pe.getX();
        }
        else
        {
            x += pe.getX();
        }
        path.getElements().add(pe);
    }

    private void update(LineTo pe)
    {
        if(pe.isAbsolute())
        {
            x = pe.getX();
            y = pe.getY();
        }
        else
        {
            x += pe.getX();
            y += pe.getY();
        }
        path.getElements().add(pe);
    }

    private void update(MoveTo pe)
    {
        if(pe.isAbsolute())
        {
            x = pe.getX();
            y = pe.getY();
        }
        else
        {
            x += pe.getX();
            y += pe.getY();
        }
        path.getElements().add(pe);
    }

    private void update(QuadCurveTo pe)
    {
        if(pe.isAbsolute())
        {
            x = pe.getX();
            y = pe.getY();
        }
        else
        {
            x += pe.getX();
            y += pe.getY();
        }
        path.getElements().add(pe);
    }

    private void update(VLineTo pe)
    {
        if(pe.isAbsolute())
        {
            y = pe.getY();
        }
        else
        {
            y += pe.getY();
        }
        path.getElements().add(pe);
    }
    
    private void update(ClosePath pe)
    {
        path.getElements().add(pe);
    }
        
    @Override
    public void visitSVGPathSegMovetoAbs(SVGPathSegMovetoAbs svgPathSegm)
    {
        MoveTo mt = new MoveTo(svgPathSegm.getX(), svgPathSegm.getY());
        mt.setAbsolute(true);
        update(mt);
    }

    @Override
    public void visitSVGPathSegMovetoRel(SVGPathSegMovetoRel svgPathSegm)
    {
        MoveTo mt = new MoveTo(svgPathSegm.getX(), svgPathSegm.getY());
        mt.setAbsolute(path.getElements().isEmpty());
        update(mt);
    }
    @Override
    public void visitSVGPathSegLinetoAbs(SVGPathSegLinetoAbs svgPathSegm)
    {
        LineTo lt = new LineTo(svgPathSegm.getX(), svgPathSegm.getY());
        lt.setAbsolute(true);
        update(lt);
    }

    @Override
    public void visitSVGPathSegLinetoRel(SVGPathSegLinetoRel svgPathSegm)
    {
        LineTo lt = new LineTo(svgPathSegm.getX(), svgPathSegm.getY());
        lt.setAbsolute(false);
        update(lt);
    }
    @Override
    public void visitSVGPathSegLinetoHorizontalAbs(SVGPathSegLinetoHorizontalAbs svgPathSegm)
    {
        HLineTo hlt = new HLineTo(svgPathSegm.getX());
        hlt.setAbsolute(true);
        update(hlt);
    }
    @Override
    public void visitSVGPathSegLinetoHorizontalRel(SVGPathSegLinetoHorizontalRel svgPathSegm)
    {
        HLineTo hlt = new HLineTo(svgPathSegm.getX());
        hlt.setAbsolute(false);
        update(hlt);
    }
    @Override
    public void visitSVGPathSegLinetoVerticalAbs(SVGPathSegLinetoVerticalAbs svgPathSegm)
    {
        VLineTo vlt = new VLineTo(svgPathSegm.getY());
        vlt.setAbsolute(true);
        update(vlt);
    }
    @Override
    public void visitSVGPathSegLinetoVerticalRel(SVGPathSegLinetoVerticalRel svgPathSegm)
    {
        VLineTo vlt = new VLineTo(svgPathSegm.getY());
        vlt.setAbsolute(false);
        update(vlt);
    }
    @Override
    public void visitSVGPathSegCurvetoCubicAbs(SVGPathSegCurvetoCubicAbs svgPathSegm)
    {
        CubicCurveTo cct = new CubicCurveTo(svgPathSegm.getX1(), svgPathSegm.getY1(),
                                            svgPathSegm.getX2(), svgPathSegm.getY2(),
                                            svgPathSegm.getX(), svgPathSegm.getY());
        cct.setAbsolute(true);
        update(cct);
    }
    @Override
    public void visitSVGPathSegCurvetoCubicRel(SVGPathSegCurvetoCubicRel svgPathSegm)
    {
        CubicCurveTo cct = new CubicCurveTo(svgPathSegm.getX1(), svgPathSegm.getY1(),
                                            svgPathSegm.getX2(), svgPathSegm.getY2(),
                                            svgPathSegm.getX(), svgPathSegm.getY());
        cct.setAbsolute(false);
        update(cct);
    }
    @Override
    public void visitSVGPathSegCurvetoCubicSmoothAbs(SVGPathSegCurvetoCubicSmoothAbs svgPathSegm)
    {
        PathElement pathElement = last(path);
        CubicCurveTo cct = null;
        CubicCurveTo pcct = null;
        if(pathElement instanceof CubicCurveTo)
        {
            pcct = (CubicCurveTo)pathElement;
            if(pcct.isAbsolute())
            {
                cct = new CubicCurveTo( 2*x - pcct.getControlX2(), 2*y - pcct.getControlY2(),
                                        svgPathSegm.getX2(), svgPathSegm.getY2(),
                                        svgPathSegm.getX(), svgPathSegm.getY());
            }
            else
            {
                cct = new CubicCurveTo( x + pcct.getX() - pcct.getControlX2(), y + pcct.getY() - pcct.getControlY2(),
                                        svgPathSegm.getX2(), svgPathSegm.getY2(),
                                        svgPathSegm.getX(), svgPathSegm.getY());                
            }
            
        }
        else
        {
            cct = new CubicCurveTo( x, y,
                                    svgPathSegm.getX2(), svgPathSegm.getY2(),
                                    svgPathSegm.getX(), svgPathSegm.getY());
        }
        cct.setAbsolute(true);
        update(cct);               
    }
    @Override
    public void visitSVGPathSegCurvetoCubicSmoothRel(SVGPathSegCurvetoCubicSmoothRel svgPathSegm)
    {
        PathElement pathElement = last(path);
        CubicCurveTo cct = null;
        CubicCurveTo pcct = null;
        if(pathElement instanceof CubicCurveTo)
        {
            pcct = (CubicCurveTo)pathElement;
            if(pcct.isAbsolute())
            {
                cct = new CubicCurveTo( x - pcct.getControlX2(), y - pcct.getControlY2(),
                                        svgPathSegm.getX2(), svgPathSegm.getY2(),
                                        svgPathSegm.getX(), svgPathSegm.getY());
            }
            else
            {
                cct = new CubicCurveTo( pcct.getX() - pcct.getControlX2(), pcct.getY() - pcct.getControlY2(),
                                        svgPathSegm.getX2(), svgPathSegm.getY2(),
                                        svgPathSegm.getX(), svgPathSegm.getY());                
            }
            
        }
        else
        {
            cct = new CubicCurveTo( 0.0, 0.0,
                                    svgPathSegm.getX2(), svgPathSegm.getY2(),
                                    svgPathSegm.getX(), svgPathSegm.getY());
        }
        cct.setAbsolute(false);
        update(cct);               
    }
    @Override
    public void visitSVGPathSegCurvetoQuadraticAbs(SVGPathSegCurvetoQuadraticAbs svgPathSegm)
    {
        QuadCurveTo qct = new QuadCurveTo(svgPathSegm.getX1(), svgPathSegm.getY1(), svgPathSegm.getX(), svgPathSegm.getY());
        qct.setAbsolute(true);
        update(qct);
    }
    @Override
    public void visitSVGPathSegCurvetoQuadraticRel(SVGPathSegCurvetoQuadraticRel svgPathSegm)
    {
        QuadCurveTo qct = new QuadCurveTo(svgPathSegm.getX1(), svgPathSegm.getY1(), svgPathSegm.getX(), svgPathSegm.getY());
        qct.setAbsolute(false);
        update(qct);
    }
    @Override
    public void visitSVGPathSegCurvetoQuadraticSmoothAbs(SVGPathSegCurvetoQuadraticSmoothAbs svgPathSegm)
    {
        PathElement pathElement = last(path);
        QuadCurveTo qct = null;
        QuadCurveTo pqct = null;
        if(pathElement instanceof QuadCurveTo)
        {
            pqct = (QuadCurveTo)pathElement;
            if(pqct.isAbsolute())
            {
                qct = new QuadCurveTo(  2*x - pqct.getControlX(),
                                        2*y - pqct.getControlY(),
                                        svgPathSegm.getX(), svgPathSegm.getY());
            }
            else
            {
                qct = new QuadCurveTo(  x + pqct.getX() - pqct.getControlX(),
                                        y + pqct.getY() - pqct.getControlY(),
                                        svgPathSegm.getX(), svgPathSegm.getY());
            }
            
        }
        else
        {
            qct = new QuadCurveTo(x, y, svgPathSegm.getX(), svgPathSegm.getY());
        }
        qct.setAbsolute(true);
        update(qct);               
    }
    @Override
    public void visitSVGPathSegCurvetoQuadraticSmoothRel(SVGPathSegCurvetoQuadraticSmoothRel svgPathSegm)
    {
        PathElement pathElement = last(path);
        QuadCurveTo qct = null;
        QuadCurveTo pqct = null;
        if(pathElement instanceof QuadCurveTo)
        {
            pqct = (QuadCurveTo)pathElement;
            if(pqct.isAbsolute())
            {
                qct = new QuadCurveTo(  2*x - pqct.getControlX(),
                                        2*y - pqct.getControlY(),
                                        svgPathSegm.getX(), svgPathSegm.getY());
            }
            else
            {
                qct = new QuadCurveTo(  x + pqct.getX() - pqct.getControlX(),
                                        y + pqct.getY() - pqct.getControlY(),
                                        svgPathSegm.getX(), svgPathSegm.getY());
            }
            
        }
        else
        {
            qct = new QuadCurveTo(0.0, 0.0, svgPathSegm.getX(), svgPathSegm.getY());
        }
        qct.setAbsolute(false);
        update(qct);               
    }
    
    @Override
    public void visitSVGPathSegArcAbs(SVGPathSegArcAbs svgPathSegm)
    {
        ArcTo art = new ArcTo(  svgPathSegm.getR1() , svgPathSegm.getR2(),
                                svgPathSegm.getAngle(), svgPathSegm.getX(), svgPathSegm.getY(),
                                svgPathSegm.getLargeArcFlag(), svgPathSegm.getSweepFlag());
        art.setAbsolute(true);
        update(art);
    }
    
    @Override
    public void visitSVGPathSegArcRel(SVGPathSegArcRel svgPathSegm)
    {
        ArcTo art = new ArcTo(  svgPathSegm.getR1() , svgPathSegm.getR2(),
                                svgPathSegm.getAngle(), svgPathSegm.getX(), svgPathSegm.getY(),
                                svgPathSegm.getLargeArcFlag(), svgPathSegm.getSweepFlag());
        art.setAbsolute(false);
        update(art);
    }
    
    @Override
    public void visitSVGPathSegClosePath(SVGPathSegClosePath svgPathSegm)
    {
        update(new ClosePath());
    }
    
    
}

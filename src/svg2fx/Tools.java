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

import java.util.ArrayList;
import javafx.scene.transform.*;

/**
 *
 * @author Alexander Heusel
 */
public class Tools
{
    private static class TPair
    {
        public TPair(String transformName)
        {
            key = transformName;
        }
        public String key = null;
        public final ArrayList<Double> values = new ArrayList<>();
    }

    public static ArrayList<Transform> decodeSVGTransforms(String transforms)
    {
        //transforms = "translate(130,160),Skewy( 30) rotAte(45,   7 16) ,matrix(1, 6.23e-2 5 4,7.7 16)";
        
        String[] parts = transforms.split("[,\\s\\(\\)]");

        ArrayList<TPair> al = new ArrayList<>();
        TPair currPair = null;
        for(int i = 0; i < parts.length; i++)
        {
            if(!parts[i].equals(""))
            {
                switch(parts[i].toLowerCase())
                {
                    case "matrix":
                    case "translate":
                    case "scale":
                    case "rotate":
                    case "skewx":
                    case "skewy":
                        if(currPair != null)
                        {
                            al.add(currPair);
                        }
                        currPair = new TPair(parts[i].toLowerCase());
                        break;
                    default:
                        if(currPair == null)
                        {
                            throw new java.lang.IllegalStateException("Undefined text-fragments at attribute start!");
                        }
                        else
                        {
                            currPair.values.add(new Double(parts[i]));
                        }
                }
            }
        }
        if(currPair != null)
        {
            al.add(currPair);
            currPair = null;
        }

        ArrayList<Transform> res = new ArrayList<>();
        for(int i = 0; i < al.size(); i++)
        {
            currPair = al.get(i);
            switch(currPair.key)
            {
                case "matrix":
                    if(currPair.values.size() == 6)
                    {
                        Affine at = new Affine();
                        at.setMxx(currPair.values.get(0)); // a
                        at.setMyx(currPair.values.get(1)); // b
                        at.setMxy(currPair.values.get(2)); // c
                        at.setMyy(currPair.values.get(3)); // d
                        at.setTx(currPair.values.get(4)); // e
                        at.setTy(currPair.values.get(5)); // f
                        
                        at.setMxz(0.0);
                        at.setMyz(0.0);
                        at.setMzx(0.0);
                        at.setMzy(0.0);
                        at.setMzz(1.0);
                        at.setTz(0.0);
                        
                        res.add(at);
                    }
                    else
                    {
                        throw new java.lang.IllegalStateException("Wrong number of arguments in matrix transformation!");
                    }
                    break;
                case "translate":
                    if(currPair.values.size() == 1)
                    {
                        res.add(new Translate(currPair.values.get(0), 0.0));
                    }
                    else if(currPair.values.size() == 2)
                    {
                        res.add(new Translate(currPair.values.get(0), currPair.values.get(1)));    
                    }
                    else
                    {
                        throw new java.lang.IllegalStateException("Wrong number of arguments in translate transformation!");
                    }
                    break;
                case "scale":
                    if(currPair.values.size() == 1)
                    {
                        res.add(new Scale(currPair.values.get(0), currPair.values.get(0)));    
                    }
                    else if(currPair.values.size() == 2)
                    {
                        res.add(new Scale(currPair.values.get(0), currPair.values.get(1)));    
                    }
                    else
                    {
                        throw new java.lang.IllegalStateException("Wrong number of arguments in scale transformation!");
                    }
                    break;
                case "rotate":
                    if(currPair.values.size() == 1)
                    {
                        res.add(new Rotate(currPair.values.get(0)));    
                    }
                    else if(currPair.values.size() == 3)
                    {
                        res.add(new Rotate(currPair.values.get(0), currPair.values.get(1), currPair.values.get(2)));    
                    }
                    else
                    {
                        throw new java.lang.IllegalStateException("Wrong number of arguments in rotate transformation!");
                    }
                    break;
                case "skewx":
                    if(currPair.values.size() == 1)
                    {
                        res.add(new Shear(currPair.values.get(0), 0.0));    
                    }
                    else
                    {
                        throw new java.lang.IllegalStateException("Wrong number of arguments in skewX transformation!");
                    }
                    break;
                case "skewy":
                    if(currPair.values.size() == 1)
                    {
                        res.add(new Shear(0.0, currPair.values.get(0)));    
                    }
                    else
                    {
                        throw new java.lang.IllegalStateException("Wrong number of arguments in skewY transformation!");
                    }
                    break;
                default:
                    throw new java.lang.IllegalStateException("Unsupportet transformation!");
            }
        }
        
        return res;
    }
}

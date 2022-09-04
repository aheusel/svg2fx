package org.goemboec.svg2fx.util;

import javafx.scene.paint.Color;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.css.CSSStyleDeclaration;

import javax.naming.directory.ModificationItem;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class AttributeHelper
{

    private final static Pattern cssStyle = Pattern.compile("([^\\:\\s]+)\\s*\\:\\s*([^;\\s]+)\\s*;");
    public static Map<String, String> unpackCssString(String value)
    {
        return cssStyle.matcher(value)
            .results()
            .collect(Collectors.toMap(r -> r.group(1).trim().toLowerCase(), r -> r.group(2).trim().toLowerCase()));
    }

    public static String packCssString(Map<String, String> valueMap)
    {
        return String.join(" ", valueMap.keySet().stream().map(k -> "%s: %s;".formatted(k, valueMap.get(k))).collect(Collectors.toList()));
    }


    public static Map<String, String> getAttributes(NamedNodeMap map, Set<String> filter)
    {
        var attributes = new HashMap<String, String>();
        for(int i = 0; i < map.getLength(); i++)
        {
            var node = map.item(i);
            attributes.put(node.getNodeName(), node.getNodeValue());
        }

        return attributes.keySet().stream()
                .filter(k -> !(filter.contains(k)))
                .collect(Collectors.toUnmodifiableMap(k -> k, k -> attributes.get(k)));

    }

    public static Map<String, String> CSSStyleDeclarationToMap(CSSStyleDeclaration style)
    {
        var res = new HashMap<String, String>();
        for(int i = 0; i < style.getLength(); i++)
        {
            var item = style.item(i);
            var value = style.getPropertyValue(item);
            res.put(item, value);
        }
        return Collections.unmodifiableMap(res);
    }


    public static Map<String, String> mergeWithCSS(Map<String, String> attr, Map<String, String> css)
    {
        var res = new HashMap<>(attr);
        for(var key : css.keySet())
        {
            res.put(key, css.get(key));
        }
        return Collections.unmodifiableMap(res);
    }



    private AttributeHelper()
    {
        throw new RuntimeException("Don't instantiate me.");
    }

}

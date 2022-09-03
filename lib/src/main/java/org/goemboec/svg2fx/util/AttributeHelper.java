package org.goemboec.svg2fx.util;

import org.w3c.dom.NamedNodeMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AttributeHelper
{

    private final static Pattern cssStyle = Pattern.compile("([^\\:\\s]+)\\s*\\:\\s*([^;\\s]+)\\s*;");
    public final static Map<String, String> chopCssStyleString(String value)
    {
        return cssStyle.matcher(value)
            .results()
            .collect(Collectors.toMap(r -> r.group(1).trim().toLowerCase(), r -> r.group(2).trim().toLowerCase()));
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
                .filter(k -> !filter.contains(k))
                .collect(Collectors.toUnmodifiableMap(k -> k, k -> attributes.get(k)));

    }

    private AttributeHelper()
    {
        throw new RuntimeException("Don't instantiate me.");
    }

}

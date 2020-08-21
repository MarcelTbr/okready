package com.marceltbr.okready;

import java.util.HashMap;
import java.util.Map;

public class HelperFunctions {


    protected static Map<String, Object> makeMap(String s, Object object) {

        Map<String, Object> map = new HashMap<String, Object>() {

            {
                put(s, object);

            }

        };
        return map;
    }


}

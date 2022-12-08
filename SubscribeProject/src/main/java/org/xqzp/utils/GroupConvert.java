package org.xqzp.utils;

import org.xqzp.entity.yamlvo.Group;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class GroupConvert {
    public static Map groupConvertToMap(Group group)  {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        Field[] fields = Group.class.getDeclaredFields();

        for(Field field:fields){
            field.setAccessible(true);
            try {
                map.put(field.getName(),field.get(group));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}

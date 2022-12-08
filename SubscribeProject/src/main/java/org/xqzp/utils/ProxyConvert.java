package org.xqzp.utils;

import org.xqzp.entity.yamlvo.Proxy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class ProxyConvert {
    /**
     * 将代理信息封装成LinkedHashMap
     * 用于组装成yaml
     * @param proxy
     * @return
     * @throws IllegalAccessException
     */
    public static Map proxyConvertToMap(Proxy proxy) throws IllegalAccessException {
        Map<String, Object> map = new LinkedHashMap<>();
        Field[] fields = Proxy.class.getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            //如果属性值为空或者不存在就不封装进map
            if(field.get(proxy)==null&&"".equals(field.get(proxy))) continue;

            //如果network的类型是tcp，则不封装opts
            if("opts".equals(field.getName())){
                if("tcp".equals(proxy.getNetwork())) continue;
                map.put(proxy.getNetwork()+"-opts",field.get(proxy));
            }else if("skipCertVerify".equals(field.getName())){
                map.put("skip-cert-verify",field.get(proxy));
            }else {
                map.put(field.getName(),field.get(proxy));
            }
        }
        return map;
    }

    public static ArrayList<LinkedHashMap<String,Object>> proxyListConvertToMapArray(ArrayList<Proxy> array){
        ArrayList<LinkedHashMap<String, Object>> mapArray = new ArrayList<>();
        for (Proxy proxy:array)
            try {
                LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) ProxyConvert.proxyConvertToMap(proxy);
                mapArray.add(map);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        return mapArray;
    }

}
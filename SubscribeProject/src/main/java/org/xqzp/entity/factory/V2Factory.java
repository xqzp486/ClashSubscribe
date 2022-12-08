package org.xqzp.entity.factory;

import org.xqzp.entity.v2.V2Proxy;
import org.xqzp.entity.yamlvo.Proxy;
import org.xqzp.utils.ApplicationContextUtil;

public class V2Factory {
     public static String getInstance(Proxy proxy){
         V2Proxy v2Proxy = ApplicationContextUtil.getBean(proxy.getType());
         return v2Proxy.getSubscribe(proxy);
     }
}

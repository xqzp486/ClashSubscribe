package org.xqzp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xqzp.entity.factory.V2Factory;
import org.xqzp.entity.yamlvo.Proxy;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class V2Service {

    @Autowired
    UserServerService userServerService;

    public String createContribute(String uuid) {
        Map proxyInfoMap = userServerService.getProxyInfoMap(uuid,"v2ray");
        List<Proxy> proxyList = (List<Proxy>) proxyInfoMap.get("proxyList");

        StringWriter sw=new StringWriter();

        for(Proxy proxy:proxyList){
            String instance = V2Factory.getInstance(proxy);
            sw.write(instance);
        }

        //对整体再进行一次base64加密
        byte[] bytes2 = sw.toString().getBytes(StandardCharsets.UTF_8);
        String finalstr = Base64.getEncoder().encodeToString(bytes2);
        return finalstr;
    }
}

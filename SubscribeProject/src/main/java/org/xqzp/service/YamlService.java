package org.xqzp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xqzp.entity.yamlvo.Group;
import org.xqzp.entity.yamlvo.Proxy;
import org.xqzp.utils.GroupConvert;
import org.xqzp.utils.ProxyConvert;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class YamlService {
    @Autowired
    UserServerService userServerService;

    public StringBuffer createYaml(String uuid)  {
        Map map = userServerService.getProxyInfoMap(uuid,"clash");

        //封装proxies数组
        ArrayList<LinkedHashMap<String, Object>> proxyMapArray =
                ProxyConvert.proxyListConvertToMapArray((ArrayList<Proxy>) map.get("proxyList"));

        //封装proxy-groups
        Group group = new Group();
        group.setProxies((ArrayList<String>) map.get("proxyNameList"));
        Map groupMap = GroupConvert.groupConvertToMap(group);

        ArrayList<LinkedHashMap<String, Object>> groupMapArray = new ArrayList<>();
        groupMapArray.add((LinkedHashMap<String, Object>) groupMap);

        ClassPathResource classPathResource=new ClassPathResource("raw.yaml");
        InputStream inputStream = null;
        try {
            inputStream = classPathResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LinkedHashMap<String,Object> yamlmap = new LinkedHashMap<>();

        Yaml rawyaml = new Yaml();
        yamlmap = rawyaml.loadAs(inputStream,LinkedHashMap.class);
        yamlmap.put("proxies",proxyMapArray);
        yamlmap.put("proxy-groups",groupMapArray);

        Yaml finalyaml = new Yaml();
        StringWriter writer = new StringWriter();
        finalyaml.dump(yamlmap,writer);
        return writer.getBuffer();
    }
}

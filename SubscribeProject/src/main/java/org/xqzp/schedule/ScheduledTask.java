package org.xqzp.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xbill.DNS.*;
import org.xqzp.entity.Server;
import org.xqzp.service.ServerService;
import org.xqzp.utils.HttpClientUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Slf4j
@Component
public class ScheduledTask {

    @Autowired
    ServerService serverService;

    @Scheduled(cron = "0 54 14 * * ?")
    public void task() throws Exception {
        String path = "?rp=/rules/normal";
        String result = HttpClientUtils.get("https://idc.wiki/modules/addons/exnetwork/api.php" + path);
        ObjectMapper mapper = new ObjectMapper();
        HashMap map = mapper.readValue(result, HashMap.class);
        ArrayList rules = (ArrayList) map.get("rules");
        LinkedHashMap<String,Object> serverInfoMap = null;

        for(Object rule:rules){
            String name = (String) ((LinkedHashMap)rule).get("label");
            if("SG-GreenCloud-ShangHai".equals(name)){
                serverInfoMap = (LinkedHashMap<String, Object>) rule;
            }
        }

        if(serverInfoMap==null) return;

        String rawAddress = (String) serverInfoMap.get("ext_ip");
        Integer port = (Integer) serverInfoMap.get("ext_port");

        Resolver resolver = new SimpleResolver("114.114.114.114");
        Lookup lookup = new Lookup(rawAddress,Type.TXT);
        lookup.setResolver(resolver);

        Record[] records = lookup.run();

        String ip = null;
        if (lookup.getResult() == Lookup.SUCCESSFUL) {
            // 遍历结果
            for (Record record : records) {
                ip = record.rdataToString();
                ip = ip.replace("\"", "");
            }
        } else {
            log.error("域名txt记录解析失败");
            return;
        }

        QueryWrapper<Server> wrapper = new QueryWrapper<>();
        wrapper.eq("name","SG-GreenCloud-ShangHai");
        Server server = serverService.getOne(wrapper);

        if((!server.getServer().equals(ip))||
                (!server.getPort().equals(port)))
        {
            server.setServer(ip);
            server.setPort(port);
            serverService.updateById(server);
            log.info("ip change");
        }else {
            log.info("ip no change");
        }
    }

}

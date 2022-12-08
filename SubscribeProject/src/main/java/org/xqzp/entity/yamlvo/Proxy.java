package org.xqzp.entity.yamlvo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.xqzp.entity.Server;
import org.xqzp.entity.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Data
public class Proxy implements Serializable{

    private String name;

    private String type;

    private String server;

    private Integer port;

    private String uuid;

    private Integer alterId=0;

    private String cipher = "auto";

    private Boolean udp;

    private Boolean tls;

    private Boolean skipCertVerify;

    private String network;

    private LinkedHashMap<String,String> opts;

    private ArrayList<String> alpn =new ArrayList<String>();


    /**
     * 将服务器信息封装进代理信息中
     *
     * @param server
     * @return
     */
    public Proxy addServerInfo(Server server){
        BeanUtils.copyProperties(server,this);
        //封装h2
        if("h2".equals(server.getAlpn())){
            this.getAlpn().add("h2");
        }

        //封装opts
        // opts以json形式存储在mysql中，取出的时候将其转换成LinkedHashMap
        if(!StringUtils.isEmpty(server.getOpts())){
            opts=new LinkedHashMap<String,String>();
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<LinkedHashMap<String,String>> typeRef = new TypeReference<LinkedHashMap<String,String>>() {};
            try {
                opts = mapper.readValue(server.getOpts(), typeRef);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    /**
     * 将用户信息封装进代理信息中
     * @param user
     * @return
     */
    public Proxy addUserInfo(User user){
        this.setUuid(user.getUuid());
        return this;
    }
}

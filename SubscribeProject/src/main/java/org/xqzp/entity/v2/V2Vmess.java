package org.xqzp.entity.v2;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.xqzp.entity.yamlvo.Proxy;
import java.util.ArrayList;
import java.util.Base64;


@NoArgsConstructor
@Data
@Component(value = "vmess")
public class V2Vmess extends V2Proxy{

    @JsonProperty("v")
    private String v="2";

    @JsonProperty("ps")
    private String name;

    @JsonProperty("add")
    private String server;

    @JsonProperty("port")
    private String port;

    @JsonProperty("id")
    private String uuid;

    @JsonProperty("aid")
    private String aid="0";

    @JsonProperty("scy")
    private String scy="auto";

    @JsonProperty("net")
    private String network;

    @JsonProperty("path")
    private String path;

    @JsonProperty("tls")
    private String tls="";

    @JsonProperty("alpn")
    private String alpn;


    public V2Vmess addProxyInfo(Proxy proxy){
        BeanUtils.copyProperties(proxy,this);
        this.port = proxy.getPort().toString();
        if(proxy.getTls()){
            this.tls="tls";
        }

        String path = proxy.getOpts().get("path");
        if(!StringUtils.isEmpty(path)){
            this.path = path;
        }

        ArrayList<String> alpnArray = proxy.getAlpn();
        if(alpnArray.size()!=0){
            this.alpn = alpnArray.get(0);
        }
        return this;
    }

    @Override
    public String getSubscribe(Proxy proxy) {
        ObjectMapper objectMapper = new ObjectMapper();
        V2Vmess v2Vmess = new V2Vmess().addProxyInfo((Proxy) proxy);
        try {
            //将json对象转换成byte数组,base64只能对原始byte类型的数组进行加密
            byte[] bytes1 = objectMapper.writeValueAsBytes(v2Vmess);
            //将加密形成的字符串,拼接上协议头,再凭借上换行符
            String base64 = Base64.getEncoder().encodeToString(bytes1);
            String proxyStr = "vmess://"+base64+"\n";
            return proxyStr;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}

package org.xqzp.entity.v2;

import org.springframework.stereotype.Component;
import org.xqzp.entity.yamlvo.Proxy;

@Component(value = "vless")
public class V2Vless extends V2Proxy{
    public String getSubscribe(Proxy proxy){
        String stringFormat = "vless://%s@%s:%d?encryption=none&flow=xtls-rprx-origin&security=tls&type=tcp&headerType=none#%s"+"\n";
        String format = String.format(stringFormat, proxy.getUuid(), proxy.getServer(), proxy.getPort(), proxy.getName());
        return format;
    }
}

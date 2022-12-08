package org.xqzp.entity.yamlvo;
import lombok.Data;
import java.util.ArrayList;

@Data
public class Group {
    String name;
    String type;
    ArrayList<String> proxies;

    public Group() {
        this.name = "PROXY";
        this.type = "select";
        this.proxies = new ArrayList<String>();
    }
}
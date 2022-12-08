package org.xqzp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.xqzp.service.UserServerService;
import org.xqzp.service.YamlService;
import org.xqzp.utils.JwtUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/yaml")
public class YamlController {

    @Autowired
    UserServerService userServerService;

    @Autowired
    YamlService yamlService;

    @GetMapping("/getYaml")
    public void getYaml(@RequestParam("token") String token, HttpServletResponse resp){
        String uuid = JwtUtils.getUuidByJwtToken(token);
        StringBuffer buffer = yamlService.createYaml(uuid);

        String downloadFileName="config.yaml";
        resp.reset();
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition","attachment;filename="+downloadFileName);

        int len =buffer.toString().getBytes().length;
        try {
            ServletOutputStream outputStream = resp.getOutputStream();
            outputStream.write(buffer.toString().getBytes(),0,len);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

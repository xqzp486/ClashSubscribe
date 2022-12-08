package org.xqzp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xqzp.service.V2Service;
import org.xqzp.utils.JwtUtils;


@RestController
@RequestMapping("/v2")
public class V2Controller {
    @Autowired
    V2Service v2Service;

    @GetMapping("/getContribute")
    public String getContribute(@RequestParam("token") String token){
        String uuid = JwtUtils.getUuidByJwtToken(token);
        String contribute = v2Service.createContribute(uuid);
        return contribute;
    }
}
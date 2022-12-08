package org.xqzp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.xqzp.entity.Server;
import org.xqzp.entity.yamlvo.Proxy;
import org.xqzp.entity.User;
import org.xqzp.entity.UserServer;
import org.xqzp.exception.ProxyException;
import org.xqzp.mapper.UserServerMapper;
import org.xqzp.service.ServerService;
import org.xqzp.service.UserServerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xqzp.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 */
@Service
public class UserServerServiceImpl extends ServiceImpl<UserServerMapper, UserServer> implements UserServerService {

    @Autowired
    ServerService serverService;

    @Autowired
    UserService userService;

    public Map getProxyInfoMap(String uuid,String type){
        //查询用户信息
        QueryWrapper<User> userwrapper = new QueryWrapper<>();
        userwrapper.eq("uuid",uuid);
        User user = userService.getOne(userwrapper);

        //======================================================
        //根据用户信息查询用户拥有的节点信息
        //1.先获取user和server的对应关系
        QueryWrapper<UserServer> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",user.getId());
        List<UserServer> userServerList = baseMapper.selectList(wrapper);

        List<Integer> array = userServerList.stream()
                        .map((UserServer::getSid))
                        .collect(Collectors.toList());

        if(array.size()==0){
            throw new ProxyException(404,"没有可以使用的结点");
        }

        List<Server> serverList = serverService.listByIds(array);

        //如果是clash,则过滤掉vless节点
        if("clash".equals(type)){
            serverList = serverList.stream().filter(server -> !"vless".equals(server.getType()))
                    .collect(Collectors.toList());
        }

        //返回一个节点组
        List<Proxy> proxyList = new ArrayList<>();

        //返回结点的名称
        List<String> proxyNameList = new ArrayList<String>();

        //将用户信息和服务器信息封装进节点信息
        for (Server server:serverList){
            Proxy proxy = new Proxy();
            proxy.addServerInfo(server).addUserInfo(user);
            proxyList.add(proxy);
            proxyNameList.add(server.getName());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("proxyList",proxyList);
        map.put("proxyNameList",proxyNameList);

        return map;
    }
}

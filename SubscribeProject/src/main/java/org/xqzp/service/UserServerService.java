package org.xqzp.service;


import org.xqzp.entity.UserServer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2022-08-11
 */
public interface UserServerService extends IService<UserServer> {
    Map getProxyInfoMap(String uuid,String type);
}

package org.xqzp.config;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeyConfig implements InitializingBean {
    @Value("${jjwt.secret}")
    private String app_secret;

    @Value("${wikihost.x-api-key}")
    private String x_api_key;

    @Value("${wikihost.x-api-password}")
    private String x_api_password;

    public static String APP_SECRET;
    public static String X_API_KEY;
    public static String X_API_PASSWORD;

    @Override
    public void afterPropertiesSet() throws Exception {
        APP_SECRET = app_secret;
        X_API_KEY = x_api_key;
        X_API_PASSWORD = x_api_password;
    }
}

package com.zxw.baicizhanhelper.config;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * class description
 *
 * @author zhangxunwei
 * @date 2022/8/11
 */
@Configuration
public class HttpClientConfig {

    @Bean
    public HttpClientConnectionManager httpClientConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager;

        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(120);
        connectionManager.setDefaultMaxPerRoute(40);

        return connectionManager;
    }
}

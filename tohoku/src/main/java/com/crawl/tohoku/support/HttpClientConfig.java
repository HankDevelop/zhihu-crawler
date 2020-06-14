package com.crawl.tohoku.support;

import io.netty.handler.codec.http.HttpUtil;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NHttpClientConnectionManager;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Configuration
public class HttpClientConfig {

    private Integer maxTotal = 200;

    private Integer defaultMaxPerRoute = 20;

    private Integer connectTimeout = 60000;

    private Integer connectionRequestTimeout = 60000;

    private Integer socketTimeout = 60000;

    @Bean(name = "httpClientConnectionManager")
    public PoolingHttpClientConnectionManager getHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        httpClientConnectionManager.setMaxTotal(maxTotal);
        httpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return httpClientConnectionManager;
    }

    @Bean(name = "httpClientBuilder")
    public HttpClientBuilder getHttpClientBuilder(
            @Qualifier("httpClientConnectionManager") PoolingHttpClientConnectionManager httpClientConnectionManager) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(httpClientConnectionManager);
        return httpClientBuilder;
    }

    @Bean
    public CloseableHttpClient getCloseableHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder) {
        return httpClientBuilder.build();
    }

    @Bean(name = "nHttpClientConnectionManager")
    public NHttpClientConnectionManager getNHttpClientConnectionManager() throws IOReactorException {
//        // 绕过证书验证，处理https请求
//        SSLContext sslcontext = createIgnoreVerifySSL();
//
//        // 设置协议http和https对应的处理socket链接工厂的对象
//        Registry<SchemeIOSessionStrategy> sessionStrategyRegistry = RegistryBuilder
//                .<SchemeIOSessionStrategy>create().register("http", NoopIOSessionStrategy.INSTANCE)
//                .register("https", new SSLIOSessionStrategy(sslcontext, new AllowAllHostnameVerifier())).build();
        // 配置io线程
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setConnectTimeout(socketTimeout).setSoTimeout(connectTimeout)
                .setRcvBufSize(8192).setSndBufSize(8192).build();

        // 设置连接池大小
        ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);

        PoolingNHttpClientConnectionManager nHttpClientConnectionManager = new PoolingNHttpClientConnectionManager(ioReactor);
        nHttpClientConnectionManager.setMaxTotal(maxTotal);
        nHttpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return nHttpClientConnectionManager;
    }


    @Bean(name = "httpAsyncClientBuilder")
    public HttpAsyncClientBuilder getHttpAsyncClientBuilder(
            @Qualifier("nHttpClientConnectionManager") NHttpClientConnectionManager nHttpClientConnectionManager) {
        HttpAsyncClientBuilder httpAsyncClientBuilder = HttpAsyncClients.custom();
        httpAsyncClientBuilder.setConnectionManager(nHttpClientConnectionManager);
        return httpAsyncClientBuilder;
    }

    @Bean
    public CloseableHttpAsyncClient getCloseableHttpAsyncClient(@Qualifier("httpAsyncClientBuilder") HttpAsyncClientBuilder httpAsyncClientBuilder) {
        return httpAsyncClientBuilder.build();
    }

    @Bean(name = "builder")
    public RequestConfig.Builder getBuilder() {
        RequestConfig.Builder builder = RequestConfig.custom();
        return builder.setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout).setCookieSpec(CookieSpecs.STANDARD_STRICT);
    }

    @Bean
    public RequestConfig getRequestConfig(@Qualifier("builder") RequestConfig.Builder builder) {
        return builder.build();
    }

    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("TLS");
            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sc.init(null, new TrustManager[]{trustManager}, null);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sc;

    }
}
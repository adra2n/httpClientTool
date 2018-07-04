package com.taolc.http;

import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.*;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

/**
 * httpClient 连接工具类
 */
public class HttpClientTool {
    private static Logger logger = LoggerFactory.getLogger(HttpClientTool.class);
    /**
     * 连接池管理
     */
    private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
    /**
     * 请求参数配置
     */
    private static RequestConfig requestConfig;
    /**
     * 请求重试处理器
     */
    private static HttpRequestRetryHandler httpRequestRetryHandler;

    /**
     * 注意点
     * 1：如果客户端连接的目标服务器只有一个，那么设置最大route连接数和最大连接池连接数相同，以便高效利用连接池中创建的连接
     * 2：创建httpClient对象
     */
    static {
        SSLContext sslContext = SSLContexts.createSystemDefault();
        //协议方案
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http",PlainConnectionSocketFactory.getSocketFactory())
                .register("https",new SSLConnectionSocketFactory(sslContext))
                .build();
        //创建一个自定义的连接池管理
        poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        //创建socket配置
        SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
        poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);
        //创建信息规定参数 默认配置都是-1
        MessageConstraints messageConstraints = MessageConstraints.custom()
                .setMaxHeaderCount(-1)
                .setMaxLineLength(-1)
                .build();
        //创建连接参数
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .setMessageConstraints(messageConstraints)
                .build();
        poolingHttpClientConnectionManager.setDefaultConnectionConfig(connectionConfig);
        //连接池最大生成连接数
        poolingHttpClientConnectionManager.setMaxTotal(2);
        //默认设置route最大连接数
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(1);

        requestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM,AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                //httpclient使用连接池来管理连接，这个时间就是从连接池获取连接的超时时间
                .setConnectionRequestTimeout(3000)
                //连接建立后，数据传输过程中数据包之间间隔的最大时间
                .setConnectTimeout(3000)
                //连接建立时间，即三次握手完成时间
                .setSocketTimeout(3000)
                .build();

        //请求重试处理
        httpRequestRetryHandler = (exception, executionCount, context) -> {
            if (executionCount >= 1) {// 如果已经重试了1次，就放弃
                return false;
            }
            if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                return true;
            }
            if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                return false;
            }
            if (exception instanceof InterruptedIOException) {// 超时
                return false;
            }
            if (exception instanceof UnknownHostException) {// 目标服务器不可达
                return false;
            }
            if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                return false;
            }
            if (exception instanceof SSLException) {// ssl握手异常
                return false;
            }

            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 如果请求是幂等的，就再次尝试
            if (!(request instanceof HttpEntityEnclosingRequest)) {
                return true;
            }
            return false;
        };
    }

    /**
     * 从连接池中获取连接对象
     * @return
     */
    public static CloseableHttpClient getHttpClient(){
        return HttpClients.custom()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setRetryHandler(httpRequestRetryHandler)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public static void main(String[] args) {

        for(int i=0;i<10;i++){
            CloseableHttpClient closeableHttpClient1 = getHttpClient();
            System.out.println(closeableHttpClient1);
        }
        String response = HttpClientTool.get("http://www.baidu.com");
        logger.info(response);
    }

    /**
     * http get
     * @param url
     * @return
     */
    public static String get(String url){
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        HttpEntity httpEntity = null;
        try {
            response = closeableHttpClient.execute(httpGet);
            System.out.println(response.getStatusLine());
            httpEntity = response.getEntity();
            httpGet.abort();
            return EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                EntityUtils.consume(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * http post 方法
     * @param url
     * @return
     */
    public static String post(String url){
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            closeableHttpResponse = closeableHttpClient.execute(httpPost);
            System.out.println(closeableHttpResponse.getStatusLine());
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            EntityUtils.consume(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                closeableHttpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

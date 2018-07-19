package com.taolc.http;

import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.*;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * httpClient 连接工具类
 */
public class HttpClientTool {

    private static Logger logger = LoggerFactory.getLogger(HttpClientTool.class);

    //是否打印日志
    private static boolean DEBUG = true;
    //连接池最大连接数
    private static int MAX_TOTAL = 20;
    //route最大连接数
    private static int MAX_PRE_ROUTE = 10;
    //httpclient使用连接池来管理连接，这个时间就是从连接池获取连接的超时时间
    private static int REQUEST_TIMEOUT = 6000;
    //连接建立后，数据传输过程中数据包之间间隔的最大时间
    private static int CONNECT_TIMEOUT = 6000;
    //连接建立时间，即三次握手完成时间
    private static int SOCKET_TIMEOUT = 10000;
    //连接重试次数
    private static int RETRYP_HTTP_COUNT = 1;
    //编码
    private static String ENCODING = "utf-8";


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
     * httpClient 客户端
     */
    private static CloseableHttpClient httpClient;

    /**
     * 注意点
     * 1：如果客户端连接的目标服务器只有一个，那么设置最大route连接数和最大连接池连接数相同，以便高效利用连接池中创建的连接
     * 2：创建httpClient对象
     */
    static {
        SSLContext sslContext = SSLContexts.createSystemDefault();
        //协议方案
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", new SSLConnectionSocketFactory(sslContext))
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
        poolingHttpClientConnectionManager.setMaxTotal(MAX_TOTAL);
        //默认设置route最大连接数
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(MAX_PRE_ROUTE);

        requestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                //httpclient使用连接池来管理连接，这个时间就是从连接池获取连接的超时时间
                .setConnectionRequestTimeout(REQUEST_TIMEOUT)
                //连接建立后，数据传输过程中数据包之间间隔的最大时间
                .setConnectTimeout(CONNECT_TIMEOUT)
                //连接建立时间，即三次握手完成时间
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();

        //请求重试处理
        httpRequestRetryHandler = (exception, executionCount, context) -> {
            if (executionCount >= RETRYP_HTTP_COUNT) {// 如果已经重试了n次，就放弃
                if (DEBUG) {
                    logger.info("已经重试了{}次，放弃重试", executionCount - 1);
                }
                return false;
            }
            if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                if (DEBUG) {
                    logger.info("服务器丢掉了连接，重试请求");
                }
                return true;
            }
            if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                if (DEBUG) {
                    logger.info("SSL握手异常，放弃重试");
                }
                return false;
            }
            if (exception instanceof InterruptedIOException) {// 超时
                if (DEBUG) {
                    logger.info("超时异常，放弃重试");
                }
                return false;
            }
            if (exception instanceof UnknownHostException) {// 目标服务器不可达
                if (DEBUG) {
                    logger.info("目标服务器不可达，放弃重试");
                }
                return false;
            }
            if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                if (DEBUG) {
                    logger.info("连接被拒绝，放弃重试");
                }
                return false;
            }
            if (exception instanceof SSLException) {// ssl异常
                if (DEBUG) {
                    logger.info("ssl异常，放弃重试");
                }
                return false;
            }

            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 如果请求是幂等的，就再次尝试
            if (!(request instanceof HttpEntityEnclosingRequest)) {
                if (DEBUG) {
                    logger.info("当前请求是幂等，重试请求");
                }
                return true;
            }
            return false;
        };

        httpClient = HttpClients.custom()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setRetryHandler(httpRequestRetryHandler)
                .setDefaultRequestConfig(requestConfig)
                .build();

        printStatus();
    }

    private static void printStatus(){
        if(poolingHttpClientConnectionManager != null){
            logger.info("连接池状态 --> {}",poolingHttpClientConnectionManager.getTotalStats());
        }
    }

    /**
     * 发送get请求，不带请求头和请求参数
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        return get(url, null);
    }

    /**
     * 发送get请求，带请求参数
     *
     * @param url
     * @param params
     * @return
     */
    public static String get(String url, Map<String, String> params) {
        return get(url, null, params);
    }

    /**
     * 发送get请求，带请求头和请求参数
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String get(String url, Map<String, String> headers, Map<String, String> params) {
        //创建访问的地址
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null) {
                params.forEach((k, v) -> uriBuilder.setParameter(k, v));
            }
            //创建http对象
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            packageHeader(headers, httpGet);
            return execute(httpGet);
        } catch (URISyntaxException e) {
            logger.error("根据url创建URIBuilder失败 --> {}", e.getMessage());
            return null;
        }
    }

    /**
     * post 请求 不带请求头和请求参数
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String post(String url) {
        return post(url, null);
    }

    /**
     * post请求 带请求参数
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params) {
        return post(url, null, params);
    }

    /**
     * 发送post请求 带请求头和请求参数
     *
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> headers,
                              Map<String, String> params) {
        //创建HttpPost对象
        HttpPost httpPost = new HttpPost(url);
        packageHeader(headers, httpPost);
        packageParam(params, httpPost);
        return execute(httpPost);
    }

    /**
     * 发送post请求 带json参数
     *
     * @param url
     * @param jsonString
     * @return
     */
    public static String postJson(String url, String jsonString) {
        return postJson(url,null,jsonString);
    }

    /**
     * 发送post请求，带请求头和json参数
     * @param url
     * @param headers
     * @param jsonString
     * @return
     */
    public static String postJson(String url,Map<String,String> headers,String jsonString){
        HttpPost httpPost = new HttpPost(url);
        packageHeader(headers,httpPost);
        httpPost.setEntity(new StringEntity(jsonString,ContentType.APPLICATION_JSON));
        return execute(httpPost);
    }

    /**
     * 发送put请求，不带请求参数
     *
     * @param url
     * @return
     */
    public static String put(String url) {
        return put(url, null);
    }

    /**
     * 发送put请求，带请求参数
     *
     * @param url
     * @param params
     * @return
     */
    public static String put(String url, Map<String, String> params) {
        HttpPut httpPut = new HttpPut(url);
        packageParam(params, httpPut);
        return execute(httpPut);
    }

    /**
     * 封装请求头
     *
     * @param params
     * @param httpRequestBase
     */
    private static void packageHeader(Map<String, String> params, HttpRequestBase httpRequestBase) {
        //封装请求头
        if (params != null) {
            params.forEach((k, v) -> httpRequestBase.setHeader(k, v));
        }
    }

    /**
     * 封装请求参数
     *
     * @param params
     * @param httpMethod
     */
    private static void packageParam(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod) {
        //封装请求参数
        if (params != null) {
            List<NameValuePair> nameValuePairs = new ArrayList<>(params.size());
            params.forEach((k, v) -> nameValuePairs.add(new BasicNameValuePair(k, v)));
            //设置请求参数到http对象中
            try {
                httpMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs,ENCODING));
            } catch (UnsupportedEncodingException e) {
                logger.error("将请求参数设置到http对象异常 --> {}", e.getMessage());
            }
        }
    }

    /**
     * 获取响应结果
     *
     * @param httpMethod
     * @return
     * @throws IOException
     */
    private static String execute(HttpRequestBase httpMethod) {
        CloseableHttpResponse httpResponse = null;
        //执行请求
        try {
            httpResponse = httpClient.execute(httpMethod);
            //获取返回结果
            if (httpResponse != null) {
                if (DEBUG) {
                    logger.info("http 请求url {} --> 响应码[{}]", httpMethod.getURI(), httpResponse.getStatusLine().getStatusCode());
                }
                if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    if(DEBUG){
                        String content = EntityUtils.toString(httpResponse.getEntity(),ENCODING);
                        logger.info("响应内容 --> {}",content);
                        return content;
                    }
                    return EntityUtils.toString(httpResponse.getEntity(),ENCODING);
                }else{
                    if(httpResponse.getEntity() != null){
                        if(DEBUG){
                            logger.info("响应内容 --> {}",EntityUtils.toString(httpResponse.getEntity(),ENCODING));
                        }
                    }
                }
            }
            return null;
        } catch (IOException e) {
            logger.error("执行http请求异常 --> {}", e.getMessage());
            return null;
        } finally {
            if(httpResponse != null){
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}

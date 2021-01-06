package com.geyao.manager.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.*;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;

/**
 * http请求工具类 get,post
 *
 * @author yanchaoyin
 */
public class HttpClientUtil {

    private static Logger logger = LoggerFactory
            .getLogger(HttpClientUtil.class);
    private static PoolingHttpClientConnectionManager connManager = null;
    private static CloseableHttpClient httpclient = null;
    public final static int connectTimeout = 120000;

    static {
        try {
            SSLContext sslContext = SSLContexts.custom().useTLS().build();
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs,
                                               String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs,
                                               String authType) {
                }
            }}, null);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https",
                            new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)).build();

            connManager = new PoolingHttpClientConnectionManager(
                    socketFactoryRegistry);
            httpclient = HttpClients.custom().setConnectionManager(connManager)
                    .build();
            // Create socket configuration
            SocketConfig socketConfig = SocketConfig.custom()
                    .setTcpNoDelay(true).build();
            connManager.setDefaultSocketConfig(socketConfig);
            // Create message constraints
            MessageConstraints messageConstraints = MessageConstraints.custom()
                    .setMaxHeaderCount(200).setMaxLineLength(2000).build();
            // Create connection configuration
            ConnectionConfig connectionConfig = ConnectionConfig.custom()
                    .setMalformedInputAction(CodingErrorAction.IGNORE)
                    .setUnmappableInputAction(CodingErrorAction.IGNORE)
                    .setCharset(Consts.UTF_8)
                    .setMessageConstraints(messageConstraints).build();
            connManager.setDefaultConnectionConfig(connectionConfig);
            connManager.setMaxTotal(200);
            connManager.setDefaultMaxPerRoute(20);
        } catch (KeyManagementException e) {
            logger.error("KeyManagementException", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException", e);
        }
    }


    /**
     * 通过post提交方式获取url指定的资源和数据 http/https
     *
     * @param url
     * @param params 请求参数
     * @param headers        请求header参数
     * @return
     * @throws RuntimeException
     */
    public static String postData(String url,Map<String, String> params, Map<String, String> headers)
            throws RuntimeException {
        long start = System.currentTimeMillis();
        String responseContent = null;
        HttpPost httpPost = new HttpPost(url);
        try {
            if (headers != null && headers.size() > 0) {
                Set<Entry<String, String>> set = headers.entrySet();
                for (Iterator<Entry<String, String>> it = set.iterator(); it
                        .hasNext(); ) {
                    Entry<String, String> header = it.next();
                    if (header != null) {
                        httpPost.setHeader(header.getKey(), header.getValue());
                    }
                }
            }
            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            for (Entry<String, String> entry : params.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry
                        .getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(formParams,
                    Consts.UTF_8));
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                try {
                    if (null != entity) {
                        responseContent = EntityUtils.toString(entity,
                                Consts.UTF_8);
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }

            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return responseContent;
        } catch (UnsupportedEncodingException e) {
            logger.warn("postData UnsupportedEncodingException url: {}" + url, e);
            throw new RuntimeException(url + "postData UnsupportedEncodingException：", e);

        } catch (ClientProtocolException e) {
            logger.warn("postData ClientProtocolException url: {}" + url, e);
            throw new RuntimeException("postData ClientProtocolException url：" + url, e);

        } catch (IOException e) {
            logger.warn("postData IOException url: {}" + url, e);
            throw new RuntimeException("postData IOException url：" + url, e);
        } finally {
            httpPost.releaseConnection();
            long interval = System.currentTimeMillis() - start;
            logger.debug("{} 请求耗时：{} ", url, interval);
        }
    }

    /**
     * 通过post提交传输xml格式的http/https请求
     *
     * @param url
     * @param content
     * @return
     * @throws RuntimeException
     */
    public static String postXMlData(String url, String content) throws RuntimeException {
        return postDataByType(url, content, "application/xml");
    }

    /**
     * 通过post提交传输Json格式的http/https请求
     *
     * @param url
     * @param content
     * @return
     * @throws RuntimeException
     */
    public static String postJsonData(String url, String content) throws RuntimeException {
        return postDataByType(url, content, "application/json");
    }

    /**
     * 通过post提交传输格式为 contentType的http/https请求
     *
     * @param url         远程url
     * @param content     传输内容
     * @param contentType 传输格式  application/xml,application/json
     * @return
     * @throws RuntimeException
     */
    public static String postDataByType(String url, String content, String contentType)
            throws RuntimeException {
        String responseContent = null;
        long start = System.currentTimeMillis();
        HttpPost httpPost = new HttpPost(url);
        try {
            if (content != null && content.length() > 0) {
                httpPost.setEntity(new StringEntity(content,
                        ContentType.create(contentType, Consts.UTF_8)));
            }
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(connectTimeout)
                    .setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(connectTimeout).build();
            httpPost.setConfig(requestConfig);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                try {
                    if (null != entity) {
                        responseContent = EntityUtils.toString(entity,
                                Consts.UTF_8);
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }

            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return responseContent;
        } catch (ClientProtocolException e) {
            logger.error("postDataByJson ClientProtocolException url:" + url, e);
            throw new RuntimeException(url + "postJDataByType exception：", e);
        } catch (IOException e) {
            logger.error("postDataByJson IOException url:" + url, e);
            throw new RuntimeException("postDataByType IOException url：" + url, e);
        } finally {
            httpPost.releaseConnection();
            long interval = System.currentTimeMillis() - start;
            logger.debug("{} 请求耗时：{} ", url, interval);
        }
    }

    /**
     * 通过post提交方法传输格式为json的 Map数据  http/https
     *
     * @param url
     * @param timeout
     * @param map
     * @return
     * @throws RuntimeException
     */
    public static String postJsonData(String url, int timeout, Map<String, Object> map) throws RuntimeException {
        long start = System.currentTimeMillis();
        HttpPost post = new HttpPost(url);
        String responseStr = null;
        try {
            post.setHeader("Content-type", "application/json");
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(timeout).setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout)
                    .setExpectContinueEnabled(false).build();
            post.setConfig(requestConfig);

            String str1 = JSONObject.toJSONString(map).replace("\\", "");
            post.setEntity(new StringEntity(str1, "UTF-8"));
            logger.info("[postJSONData Post] begin invoke url:" + url
                    + " , params:" + str1);
            CloseableHttpResponse response = httpclient.execute(post);
            try {
                HttpEntity entity = response.getEntity();
                try {
                    if (entity != null) {
                        responseStr = EntityUtils.toString(entity, "UTF-8");
                        logger.info("[postJSONData Post]Debug response, url :"
                                + url + " , response string :" + responseStr);
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return responseStr;
        } catch (UnsupportedEncodingException e) {
            logger.error("postJSONData UnsupportedEncodingException url:" + url, e);
            throw new RuntimeException(url + "postJSONData UnsupportedEncodingException：", e);
        } catch (ClientProtocolException e) {
            logger.error("postJSONData ClientProtocolException url:" + url, e);
            throw new RuntimeException(url + "postJSONData ClientProtocolException：", e);
        } catch (IOException e) {
            logger.error("postJSONData IOException url:" + url, e);
            throw new RuntimeException(url + "postJSONData IOException：", e);
        } finally {
            post.releaseConnection();
            long interval = System.currentTimeMillis() - start;
            logger.info("{} 请求耗时：{} ", url, interval);
        }
    }

    /**
     * 通过get请求方式获取资源 http/https
     *
     * @param url
     * @param params
     * @return
     */
    public static String getData(String url, Map<String, String> params,
                                 int timeOut) throws RuntimeException {
        long start = System.currentTimeMillis();
        String responseString = null;
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(timeOut)
                .setConnectTimeout(timeOut)
                .setConnectionRequestTimeout(timeOut).build();

        StringBuilder sb = new StringBuilder();
        sb.append(url);
        int i = 0;
        if (params != null && params.size() > 0) {
            for (Entry<String, String> entry : params.entrySet()) {
                if (i == 0 && !url.contains("?")) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(entry.getKey());
                sb.append("=");
                String value = entry.getValue();
                try {
                    sb.append(URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.warn("encode http get params error, value is " + value,
                            e);
                    sb.append(URLEncoder.encode(value));
                }
                i++;
            }
        }
        logger.info("[HttpUtil getData] begin invoke:" + sb.toString());
        HttpGet get = new HttpGet(sb.toString());
        get.setConfig(requestConfig);

        try {
            CloseableHttpResponse response = httpclient.execute(get);
            try {
                HttpEntity entity = response.getEntity();
                logger.info("invoke return info [{}]", JSON.toJSONString(entity));
                try {
                    if (entity != null) {
                        responseString = EntityUtils.toString(entity, "UTF-8");
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } catch (ParseException e) {
                logger.error(String.format(
                        "[HttpUtil getData]get response error, url:%s",
                        sb.toString()), e);
                throw new RuntimeException("[HttpUtil getData] ParseException ,url:%s：" + sb.toString(), e);
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            logger.info(String.format(
                    "[HttpUtil getData]Debug url:%s , response string %s:",
                    sb.toString(), responseString));
            return responseString;
        } catch (ClientProtocolException e) {
            logger.error("ClientProtocolException url:%s:" + sb.toString(), e);
            throw new RuntimeException("[HttpUtil getData]ClientProtocolException ,url:%s：" + sb.toString(), e);
        } catch (IOException e) {
            logger.error(
                    String.format("[HttpUtils Get]invoke get error, url:%s",
                            sb.toString()), e);
            throw new RuntimeException("[HttpUtil getData] IOException ,url:%s：" + sb.toString(), e);
        } finally {
            get.releaseConnection();
            long interval = System.currentTimeMillis() - start;
            logger.debug("{} 请求耗时：{} ", url, interval);
        }
    }

    /**
     * 带header的get请求
     *
     * @param url     服务器地址
     * @param headers 添加的请求header信息
     * @return 返回服务器响应的文本，出错抛出RuntimeException异常
     * @throws RuntimeException
     */
    public static String getData(String url, Map<String, String> params, Map<String, String> headers)
            throws RuntimeException {
        long start = System.currentTimeMillis();
        String responseString = null;
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        int i = 0;
        if (params != null && params.size() > 0) {
            for (Entry<String, String> entry : params.entrySet()) {
                if (i == 0 && !url.contains("?")) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(entry.getKey());
                sb.append("=");
                String value = entry.getValue();
                try {
                    sb.append(URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.warn("encode http get params error, value is " + value,
                            e);
                    sb.append(URLEncoder.encode(value));
                }
                i++;
            }
        }
        logger.info("[HttpUtil getData] begin invoke:" + sb.toString());
        HttpGet httpGet = new HttpGet(sb.toString());
        if (headers != null && headers.size() > 0) {
            Set<Entry<String, String>> set = headers.entrySet();
            for (Iterator<Entry<String, String>> it = set.iterator(); it
                    .hasNext(); ) {
                Entry<String, String> header = it.next();
                if (header != null) {
                    httpGet.setHeader(header.getKey(), header.getValue());
                }
            }
        }

        try {
            CloseableHttpResponse response = httpclient.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                try {
                    if (entity != null) {
                        responseString = EntityUtils.toString(entity, "UTF-8");
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return responseString;
        } catch (ClientProtocolException e) {
            logger.error("getData ClientProtocolException url: {}" + url, e);
            throw new RuntimeException(url + "getData ClientProtocolException：", e);

        } catch (IOException e) {
            logger.error("getData IOException url: {}" + url, e);
            throw new RuntimeException(url + "getData IOException：", e);
        } finally {
            httpGet.releaseConnection();
            long interval = System.currentTimeMillis() - start;
            logger.debug("{} 请求耗时：{} ", url, interval);
        }
    }


    public static String getDataByAuth(String url, Map<String, String> params,
                                       int timeOut, String username, String password) throws RuntimeException {
        long start = System.currentTimeMillis();
        String responseString = null;
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(timeOut)
                .setConnectTimeout(timeOut)
                .setConnectionRequestTimeout(timeOut).build();

        StringBuilder sb = new StringBuilder();
        sb.append(url);
        int i = 0;
        if (params != null && params.size() > 0) {
            for (Entry<String, String> entry : params.entrySet()) {
                if (i == 0 && !url.contains("?")) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(entry.getKey());
                sb.append("=");
                String value = entry.getValue();
                try {
                    sb.append(URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.warn("encode http get params error, value is " + value,
                            e);
                    sb.append(URLEncoder.encode(value));
                }
                i++;
            }
        }
        logger.info("[HttpUtil getData] begin invoke:" + sb.toString());
        HttpGet get = new HttpGet(sb.toString());
        get.setConfig(requestConfig);

//        HttpHost targetHost = new HttpHost(hostname, port, "http");
        //基础凭证提供器,明文传输数据
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(get.getURI().getHost(), get.getURI().getPort()),
                new UsernamePasswordCredentials(username, password));
        // 将认证缓存添加到执行环境中  即预填充
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);

        try {
            CloseableHttpResponse response = httpclient.execute(get, context);
            try {
                HttpEntity entity = response.getEntity();
                try {
                    if (entity != null) {
                        responseString = EntityUtils.toString(entity, "UTF-8");
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } catch (ParseException e) {
                logger.error(String.format(
                        "[HttpUtil getData]get response error, url:%s",
                        sb.toString()), e);
                throw new RuntimeException("[HttpUtil getData] ParseException ,url:%s：" + sb.toString(), e);
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            logger.info(String.format(
                    "[HttpUtil getData]Debug url:%s , response string %s:",
                    sb.toString(), responseString));
            return responseString;
        } catch (ClientProtocolException e) {
            logger.error("ClientProtocolException url:%s:" + sb.toString(), e);
            throw new RuntimeException("[HttpUtil getData]ClientProtocolException ,url:%s：" + sb.toString(), e);
        } catch (IOException e) {
            logger.error(
                    String.format("[HttpUtils Get]invoke get error, url:%s",
                            sb.toString()), e);
            throw new RuntimeException("[HttpUtil getData] IOException ,url:%s：" + sb.toString(), e);
        } finally {
            get.releaseConnection();
            long interval = System.currentTimeMillis() - start;
            logger.debug("{} 请求耗时：{} ", url, interval);
        }
    }

    /**
     * 通过post提交map类型参数 http/https
     *
     * @param reqURL 请求url
     * @param params map请求参数
     * @return
     */
    public static String postHttpData(String reqURL, Map<String, String> params) throws RuntimeException {
        long start = System.currentTimeMillis();
        String responseContent = null;
        HttpPost httpPost = new HttpPost(reqURL);
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(connectTimeout)
                    .setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(connectTimeout).build();

            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            if (params != null && params.size() > 0) {
                for (Entry<String, String> entry : params.entrySet()) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry
                            .getValue()));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(formParams,
                    Consts.UTF_8));
            httpPost.setConfig(requestConfig);
            // 绑定到请求 Entry
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                // 执行POST请求
                HttpEntity entity = response.getEntity(); // 获取响应实体
                try {
                    if (null != entity) {
                        responseContent = EntityUtils.toString(entity,
                                Consts.UTF_8);
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            logger.info("requestURI : " + httpPost.getURI()
                    + ", responseContent: " + responseContent);
            return responseContent;
        } catch (ClientProtocolException e) {
            logger.warn("ClientProtocolException url:" + reqURL, e);
            throw new RuntimeException("postDataByType ClientProtocolException url：" + reqURL, e);
        } catch (IOException e) {
            logger.warn("IOException url:" + reqURL, e);
            throw new RuntimeException("postDataByType IOException url：" + reqURL, e);
        } finally {
            httpPost.releaseConnection();
            long interval = System.currentTimeMillis() - start;
            logger.debug("{} 请求耗时：{} ", reqURL, interval);
        }

    }

    /**
     * 通过post提交方法传输格式文件的 Map数据  http/https
     *
     * @param url
     * @param timeout
     * @param map
     * @return
     * @throws RuntimeException
     */
    public static String postFileData(String url, Map<String, String> map, InputStream fileContent, int timeout) throws RuntimeException {
        long start = System.currentTimeMillis();
        HttpPost post = new HttpPost(url);
        String responseStr = null;
        String responseSign = "";
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(timeout).setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout)
                    .setExpectContinueEnabled(false).build();
            post.setConfig(requestConfig);

            MultipartEntityBuilder builder = MultipartEntityBuilder
                    .create();

            java.io.File file = java.io.File.createTempFile("uploadAuditingFiles", "uploadAuditingFiles");
            FileUtils.copyInputStreamToFile(fileContent, file);
            for (Entry<String, String> entry : map.entrySet()) {
                builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.TEXT_PLAIN);
            }
            builder.addBinaryBody("idcard", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
            HttpEntity build = builder.build();
            post.setEntity(build);
            logger.info("[postJSONData Post] begin invoke url:" + url + ",params:" + JSONObject.toJSONString(map).replace("\\", ""));
            CloseableHttpResponse response = httpclient.execute(post);
            try {
                HttpEntity entity = response.getEntity();
                logger.info(":::entity:::" + entity);
                try {
                    if (entity != null) {
                        responseStr = EntityUtils.toString(entity, "UTF-8");
                        if (response.getFirstHeader("sign") != null) {
                            responseSign = response.getFirstHeader("sign").getValue();
                        }
                        logger.info("[postJSONData Post]Debug response, url :"
                                + url + " , response string :" + responseStr + "responseSign" + responseSign);
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return responseStr;
        } catch (UnsupportedEncodingException e) {
            logger.error("postJSONData UnsupportedEncodingException url:" + url, e);
            throw new RuntimeException(url + "postJSONData UnsupportedEncodingException：", e);
        } catch (ClientProtocolException e) {
            logger.error("postJSONData ClientProtocolException url:" + url, e);
            throw new RuntimeException(url + "postJSONData ClientProtocolException：", e);
        } catch (IOException e) {
            logger.error("postJSONData IOException url:" + url, e);
            throw new RuntimeException(url + "postJSONData IOException：", e);
        } finally {
            post.releaseConnection();
            long interval = System.currentTimeMillis() - start;
            logger.debug("{} 请求耗时：{} ", url, interval);
        }
    }

    /**
     * 通过post提交map类型参数 http/https
     *
     * @param reqURL 请求url
     * @param params map请求参数
     * @return
     */
    public static Map<String, String> postHttpDataForLanmao(String reqURL, Map<String, String> params) throws RuntimeException {
        long start = System.currentTimeMillis();
        Map<String, String> responseMap = new HashMap<String, String>();
        HttpPost httpPost = new HttpPost(reqURL);
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(connectTimeout)
                    .setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(connectTimeout).build();

            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            if (params != null && params.size() > 0) {
                for (Entry<String, String> entry : params.entrySet()) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry
                            .getValue()));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(formParams,
                    Consts.UTF_8));
            httpPost.setConfig(requestConfig);
            // 绑定到请求 Entry
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                // 执行POST请求
                HttpEntity entity = response.getEntity(); // 获取响应实体
                try {
                    if (null != entity) {
                        String responseData = EntityUtils.toString(entity,
                                Consts.UTF_8);
                        Header header = entity.getContentType();
                        String sign = header.toString();
                        responseMap.put("respData", responseData);
                        responseMap.put("sign", sign);
                        logger.info("requestURI : " + httpPost.getURI()
                                + ", responseData: " + responseData);
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } finally {
                if (response != null) {
                    response.close();
                }
            }

            return responseMap;
        } catch (ClientProtocolException e) {
            logger.warn("ClientProtocolException url:" + reqURL, e);
            throw new RuntimeException("postDataByType ClientProtocolException url：" + reqURL, e);
        } catch (IOException e) {
            logger.warn("IOException url:" + reqURL, e);
            throw new RuntimeException("postDataByType IOException url：" + reqURL, e);
        } finally {
            httpPost.releaseConnection();
            long interval = System.currentTimeMillis() - start;
            logger.debug("{} 请求耗时：{} ", reqURL, interval);
        }

    }

}

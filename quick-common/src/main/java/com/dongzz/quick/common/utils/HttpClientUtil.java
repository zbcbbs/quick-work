package com.dongzz.quick.common.utils;

import com.dongzz.quick.common.domain.ResponseVo;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;

/**
 * Http Client 远程接口调用工具
 */
@Component
public class HttpClientUtil {

    private static final String ENCODING = "UTF-8"; // 编码格式 请求编码格式统一用 UTF-8

    @Autowired
    private CloseableHttpClient httpClient; // Http Client 客户端
    @Autowired
    private RequestConfig requestConfig; // 请求配置

    private HttpClientConnectionManager connectionManager; // 连接池管理器

    /**
     * 构造器
     *
     * @param connectionManager 连接池管理器
     */
    public HttpClientUtil(@Qualifier("poolingHttpClientManager") HttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        // 开启独立线程 定时清理 Http Client 过期连接和无效连接
//        ThreadPoolExecutorUtil.getPoll().execute(new HttpClientCleanBean(connectionManager));
    }

    /**
     * GET请求 无参
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public ResponseVo doGet(String url) throws Exception {
        return doGet(url, null, null);
    }

    /**
     * GET请求 带参数
     *
     * @param url    请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public ResponseVo doGet(String url, Map<String, String> params) throws Exception {
        return doGet(url, null, params);
    }

    /**
     * GET请求 带参数和请求头
     *
     * @param url     请求地址
     * @param headers 请求头
     * @param params  参数集合
     * @return
     * @throws Exception
     */
    public ResponseVo doGet(String url, Map<String, String> headers, Map<String, String> params)
            throws Exception {
        // 创建访问的地址
        URIBuilder uriBuilder = new URIBuilder(url);
        // 设置参数
        if (params != null) {
            Set<Entry<String, String>> entrySet = params.entrySet();
            for (Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }

        // 创建 Http对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        // 请求配置
        httpGet.setConfig(requestConfig);

        // 设置请求头
        packageHeader(headers, httpGet);

        // 执行请求并获得响应结果
        return getHttpClientResult(httpClient, httpGet);
    }

    /**
     * POST请求 不带请求头和参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public ResponseVo doPost(String url) throws Exception {
        return doPost(url, null, null);
    }

    /**
     * POST请求 带参数
     *
     * @param url    请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public ResponseVo doPost(String url, Map<String, String> params) throws Exception {
        return doPost(url, null, params);
    }

    /**
     * POST请求 带请求头和参数
     *
     * @param url     请求地址
     * @param headers 请求头集合
     * @param params  参数集合
     * @return
     * @throws Exception
     */
    public ResponseVo doPost(String url, Map<String, String> headers, Map<String, String> params)
            throws Exception {
        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        /**
         * setConnectTimeout：设置连接超时时间，单位毫秒。
         * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection
         * 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
         * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
         */
        httpPost.setConfig(requestConfig);
        // 设置请求头
		/*httpPost.setHeader("Cookie", "");
		httpPost.setHeader("Connection", "keep-alive");
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
		httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
		httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");*/
        packageHeader(headers, httpPost);

        // 封装请求参数
        packageParam(params, httpPost);

        // 执行请求并获得响应结果
        return getHttpClientResult(httpClient, httpPost);
    }

    /**
     * PUT请求 不带参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public ResponseVo doPut(String url) throws Exception {
        return doPut(url, null);
    }

    /**
     * PUT请求 带参数
     *
     * @param url    请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public ResponseVo doPut(String url, Map<String, String> params) throws Exception {
        HttpPut httpPut = new HttpPut(url);
        httpPut.setConfig(requestConfig);

        packageParam(params, httpPut);

        return getHttpClientResult(httpClient, httpPut);
    }

    /**
     * DELETE请求 不带参数
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public ResponseVo doDelete(String url) throws Exception {
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setConfig(requestConfig);

        return getHttpClientResult(httpClient, httpDelete);
    }

    /**
     * DELETE请求 带请求参数
     *
     * @param url    请求地址
     * @param params 参数集合
     * @return
     * @throws Exception
     */
    public ResponseVo doDelete(String url, Map<String, String> params) throws Exception {
        if (params == null) {
            params = new HashMap<String, String>();
        }

        params.put("_method", "delete");
        return doPost(url, params);
    }

    /**
     * 封装请求头
     *
     * @param params     参数集合
     * @param httpMethod 请求对象
     */
    public void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        // 封装请求头
        if (params != null) {
            Set<Entry<String, String>> entrySet = params.entrySet();
            for (Entry<String, String> entry : entrySet) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 封装 请求参数
     *
     * @param params     参数集合
     * @param httpMethod 请求对象
     * @throws UnsupportedEncodingException
     */
    public void packageParam(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod)
            throws UnsupportedEncodingException {
        // 封装请求参数
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<Entry<String, String>> entrySet = params.entrySet();
            for (Entry<String, String> entry : entrySet) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            // 设置到请求的http对象中
            httpMethod.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
        }
    }

    /**
     * 处理 响应结果
     *
     * @param httpClient 客户端连接
     * @param httpMethod 请求对象
     * @return
     * @throws Exception
     */
    public ResponseVo getHttpClientResult(CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws Exception {
        // 执行请求
        HttpResponse httpResponse = httpClient.execute(httpMethod);

        // 获取返回结果
        if (httpResponse != null && httpResponse.getStatusLine() != null) {
            String content = "";
            if (httpResponse.getEntity() != null) {
                content = EntityUtils.toString(httpResponse.getEntity(), ENCODING);
            }
            return new ResponseVo(httpResponse.getStatusLine().getStatusCode(), "", content);
        }
        return new ResponseVo(HttpStatus.SC_INTERNAL_SERVER_ERROR, "", "");
    }

}

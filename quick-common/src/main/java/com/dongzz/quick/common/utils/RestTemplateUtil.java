package com.dongzz.quick.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * RestTemplate 远程接口调用 工具类
 */
@Component
public class RestTemplateUtil {

    @Autowired
    private RestTemplate restTemplate; // RestTemplate 客户端


    // ---------------------------------- GET --------------------------------------

    /**
     * GET请求
     *
     * @param url          请求URL
     * @param responseType 响应类型
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> get(String url, Class<T> responseType) throws Exception {
        return restTemplate.getForEntity(url, responseType);
    }

    /**
     * GET请求
     *
     * @param url          请求URL
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> get(String url, Class<T> responseType, Object... uriVariables) throws Exception {
        return restTemplate.getForEntity(url, responseType, uriVariables);
    }

    /**
     * GET请求
     *
     * @param url          请求URL
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> get(String url, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        return restTemplate.getForEntity(url, responseType, uriVariables);
    }

    /**
     * GET请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> get(String url, Map<String, String> headers, Class<T> responseType, Object... uriVariables) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return get(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * GET请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables) throws Exception {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(url, HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    /**
     * GET请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> get(String url, Map<String, String> headers, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return get(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * GET请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(url, HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    // ---------------------------------- POST -----------------------------------

    /**
     * POST请求
     *
     * @param url          请求URL
     * @param responseType 响应类型
     * @return
     */
    public <T> ResponseEntity<T> post(String url, Class<T> responseType) throws Exception {
        return restTemplate.postForEntity(url, HttpEntity.EMPTY, responseType);
    }

    /**
     * POST请求
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType) throws Exception {
        return restTemplate.postForEntity(url, requestBody, responseType);
    }

    /**
     * POST请求
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType, Object... uriVariables) throws Exception {
        return restTemplate.postForEntity(url, requestBody, responseType, uriVariables);
    }

    /**
     * POST请求
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        return restTemplate.postForEntity(url, requestBody, responseType, uriVariables);
    }

    /**
     * POST请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> post(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Object... uriVariables) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return post(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * POST请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> post(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Object... uriVariables) throws Exception {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return post(url, requestEntity, responseType, uriVariables);
    }

    /**
     * POST请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> post(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return post(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * POST请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> post(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return post(url, requestEntity, responseType, uriVariables);
    }

    /**
     * POST请求 自定义请求头和请求体
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装
     * @param responseType  响应类型
     * @param uriVariables  URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> post(String url, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws Exception {
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType, uriVariables);
    }

    /**
     * POST请求 自定义请求头和请求体
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装
     * @param responseType  响应类型
     * @param uriVariables  URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> post(String url, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType, uriVariables);
    }

    // ---------------------------------- PUT -----------------------------------------

    /**
     * PUT请求
     *
     * @param url          请求URL
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> put(String url, Class<T> responseType, Object... uriVariables) throws Exception {
        return put(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * PUT请求
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> put(String url, Object requestBody, Class<T> responseType, Object... uriVariables) throws Exception {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * PUT请求
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> put(String url, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * PUT请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> put(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Object... uriVariables) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return put(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * PUT请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> put(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Object... uriVariables) throws Exception {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * PUT请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> put(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return put(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * PUT请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> put(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * PUT请求 自定义请求头和请求体
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  响应类型
     * @param uriVariables  URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> put(String url, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws Exception {
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType, uriVariables);
    }

    /**
     * PUT请求 自定义请求头和请求体
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  响应类型
     * @param uriVariables  URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> put(String url, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType, uriVariables);
    }

    // ---------------------------------- DELETE ----------------------------------------

    /**
     * DELETE请求
     *
     * @param url          请求URL
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> delete(String url, Class<T> responseType, Object... uriVariables) throws Exception {
        return delete(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * DELETE请求
     *
     * @param url          请求URL
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> delete(String url, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        return delete(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * DELETE请求
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> delete(String url, Object requestBody, Class<T> responseType, Object... uriVariables) throws Exception {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * DELETE请求
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> delete(String url, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * DELETE请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> delete(String url, Map<String, String> headers, Class<T> responseType, Object... uriVariables) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * DELETE请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables) throws Exception {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * DELETE请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> delete(String url, Map<String, String> headers, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * DELETE请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * DELETE请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> delete(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Object... uriVariables) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * DELETE请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Object... uriVariables) throws Exception {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * DELETE请求
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> delete(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * DELETE请求 带请求头
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 响应类型
     * @param uriVariables URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * DELETE请求 自定义请求头和请求体
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装
     * @param responseType  响应类型
     * @param uriVariables  URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> delete(String url, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws Exception {
        return restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, responseType, uriVariables);
    }

    /**
     * DELETE请求 自定义请求头和请求体
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> delete(String url, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        return restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, responseType, uriVariables);
    }

    // ---------------------------------- 通用方法 ------------------------------------------

    /**
     * 通用调用方式
     *
     * @param url           请求URL
     * @param method        请求方法类型
     * @param requestEntity 请求头和请求体封装
     * @param responseType  响应类型
     * @param uriVariables  URL中的变量 按顺序依次对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws Exception {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
    }

    /**
     * 通用调用方式
     *
     * @param url           请求URL
     * @param method        请求方法类型
     * @param requestEntity 请求头和请求体封装
     * @param responseType  响应类型
     * @param uriVariables  URL中的变量 与Map中的key对应
     * @return ResponseEntity 响应对象
     */
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
    }

}

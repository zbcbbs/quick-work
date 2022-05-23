package com.dongzz.quick.common.base;

import com.dongzz.quick.common.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 基础控制器
 */
public class BaseController {

    // 线程安全的
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;

    /**
     * 请求参数绑定格式编辑器，进行数据格式转化
     *
     * @param binder 绑定器
     * @throws Exception
     */
    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder) throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //日期格式编辑器 日期字符串 -> Date
        CustomDateEditor dateEditor = new CustomDateEditor(format, true);
        binder.registerCustomEditor(Date.class, dateEditor);

    }

    /**
     * 获取请求参数
     *
     * @param name 参数名称
     * @return
     */
    protected String getParameter(String name) {
        Map<String, Object> map = WebUtil.getParamsMap(request);
        String paramString = (map.get(name) == null) ? "" : map.get(name).toString();
        return paramString;
    }

    /**
     * 将json响应到客户端
     *
     * @param jsonString json内容
     */
    protected void flushOutJson(String jsonString) {
        WebUtil.flushOutJson(response, jsonString);
    }

    /**
     * 将文本响应到客户端
     *
     * @param textString 文本内容
     */
    protected void flushOutText(String textString) {
        WebUtil.flushOutText(response, textString);
    }

}

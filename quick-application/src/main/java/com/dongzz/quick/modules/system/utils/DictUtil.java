package com.dongzz.quick.modules.system.utils;

import com.dongzz.quick.common.utils.Constants;
import com.dongzz.quick.common.utils.WebUtil;
import com.dongzz.quick.modules.system.domain.SysDict;
import com.dongzz.quick.modules.system.domain.SysDictItem;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典 工具类
 */
public class DictUtil {

    /**
     * 获取字典项编码
     *
     * @param dic （字典编码:字典项编码）格式：（'logsType:1'）
     * @return
     */
    public static String dic(String dic) {
        SysDictItem dictItem = listDictItem(dic);
        return dictItem.getItemCode();
    }

    /**
     * 根据字典编码和字典项编码 获取 字典项
     *
     * @param dic （字典编码:字典项编码）格式：（'logsType:1'）
     * @return
     */
    public static SysDictItem listDictItem(String dic) {
        Map<String, SysDictItem> map = mapDictItems();
        return map.get(dic);
    }

    /**
     * 根据字典编码 获取 字典
     *
     * @param dc
     * @return
     */
    public static SysDict listDict(String dc) {
        List<SysDict> list = getServletContextDict();
        SysDict dt = null;
        for (SysDict dict : list) {
            if (dict.getCode().equals(dc)) {
                dt = dict;
                break;
            }
        }
        return dt;
    }

    /**
     * 根据字典编码 获取 字典项集合
     *
     * @param dc
     * @return
     */
    public static List<SysDictItem> listDictItems(String dc) {
        Map<String, List<SysDictItem>> map = mapDicts();
        return map.get(dc);
    }

    /**
     * 获取字典集合
     *
     * @return
     */
    public static List<SysDict> listDicts() {
        return getServletContextDict();
    }

    /**
     * 字典存储到Map k:字典编码  v:字典项集合
     *
     * @return
     */
    public static Map<String, List<SysDictItem>> mapDicts() {
        Map<String, List<SysDictItem>> map = new HashMap<String, List<SysDictItem>>();
        List<SysDict> list = getServletContextDict();
        for (SysDict dict : list) {
            map.put(dict.getCode(), dict.getItems());
        }
        return map;
    }

    /**
     * 字典项存储到Map k:（'字典编码:字典项编码'）  v:字典项
     *
     * @return
     */
    public static Map<String, SysDictItem> mapDictItems() {
        Map<String, SysDictItem> map = new HashMap<String, SysDictItem>();
        List<SysDict> list = getServletContextDict();
        for (SysDict dict : list) {
            List<SysDictItem> items = dict.getItems();
            for (SysDictItem item : items) {
                map.put(dict.getCode() + ":" + item.getItemCode(), item);
            }
        }
        return map;
    }

    /**
     * servlet 上下文 取出字典
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<SysDict> getServletContextDict() {
        HttpServletRequest request = WebUtil.getHttpRequest();
        ServletContext servletContext = request.getSession().getServletContext();
        List<SysDict> dicts = (List<SysDict>) servletContext.getAttribute(Constants.DICT_CONTEXT_KEY);
        return dicts;
    }
}

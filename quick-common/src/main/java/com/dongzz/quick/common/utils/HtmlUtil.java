package com.dongzz.quick.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Html 操作 工具类
 */
public class HtmlUtil {

    public final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签

    public final static String regxpForImgTag = "<\\s*img\\s+([^>]*)\\s*>"; // 找出IMG标签

    public final static String regxpForImaTagSrcAttrib = "src=\"([^\"]+)\""; // 找出IMG标签的SRC属性

    /**
     * 基本功能：替换标记以正常显示
     *
     * @param input
     * @return String
     */
    public static String replaceTag(String input) {
        if (!hasSpecialChars(input)) {
            return input;
        }
        StringBuffer filtered = new StringBuffer(input.length());
        char c;
        for (int i = 0; i <= input.length() - 1; i++) {
            c = input.charAt(i);
            switch (c) {
                case '<':
                    filtered.append("&lt;");
                    break;
                case '>':
                    filtered.append("&gt;");
                    break;
                case '"':
                    filtered.append("&quot;");
                    break;
                case '&':
                    filtered.append("&amp;");
                    break;
                default:
                    filtered.append(c);
            }

        }
        return (filtered.toString());
    }

    /**
     * 基本功能：判断标记是否存在
     *
     * @param input
     * @return boolean
     */
    public static boolean hasSpecialChars(String input) {
        boolean flag = false;
        if ((input != null) && (input.length() > 0)) {
            char c;
            for (int i = 0; i <= input.length() - 1; i++) {
                c = input.charAt(i);
                switch (c) {
                    case '>':
                        flag = true;
                        break;
                    case '<':
                        flag = true;
                        break;
                    case '"':
                        flag = true;
                        break;
                    case '&':
                        flag = true;
                        break;
                }
            }
        }
        return flag;
    }

    /**
     * 基本功能：过滤所有以"<"开头以">"结尾的标签
     *
     * @param str
     * @return String
     */
    public static String filterHtml(String str) {
        Pattern pattern = Pattern.compile(regxpForHtml);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 基本功能：过滤指定标签
     *
     * @param str
     * @param tag 指定标签
     * @return String
     */
    public static String filterHtmlTag(String str, String tag) {
        String regxp = "<\\s*" + tag + "\\s+([^>]*)\\s*>";
        Pattern pattern = Pattern.compile(regxp);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            //获取当前匹配的字符串
            String m = matcher.group();
            System.out.println("匹配：" + m);
            //判断是否自己需要的字符串  进行业务处理
            //TODO 此处写业务代码
            if (!m.contains("style")) {
                matcher.appendReplacement(sb, ""); //追加 替换
            }

            System.out.println("匹配追加：" + sb.toString());
            result1 = matcher.find();
        }
        matcher.appendTail(sb); //最后一次匹配之后的字符串追加到sb
        return sb.toString();
    }

    //测试方法
    public static void main(String[] args) {
        String htmlString = "aaa<input style=\"width: 10%\" type=\"text\" onclick=\"controlType()\" placeholder=\"无则不填\">sss<div><span class=\"rectangle\" flag=\"true\"><span class=\"wind\"><span>名称:<input value=\"1\"></span></span><span>跨越角度:<input value=\"2\"></span><span>跨越方式:<input value=\"3\"></span><span>视频在线监测:<input value=\"4\"></span></span></div>";

        String filterHtmlTag = filterHtmlTag(htmlString, "input");
        System.out.println(filterHtmlTag);

    }

    /**
     * 基本功能：替换指定的标签
     *
     * @param str
     * @param beforeTag 要替换的标签
     * @param tagAttrib 要替换的标签属性值
     * @param startTag  新标签开始标记
     * @param endTag    新标签结束标记
     * @return String
     */
    public static String replaceHtmlTag(String str, String beforeTag, String tagAttrib, String startTag, String endTag) {
        String regxpForTag = "<\\s*" + beforeTag + "\\s+([^>]*)\\s*>";
        String regxpForTagAttrib = tagAttrib + "=\"([^\"]+)\"";
        Pattern patternForTag = Pattern.compile(regxpForTag);
        Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib);
        Matcher matcherForTag = patternForTag.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result = matcherForTag.find();
        while (result) {
            StringBuffer sbreplace = new StringBuffer();
            Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag.group(1));
            if (matcherForAttrib.find()) {
                matcherForAttrib.appendReplacement(sbreplace, startTag + matcherForAttrib.group(1) + endTag);
            }
            matcherForTag.appendReplacement(sb, sbreplace.toString());
            result = matcherForTag.find();
        }
        matcherForTag.appendTail(sb);
        return sb.toString();
    }

}

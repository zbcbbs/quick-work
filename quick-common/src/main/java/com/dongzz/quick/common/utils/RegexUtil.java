package com.dongzz.quick.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则验证 工具类
 */
public class RegexUtil {

    /**
     * 验证 邮箱
     *
     * @param email 邮箱地址 格式：zbccc@163.com，zbccc@xxx.com.cn，xxx代表邮件服务商
     * @return
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }

    /**
     * 验证 身份证号码
     *
     * @param idCard 居民身份证号码 15位或18位，最后一位可能是数字或字母
     * @return
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }

    /**
     * 验证 手机号码
     * （支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     * 移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）
     * 联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）
     * 电信的号段：133、153、180（未启用）、189
     *
     * @param mobile 移动、联通、电信运营商的号码
     * @return
     */
    public static boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[34578]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 验证 固定电话号码
     * 国家（地区）代码 ：标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，数字之后是空格分隔的国家（地区）代码
     * 区号（城市代码）：这可能包含一个或多个从 0 到 9 的数字
     * 对不使用地区或城市代码的国家（地区），则省略该组件
     * 电话号码：这包含从 0 到 9 的一个或多个数字
     *
     * @param phone 电话号码 格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8601085588447
     * @return
     */
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    /**
     * 验证 整数（正整数和负整数）
     *
     * @param digit 一位或多位0-9之间的整数
     * @return
     */
    public static boolean checkDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex, digit);
    }

    /**
     * 验证 整数和浮点数（正负整数和正负浮点数）
     *
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return
     */
    public static boolean checkDecimals(String decimals) {
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?";
        return Pattern.matches(regex, decimals);
    }

    /**
     * 验证 空白字符
     *
     * @param blankSpace 空白字符 包括：空格、\t、\n、\r、\f、\x0B
     * @return
     */
    public static boolean checkBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return Pattern.matches(regex, blankSpace);
    }

    /**
     * 验证 中文
     *
     * @param chinese 中文字符
     * @return
     */
    public static boolean checkChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex, chinese);
    }

    /**
     * 验证 日期（年月日）
     *
     * @param birthday 日期 格式：1992-09-03，或1992.09.03
     * @return
     */
    public static boolean checkBirthday(String birthday) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex, birthday);
    }

    /**
     * 验证 URL地址
     *
     * @param url 格式：http://zbcbbs.com:80/blogs/details/111? 或 http://www.zbcbbs.com:80
     * @return
     */
    public static boolean checkURL(String url) {
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        return Pattern.matches(regex, url);
    }

    /**
     * 获取 网址URL的一级域名
     *
     * @param url 网址URL
     * @return
     */
    public static String getDomain(String url) {
        Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        // 获取 完整域名
        // Pattern p=Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        matcher.find();
        return matcher.group();
    }

    /**
     * 验证 中国邮政编码
     *
     * @param postcode 邮政编码
     * @return
     */
    public static boolean checkPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }

    /**
     * 验证 IP地址
     * 简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小
     *
     * @param ipAddress IPv4 标准地址
     * @return
     */
    public static boolean checkIpAddress(String ipAddress) {
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
        return Pattern.matches(regex, ipAddress);
    }
}

package com.dongzz.quick.common.utils;

import cn.hutool.core.date.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期处理 工具类
 */
public final class DateUtil extends cn.hutool.core.date.DateUtil {

    private static Logger log = LoggerFactory.getLogger(DateUtil.class);
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";

    public static void main(String[] args) {
        DateTime dateTime = cn.hutool.core.date.DateUtil.offsetDay(new Date(), -7);
        System.out.println(dateTime.toString("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 获取日期格式 yyyy-MM-dd
     *
     * @return
     */
    public static String getDatePattern() {
        String defaultDatePattern;
        defaultDatePattern = DATE_PATTERN;
        return defaultDatePattern;
    }

    /**
     * 获取日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getDateTimePattern() {
        return DateUtil.getDatePattern() + " " + TIME_PATTERN;
    }

    /**
     * 返回特定格式的日期字符串
     *
     * @param aMask 格式
     * @param aDate 日期
     * @return
     */
    public static String getDate(String aMask, Date aDate) {
        SimpleDateFormat df;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * 获取日期字符串 yyyy-MM-dd
     *
     * @param aDate 日期
     * @return
     */
    public static String getDate(Date aDate) {
        return getDate(getDatePattern(), aDate);
    }

    /**
     * 获取当前日期,格式为yyyy-MM-dd
     *
     * @return
     */
    public static String getNowDate() {
        return getDate(getDatePattern(), new Date());
    }

    /**
     * 将字符串转化为Date
     *
     * @param aMask   日期格式
     * @param strDate 日期字符串
     * @return
     */
    public static Date convertStringToDate(String aMask, String strDate) {
        SimpleDateFormat df;
        Date date = null;
        df = new SimpleDateFormat(aMask);

        if (log.isDebugEnabled()) {
            log.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
        }

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            log.debug("日期转换错误: ", pe.getCause());
        }

        return date;
    }

    /**
     * 字符串转化为Date，自动匹配格式
     *
     * @param strDate 日期字符串
     * @return
     */
    public static Date convertStringToDate(String strDate) {
        Date ndate = null;
        if (strDate != null) {
            if (strDate.length() > 10) {
                ndate = convertStringToDate(getDateTimePattern(), strDate);
            } else {
                ndate = convertStringToDate(getDatePattern(), strDate);
            }
        }
        return ndate;
    }

    /**
     * 获取时间 HH:mm:ss
     *
     * @param theTime 日期
     * @return
     */
    public static String getTime(Date theTime) {
        return getDateTime(TIME_PATTERN, theTime);
    }

    /**
     * 获取日期和时间 yyyy-MM-dd HH:mm:ss
     *
     * @param aDate 日期
     * @return
     */
    public static String getDateTime(Date aDate) {
        return getDateTime(getDateTimePattern(), aDate);
    }

    /**
     * 获取当前日期字符串 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getNowDateTime() {
        return getDateTime(getDateTimePattern(), new Date());
    }

    /**
     * 获取当前日期的 Calendar
     *
     * @return
     * @throws Exception
     */
    public static Calendar getTodayCalendar() throws Exception {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(getDatePattern(), todayAsString));

        return cal;
    }

    /**
     * 获取特定格式的日期字符串
     *
     * @param aMask 日期格式
     * @param aDate 日期
     * @return
     */
    public static String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            log.debug("转换日期不能为空!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * 根据 Calendar 获取指定格式的日期字符串
     *
     * @param cale 日历
     * @param mark 格式
     * @return
     */
    public static String getCalendarString(Calendar cale, String mark) {
        if (cale != null) {
            SimpleDateFormat formatte = new SimpleDateFormat(mark);
            Date date = cale.getTime();
            return formatte.format(date);
        }
        return "";
    }

    /**
     * 比较两个日期（光比较日期，不包括时间）
     *
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return
     */
    public static int compareDate(Date date1, Date date2) {
        Date ndate1 = convertStringToDate(getDatePattern(), getDate(date1));
        Date ndate2 = convertStringToDate(getDatePattern(), getDate(date2));
        return ndate1.compareTo(ndate2);
    }

    /**
     * 比较两个字符串日期(包含时间)
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Integer compareStrDateTime(String date1, String date2) {
        try {
            Date ndate1 = convertStringToDate(getDateTimePattern(), date1);
            Date ndate2 = convertStringToDate(getDateTimePattern(), date2);
            return ndate1.compareTo(ndate2);
        } catch (Exception e) {
            compareStrDate(date1, date2);
        }
        return null;
    }

    /**
     * 比较两个日期字符串
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Integer compareStrDate(String date1, String date2) {
        try {
            Date ndate1 = convertStringToDate(getDatePattern(), date1);
            Date ndate2 = convertStringToDate(getDatePattern(), date2);
            return ndate1.compareTo(ndate2);
        } catch (Exception ne) {
            try {
                Date ndate1 = convertStringToDate("yyyyMMdd", date1);
                Date ndate2 = convertStringToDate("yyyyMMdd", date2);
                return ndate1.compareTo(ndate2);
            } catch (Exception nne) {
                log.debug("日期转换错误!", nne.getCause());
            }

        }
        return null;
    }

    /**
     * 获取一年的第几周
     *
     * @param date 日期
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int week_of_year = c.get(Calendar.WEEK_OF_YEAR);
        return week_of_year - 1;
    }

    /**
     * 得到某日期的某一周的星期几的日期
     *
     * @param curdate      指定的日期日期格式为yyyy-MM-dd
     * @param calendarType 0~6 星期日到星期一
     * @return
     */
    public static String getWeeksOfDays(String curdate, int calendarType) {
        int calendar = 0;
        switch (calendarType) {
            case 0:
                calendar = Calendar.SUNDAY;
                break;
            case 1:
                calendar = Calendar.MONDAY;
                break;
            case 2:
                calendar = Calendar.TUESDAY;
                break;
            case 3:
                calendar = Calendar.WEDNESDAY;
                break;
            case 4:
                calendar = Calendar.THURSDAY;
                break;
            case 5:
                calendar = Calendar.FRIDAY;
                break;
            case 6:
                calendar = Calendar.SATURDAY;
                break;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date strtodate = new Date();
        try {
            strtodate = formatter.parse(curdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(strtodate);
        c.set(Calendar.DAY_OF_WEEK, calendar);
        return formatter.format(c.getTime());
    }

    /**
     * 按照传入的日期格式对日期进行加减
     *
     * @param time    计算开始的日期
     * @param daynums 天数
     * @param format  yyyy-MM-dd
     * @return
     */
    public static String getNextDays(String time, int daynums, String format) {
        try {
            SimpleDateFormat ft = new SimpleDateFormat(format);
            Date bDate = ft.parse(time);
            Calendar d1 = new GregorianCalendar();
            d1.setTime(bDate);
            d1.add(Calendar.DATE, daynums);
            return ft.format(d1.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNextMonth(String time, int daynums) {
        try {
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
            Date bDate = ft.parse(time);
            Calendar d1 = new GregorianCalendar();
            d1.setTime(bDate);
            d1.add(Calendar.MONTH, daynums);
            return ft.format(d1.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNextDays(String time, int daynums) {
        try {
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
            Date bDate = ft.parse(time);
            Calendar d1 = new GregorianCalendar();
            d1.setTime(bDate);
            d1.add(Calendar.DATE, daynums);
            return ft.format(d1.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取上月最后一天的日期
     *
     * @return
     */
    public static String getPreviousMonthEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();

        lastDate.add(Calendar.MONTH, -1);//减一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得日期的月份的第一天的日期
     *
     * @param currDate 当前日期
     * @return
     */
    public static String getMonthStart(String currDate) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        Date date = convertStringToDate(currDate);
        lastDate.setTime(date);
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获得日期的月份的最后一天的日期
     *
     * @param currDate 当前日期
     * @return
     */
    public static String getMonthEnd(String currDate) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        Date date = convertStringToDate(currDate);
        lastDate.setTime(date);
        lastDate.add(Calendar.MONTH, 1);//下一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是当前月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取日期的年份字符串
     *
     * @param date 日期
     * @return
     */
    public static String getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    /**
     * 获取日期的月份字符串 01,02,03 ... 12
     *
     * @param date 日期
     * @return
     */
    public static String getMonthOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH) + 1;
        DecimalFormat df = new DecimalFormat("00");
        return df.format(month);
    }

    /**
     * 取得当前日期的季度数，返回1，2，3，4
     * 1：第一季度  2：第二季度 3：第三季度 4：第四季度
     *
     * @param date
     * @return
     */
    public static String getQuarter(Date date) {
        int season = 0;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return String.valueOf(season);
    }

    /**
     * 计算两个日期字符串之间的天数
     * 不够24小时的按1天算
     *
     * @param endDate   结束时间
     * @param beginDate 开始时间
     * @return
     */
    public static int getIntervalDays(String endDate, String beginDate) {
        Date date1 = DateUtil.convertStringToDate(beginDate);
        Date date2 = DateUtil.convertStringToDate(endDate);
        return getIntervalDays(date2, date1);
    }

    /**
     * 计算两个日期之间的天数
     * 不够24小时的按1天算
     *
     * @param endDate   结束日期
     * @param beginDate 开始日期
     * @return
     */
    public static int getIntervalDays(Date endDate, Date beginDate) {
        long time1 = endDate.getTime();
        long time2 = beginDate.getTime();
        long interval = time1 - time2;
        int days = 0;
        if (interval % (24 * 3600 * 1000) > 0) {
            days = Integer.parseInt(interval / (24 * 3600 * 1000) + 1 + "");
        } else {
            days = Integer.parseInt(interval / (24 * 3600 * 1000) + "");
        }
        return days;
    }

    /**
     * 计算两个日期字符串之间的小时数
     * 不够24小时的按1天算
     *
     * @param endDate   结束日期
     * @param beginDate 开始日期
     * @return
     */
    public static int getIntervalHours(String endDate, String beginDate) {
        Date date1 = DateUtil.convertStringToDate(beginDate);
        Date date2 = DateUtil.convertStringToDate(endDate);
        return getIntervalHours(date2, date1);
    }

    /**
     * 计算两个日期之间的小时数
     * 不够24小时的按1天算
     *
     * @param endDate   结束日期
     * @param beginDate 开始日期
     * @return
     */
    public static int getIntervalHours(Date endDate, Date beginDate) {
        long time2 = beginDate.getTime();
        long time1 = endDate.getTime();
        long interval = time1 - time2;
        int days = 0;
        if (interval % (3600 * 1000) > 0) {
            days = Integer.parseInt(interval / (3600 * 1000) + 1 + "");
        } else {
            days = Integer.parseInt(interval / (3600 * 1000) + "");
        }
        return days;
    }

    /**
     * 转换日期字符串为目的格式
     * 比如：
     * 输入2013年12月1日10时，通过调用方法
     * DateUtil.formatDateStr(s, "yyyy年MM月dd日hh时mm分ss秒", "yyyy-MM-dd hh:MM:ss")
     * 返回结果：2013-12-1 10:0:0
     *
     * @param dateStr    传入的日期字符串
     * @param sourceMask 原始日期格式
     * @param distMask   目标日期格式
     * @return
     */
    public static String formatDateStr(String dateStr, String sourceMask, String distMask) {
        String result = "";
        try {
            int year_index = sourceMask.indexOf("yyyy");
            int month_index = sourceMask.indexOf("MM");
            int day_index = sourceMask.indexOf("dd");
            int hour_index = sourceMask.indexOf("hh");
            int min_index = sourceMask.indexOf("mm");
            int second_index = sourceMask.indexOf("ss");
            int slen = sourceMask.length();

            String year_str = year_index == -1 ? null : sourceMask.substring(year_index + 4, month_index == -1 ? slen
                    : month_index);
            String month_str = month_index == -1 ? null : sourceMask.substring(month_index + 2, day_index == -1 ? slen
                    : day_index);
            String day_str = day_index == -1 ? null : sourceMask.substring(day_index + 2, hour_index == -1 ? slen
                    : hour_index);
            String hour_str = hour_index == -1 ? null : sourceMask.substring(hour_index + 2, min_index == -1 ? slen
                    : min_index);
            String min_str = min_index == -1 ? null : sourceMask.substring(min_index + 2, second_index == -1 ? slen
                    : second_index);
            String second_str = second_index == -1 ? null : sourceMask.substring(second_index + 2, slen);

            String year = year_str != null && dateStr.indexOf(year_str) > 0 ? dateStr.substring(0,
                    dateStr.indexOf(year_str)) : "0000";
            String month = month_str != null && dateStr.indexOf(month_str) > 0 ? dateStr.substring(
                    dateStr.indexOf(year_str) + year_str.length(), dateStr.indexOf(month_str)) : "0";
            String day = day_str != null && dateStr.indexOf(day_str) > 0 ? dateStr.substring(dateStr.indexOf(month_str)
                    + month_str.length(), dateStr.indexOf(day_str)) : "0";
            String hour = hour_str != null && dateStr.indexOf(hour_str) > 0 ? dateStr.substring(
                    dateStr.indexOf(day_str) + day_str.length(), dateStr.indexOf(hour_str)) : "0";
            String min = min_str != null && dateStr.indexOf(min_str) > 0 ? dateStr.substring(dateStr.indexOf(hour_str)
                    + hour_str.length(), dateStr.indexOf(min_str)) : "0";
            String second = second_str != null && dateStr.indexOf(second_str) > 0 ? dateStr.substring(
                    dateStr.indexOf(min_str) + min_str.length(), dateStr.indexOf(second_str)) : "0";

            distMask = distMask.replace("yyyy", "%s");

            distMask = distMask.replace("MM", "%s");
            distMask = distMask.replace("dd", "%s");
            distMask = distMask.replace("hh", "%s");
            distMask = distMask.replace("mm", "%s");
            distMask = distMask.replace("ss", "%s");
            result = String.format(distMask, year, month, day, hour, min, second);
        } catch (Exception e) {
            log.debug("字符串转换日期发生错误：" + e.getCause());
        }
        return result;
    }

    /**
     * 验证日期字符串是否正确
     *
     * @param dateStr 日期字符串
     * @return
     */
    public static Date verifyDate(String dateStr) {
        Date date = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            // 该异常是为了验证字符串是否是日期格式，所以不做任何处理
        }
        return date;
    }

    /**
     * 据传入的开始日期和结束日期，按照指定间隔interval拆分为n个开始日期和结束日期组成的Map
     * 返回一个拆分后的Map，key为数字顺序，value为一个String[]，第一个为开始日期，第二个为结束日期
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param interval  间隔天数
     * @return
     */
    public static Map<String, String[]> splitDate(String startDate, String endDate, int interval) {
        Calendar calendar_start = Calendar.getInstance();
        calendar_start.setTime(DateUtil.convertStringToDate(startDate));
        Calendar calendar_end = Calendar.getInstance();
        calendar_end.setTime(DateUtil.convertStringToDate(endDate));
        Map<String, String[]> result = new LinkedHashMap<String, String[]>();
        int number = 0;
        Calendar start = Calendar.getInstance();
        start.setTime(calendar_start.getTime());
        Calendar end = Calendar.getInstance();
        end.setTime(start.getTime());
        end.add(Calendar.DATE, interval - 1);

        while (start.compareTo(calendar_end) <= 0) {

            String[] value = new String[2];
            result.put("m" + number, value);

            if (end.compareTo(calendar_end) >= 0) {
                end.setTime(calendar_end.getTime());
            }
            value[0] = DateUtil.getDate(start.getTime());
            value[1] = DateUtil.getDate(end.getTime());
            start.add(Calendar.DATE, interval);
            end.add(Calendar.DATE, interval);
            number++;
        }
        return result;
    }

}

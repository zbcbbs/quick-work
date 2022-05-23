package com.dongzz.quick.common.utils;

import com.dongzz.quick.common.exception.ApplicationException;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 封装分页信息的实体
 */
public class Page<T> implements Iterable<T> {

    protected List<T> result = new ArrayList<T>(); // 数据
    protected long totalCount = -1; // 总记录数
    protected int totalPage = -1; // 总页数
    protected int pageNo = 1; // 页码
    protected int pageSize = 10; // 分页单位
    protected String orderBy; // 排序字段
    protected String orderDir; // 排序规则
    protected boolean countTotal = true; // 是否计算总记录数

    public Page() {

    }

    public Page(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    /**
     * 获取下一页的页码，序号从1开始
     * 当前页为尾页时仍返回尾页页码
     *
     * @return
     */
    public int getNextPage() {
        if (hasNextPage()) {
            return getPageNo() + 1;
        } else {
            return getPageNo();
        }
    }

    /**
     * 获取上一页的页码，序号从1开始
     * 当前页为首页时仍返回首页页码
     *
     * @return
     */
    public int getPrePage() {
        if (hasPrePage()) {
            return getPageNo() - 1;
        } else {
            return getPageNo();
        }
    }

    /**
     * 获取当前页的记录
     *
     * @return
     */
    public List<T> getResult() {
        return result;
    }

    /**
     * 获取总记录数，默认值为-1
     *
     * @return
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * 获取总页数，默认值为-1
     * 记录数除以分页单位，结果向上取整，即为总页数
     *
     * @return
     */
    public int getTotalPage() {
        // 获取大于或等于表达式的最小整数，向上取整
        return (int) Math.ceil((double) totalCount / (double) getPageSize());

    }

    /**
     * 判断是否存在下一页
     *
     * @return
     */
    public boolean hasNextPage() {
        return (getPageNo() + 1 <= getTotalPage());
    }

    /**
     * 判断是否存在上一页
     *
     * @return
     */
    public boolean hasPrePage() {
        return (getPageNo() > 1);
    }

    /**
     * 当前页是否是第一页
     *
     * @return
     */
    public boolean isFirstPage() {
        return !hasPrePage();
    }

    /**
     * 当前页是否是最后一页
     *
     * @return
     */
    public boolean isLastPage() {
        return !hasNextPage();
    }

    /**
     * 获取当前页记录的迭代器，可以直接遍历当前页记录
     */
    @Override
    public Iterator<T> iterator() {
        return result.iterator();
    }

    /**
     * 设置当前页记录
     *
     * @param result
     */
    public void setResult(final List<T> result) {
        this.result = result;
    }

    /**
     * 设置总记录数
     *
     * @param totalCount
     */
    public void setTotalCount(final long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 设置总页数
     *
     * @param totalPage
     */
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * 获取当前页页码，默认值为1
     *
     * @return
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 设置当前页页码，小于1时自动调整为1
     *
     * @param pageNo
     */
    public void setPageNo(final int pageNo) {
        this.pageNo = pageNo;
        if (pageNo < 1) {
            this.pageNo = 1;
        }
    }

    /**
     * 获取分页单位，默认值为10
     *
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置分页单位，小于1时自动调整为1
     *
     * @param pageSize
     */
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
        if (pageSize < 1) {
            this.pageSize = 1;
        }
    }

    /**
     * 获取排序字段，多个排序字段时用','分隔
     *
     * @return
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * 设置排序字段，多个排序字段时用','分隔
     *
     * @param orderBy
     */
    public void setOrderBy(final String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * 获取排序规则，多个排序规则时用','分隔
     *
     * @return
     */
    public String getOrderDir() {
        return orderDir;
    }

    /**
     * 设置排序规则，多个排序规则时用','分隔
     *
     * @param orderDir 排序规则  asc 或 desc
     */
    public void setOrderDir(final String orderDir) {
        String lowcaseOrderDir = StringUtils.lowerCase(orderDir);
        //检查
        if (lowcaseOrderDir != null && lowcaseOrderDir.trim().length() > 0) {
            String[] orderDirs = StringUtils.split(lowcaseOrderDir, ',');
            for (String orderDirStr : orderDirs) {
                if (!StringUtils.equals(Sort.DESC, orderDirStr) && !StringUtils.equals(Sort.ASC, orderDirStr)) {
                    throw new ApplicationException("排序规则" + orderDirStr + "格式不正确！");
                }
            }
            this.orderDir = lowcaseOrderDir;
        }
    }

    /**
     * 根据设置的排序规则生成排序集合
     *
     * @return
     */
    public List<Sort> createSorts() {
        List<Sort> orders = Lists.newArrayList();
        if (StringUtil.isNotBlank(orderBy) && StringUtil.isNotBlank(orderDir)) {
            String[] orderBys = StringUtils.split(orderBy, ',');
            String[] orderDirs = StringUtils.split(orderDir, ',');
            Assert.isTrue(orderBys.length == orderDirs.length, "排序字段与排序规则的个数不相等！");
            for (int i = 0; i < orderBys.length; i++) {
                orders.add(new Sort(orderBys[i], orderDirs[i]));
            }
        }
        return orders;
    }

    /**
     * 判断是否设置了排序字段
     *
     * @return
     */
    public boolean isOrderBySetted() {
        return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(orderDir));
    }

    /**
     * 是否计算总记录数
     *
     * @return
     */
    public boolean isCountTotal() {
        return countTotal;
    }

    /**
     * 设置默认是否计算总记录数
     *
     * @param countTotal
     */
    public void setCountTotal(boolean countTotal) {
        this.countTotal = countTotal;
    }

    /**
     * 计算当前页第一条记录在总结果集中的位置，索引从0开始
     *
     * @return
     */
    public int getFirstRecord() {
        return ((pageNo - 1) * pageSize);
    }

    /**
     * 计算当前页最后一条记录在总结果集中的位置
     *
     * @return
     */
    public long getLastRecord() {
        if (hasNextPage()) {
            return (pageNo) * pageSize;
        } else {
            return totalCount;
        }
    }

    /**
     * 排序类，静态内部类
     */
    public static class Sort {
        public static final String ASC = "asc";
        public static final String DESC = "desc";

        private final String prop;
        private final String dir;

        public Sort(String prop, String dir) {
            this.prop = prop;
            this.dir = dir;
        }

        public String getProp() {
            return prop;
        }

        public String getDir() {
            return dir;
        }

    }
}

package com.dongzz.quick.tools.dao;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import com.dongzz.quick.tools.domain.ToolMail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 邮件 数据访问接口
 */
public interface ToolMailMapper extends BaseMybatisMapper<ToolMail> {

    /**
     * 新增邮件 获取新增邮件主键
     *
     * @param mail 邮件
     * @throws Exception
     */
    void insertMail(ToolMail mail) throws Exception;

    /**
     * 新增邮件发送记录
     *
     * @param mailId 邮件ID
     * @param toUser 收件人
     * @param status 状态
     * @throws Exception
     */
    void insertMailTo(@Param("mailId") Integer mailId, @Param("toUser") String toUser, @Param("status") Integer status) throws Exception;

    /**
     * 条件查询 统计数量
     *
     * @param params
     * @return
     * @throws Exception
     */
    int count(@Param("params") Map<String, Object> params) throws Exception;

    /**
     * 条件和分页查询
     *
     * @param params 查询条件
     * @param offset 起始索引
     * @param limit  分页单位
     * @return
     * @throws Exception
     */
    List<ToolMail> selectAllMails(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit) throws Exception;

    /**
     * 查询邮件发送记录
     *
     * @param mailId 邮件ID
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> selectMailDetailsTo(Integer mailId) throws Exception;
}
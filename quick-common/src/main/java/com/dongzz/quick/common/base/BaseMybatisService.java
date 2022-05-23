package com.dongzz.quick.common.base;

import com.dongzz.quick.common.annotation.query.Criteria;
import com.dongzz.quick.common.utils.Page;

import java.util.List;

/**
 * mybatis 通用 service 接口，定义通用的增删改查方法！
 *
 * @param <T>
 */
public interface BaseMybatisService<T> {

    /**
     * 新增
     *
     * @param entity 实体
     * @return
     * @throws Exception
     */
    int insert(T entity) throws Exception;

    /**
     * 动态新增，仅操作非空字段
     *
     * @param entity 实体
     * @return
     * @throws Exception
     */
    int insertSelective(T entity) throws Exception;

    /**
     * 根据主键删除
     *
     * @param entity 实体
     * @return
     * @throws Exception
     */
    int delete(T entity) throws Exception;

    /**
     * 根据主键删除
     *
     * @param pk 主键
     * @return
     * @throws Exception
     */
    int deleteByPk(Object pk) throws Exception;

    /**
     * 修改
     *
     * @param entity 实体
     * @return
     * @throws Exception
     */
    int update(T entity) throws Exception;

    /**
     * 动态修改，仅操作非空字段
     *
     * @param entity 实体
     * @return
     * @throws Exception
     */
    int updateSelective(T entity) throws Exception;

    /**
     * 根据实体中的属性执行条件查询，查询条件用 '='
     *
     * @param entity 实体
     * @return 只能返回一个值，若返回多个会报错
     * @throws Exception
     */
    T selectOne(T entity) throws Exception;

    /**
     * 根据实体中的属性执行条件查询，查询条件使用 '='
     *
     * @param entity 实体
     * @return
     * @throws Exception
     */
    List<T> selectAll(T entity) throws Exception;

    /**
     * 条件查询
     *
     * @param criteria 条件
     * @param sorts  排序规则
     * @return
     * @throws Exception
     */
    List<T> selectAll(Criteria criteria, List<Page.Sort> sorts) throws Exception;

    /**
     * 根据实体中的属性查询符合条件的记录数，查询条件使用 '='
     *
     * @param entity 实体
     * @return
     * @throws Exception
     */
    int selectCount(T entity) throws Exception;

    /**
     * 查询所有
     *
     * @return
     * @throws Exception
     */
    List<T> selectAll() throws Exception;

    /**
     * 根据主键查询
     *
     * @param pk 主键
     * @return
     * @throws Exception
     */
    T selectByPk(Object pk) throws Exception;

    /**
     * 分页查询
     *
     * @param page
     * @return
     * @throws Exception
     */
    Page<T> selectPage(Page<T> page) throws Exception;

    /**
     * 分页条件查询
     *
     * @param page     分页
     * @param criteria 条件
     * @return
     * @throws Exception
     */
    Page<T> selectPage(Page<T> page, Criteria criteria) throws Exception;

}

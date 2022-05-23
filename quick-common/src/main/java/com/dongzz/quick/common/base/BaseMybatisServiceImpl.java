package com.dongzz.quick.common.base;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.dongzz.quick.common.annotation.query.Criteria;
import com.dongzz.quick.common.annotation.query.Query;
import com.dongzz.quick.common.utils.GenericUtil;
import com.dongzz.quick.common.utils.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.util.List;

/**
 * mybatis通用service接口实现
 * 注意：
 * Spring4开始支持泛型注入，将通用Mapper<T>注入到当前类中
 * 当前类必须定义为  abstract 抽象类
 */
public abstract class BaseMybatisServiceImpl<T> implements BaseMybatisService<T> {

    private Class<T> entityClass;

    public BaseMybatisServiceImpl() {
        this.entityClass = GenericUtil.getSuperClassGenericType(this.getClass(), 0); // 获取父类泛型
    }

    @Autowired
    private BaseMybatisMapper<T> baseMapper;

    @Override
    public int insert(T entity) throws Exception {
        return baseMapper.insert(entity);
    }

    @Override
    public int insertSelective(T entity) throws Exception {
        return baseMapper.insertSelective(entity);
    }

    @Override
    public int delete(T entity) throws Exception {
        return baseMapper.delete(entity);
    }

    @Override
    public int deleteByPk(Object pk) throws Exception {
        return baseMapper.deleteByPrimaryKey(pk);
    }

    @Override
    public int update(T entity) throws Exception {
        return baseMapper.updateByPrimaryKey(entity);
    }

    @Override
    public int updateSelective(T entity) throws Exception {
        return baseMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public T selectOne(T entity) throws Exception {
        return baseMapper.selectOne(entity);
    }

    @Override
    public List<T> selectAll(T entity) throws Exception {
        return baseMapper.select(entity);
    }

    @Override
    public List<T> selectAll(Criteria criteria, List<Page.Sort> sorts) throws Exception {
        Example example = new Example(entityClass);
        Example.Criteria cr = example.createCriteria();
        // 解析查询条件
        if (ObjectUtil.isNotNull(criteria)) {
            Class<? extends Criteria> cls = criteria.getClass();
            Field[] fields = ReflectUtil.getFields(cls);
            for (Field field : fields) {
                if (field.isAnnotationPresent(Query.class)) {
                    field.setAccessible(true);
                    Query query = field.getAnnotation(Query.class);
                    Query.Type type = query.type(); // 查询类型
                    String prop = field.getName(); // 属性名
                    Object value = field.get(criteria); // 属性值
                    if (ObjectUtil.isNotEmpty(value)) {
                        if (Query.Type.EQ.equals(type)) {
                            cr.andEqualTo(prop, value);
                        }
                        if (Query.Type.GE.equals(type)) {
                            cr.andGreaterThanOrEqualTo(prop, value);
                        }
                        if (Query.Type.GT.equals(type)) {
                            cr.andGreaterThan(prop, value);
                        }
                        if (Query.Type.LE.equals(type)) {
                            cr.andLessThanOrEqualTo(prop, value);
                        }
                        if (Query.Type.LT.equals(type)) {
                            cr.andLessThan(prop, value);
                        }
                        if (Query.Type.LIKE.equals(type)) {
                            cr.andLike(prop, "%" + value.toString() + "%");
                        }
                        if (Query.Type.STARTWITH.equals(type)) {
                            cr.andLike(prop, value.toString() + "%");
                        }
                        if (Query.Type.ENDWITH.equals(type)) {
                            cr.andLike(prop, "%" + value.toString());
                        }
                        if (Query.Type.IN.equals(type)) {
                            List<?> iter = (List<?>) value;
                            cr.andIn(prop, iter);
                        }
                        if (Query.Type.NOT_IN.equals(type)) {
                            List<?> iter = (List<?>) value;
                            cr.andNotIn(prop, iter);
                        }
                        if (Query.Type.NOT_EQ.equals(type)) {
                            cr.andNotEqualTo(prop, value);
                        }
                        if (Query.Type.NOT_NULL.equals(type)) {
                            cr.andIsNotNull(prop);
                        }
                        if (Query.Type.IS_NULL.equals(type)) {
                            cr.andIsNull(prop);
                        }
                        if (Query.Type.BETWEEN.equals(type)) {
                            List<?> iter = (List<?>) value;
                            cr.andBetween(prop, iter.get(0), iter.get(1));
                        }
                    }
                }
            }
        }
        // 排序
        if (CollectionUtil.isNotEmpty(sorts)) {
            for (Page.Sort sort : sorts) {
                if (Page.Sort.ASC.equals(sort.getDir())) {
                    example.orderBy(sort.getProp()).asc();
                } else {
                    example.orderBy(sort.getProp()).desc();
                }
            }
        }

        List<T> result = baseMapper.selectByExample(example);
        return result;
    }

    @Override
    public int selectCount(T entity) throws Exception {
        return baseMapper.selectCount(entity);
    }

    @Override
    public List<T> selectAll() throws Exception {
        return baseMapper.selectAll();
    }


    @Override
    public T selectByPk(Object pk) throws Exception {
        return baseMapper.selectByPrimaryKey(pk);
    }

    @Override
    public Page<T> selectPage(Page<T> page) throws Exception {
        Example example = new Example(entityClass);
        // 排序
        List<Page.Sort> sorts = page.createSorts();
        if (CollectionUtil.isNotEmpty(sorts)) {
            for (Page.Sort sort : sorts) {
                if (Page.Sort.ASC.equals(sort.getDir())) {
                    example.orderBy(sort.getProp()).asc();
                } else {
                    example.orderBy(sort.getProp()).desc();
                }

            }
        }
        com.github.pagehelper.Page<Object> pr = PageHelper.startPage(page.getPageNo(), page.getPageSize(), true);
        List<T> result = baseMapper.selectByExample(example);
        page.setTotalCount(pr.getTotal());
        page.setTotalPage(pr.getPages());
        page.setResult(result);
        return page;
    }

    @Override
    public Page<T> selectPage(Page<T> page, Criteria criteria) throws Exception {
        Example example = new Example(entityClass);
        Example.Criteria cr = example.createCriteria();
        // 解析查询条件
        if (ObjectUtil.isNotNull(criteria)) {
            Class<? extends Criteria> cls = criteria.getClass();
            Field[] fields = ReflectUtil.getFields(cls);
            for (Field field : fields) {
                if (field.isAnnotationPresent(Query.class)) {
                    field.setAccessible(true);
                    Query query = field.getAnnotation(Query.class);
                    Query.Type type = query.type(); // 查询类型
                    String prop = field.getName(); // 属性名
                    Object value = field.get(criteria); // 属性值
                    if (ObjectUtil.isNotEmpty(value)) {
                        if (Query.Type.EQ.equals(type)) {
                            cr.andEqualTo(prop, value);
                        }
                        if (Query.Type.GE.equals(type)) {
                            cr.andGreaterThanOrEqualTo(prop, value);
                        }
                        if (Query.Type.GT.equals(type)) {
                            cr.andGreaterThan(prop, value);
                        }
                        if (Query.Type.LE.equals(type)) {
                            cr.andLessThanOrEqualTo(prop, value);
                        }
                        if (Query.Type.LT.equals(type)) {
                            cr.andLessThan(prop, value);
                        }
                        if (Query.Type.LIKE.equals(type)) {
                            cr.andLike(prop, "%" + value.toString() + "%");
                        }
                        if (Query.Type.STARTWITH.equals(type)) {
                            cr.andLike(prop, value.toString() + "%");
                        }
                        if (Query.Type.ENDWITH.equals(type)) {
                            cr.andLike(prop, "%" + value.toString());
                        }
                        if (Query.Type.IN.equals(type)) {
                            List<?> iter = (List<?>) value;
                            cr.andIn(prop, iter);
                        }
                        if (Query.Type.NOT_IN.equals(type)) {
                            List<?> iter = (List<?>) value;
                            cr.andNotIn(prop, iter);
                        }
                        if (Query.Type.NOT_EQ.equals(type)) {
                            cr.andNotEqualTo(prop, value);
                        }
                        if (Query.Type.NOT_NULL.equals(type)) {
                            cr.andIsNotNull(prop);
                        }
                        if (Query.Type.IS_NULL.equals(type)) {
                            cr.andIsNull(prop);
                        }
                        if (Query.Type.BETWEEN.equals(type)) {
                            List<?> iter = (List<?>) value;
                            cr.andBetween(prop, iter.get(0), iter.get(1));
                        }
                    }
                }
            }
        }
        // 排序
        List<Page.Sort> sorts = page.createSorts();
        if (CollectionUtil.isNotEmpty(sorts)) {
            for (Page.Sort sort : sorts) {
                if (Page.Sort.ASC.equals(sort.getDir())) {
                    example.orderBy(sort.getProp()).asc();
                } else {
                    example.orderBy(sort.getProp()).desc();
                }
            }
        }
        // 分页插件只对即将执行的sql起作用
        com.github.pagehelper.Page<Object> pr = PageHelper.startPage(page.getPageNo(), page.getPageSize(), true);
        List<T> result = baseMapper.selectByExample(example);
        page.setTotalCount(pr.getTotal());
        page.setTotalPage(pr.getPages());
        page.setResult(result);
        return page;
    }
}

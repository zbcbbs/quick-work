package ${package}.dao;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import ${package}.domain.${originalClassName};
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * ${comment}
 * @author ${author}
 * @date ${date}
 * @email ${email}
 */
public interface ${originalClassName}Mapper extends BaseMybatisMapper<${originalClassName}> {

    /**
    * 条件查询 统计数量
    *
    * @param params 查询条件
    * @return
    * @throws Exception
    */
    Integer count(@Param("params") Map<String, Object> params) throws Exception;

    /**
    * 条件查询 分页
    *
    * @param params 查询条件 包含排序
    * @param offset 每页起始索引
    * @param limit  每页显示条数
    * @return
    * @throws Exception
    */
    List<${originalClassName}> selectPage(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
        @Param("limit") Integer limit) throws Exception;
}
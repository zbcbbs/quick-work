package ${package}.service.dto;

import lombok.Data;
import com.dongzz.quick.common.annotation.query.Criteria;
<#if queryHasTimestamp>
import java.util.Date;
</#if>
<#if queryHasBigDecimal>
import java.math.BigDecimal;
</#if>
<#if betweens??>
import java.util.List;
</#if>
<#if queryColumns??>
import com.dongzz.quick.common.annotation.query.Query;
</#if>

/**
 * ${comment}
 * @author ${author}
 * @date ${date}
 * @email ${email}
 */
@Data
public class ${className}QueryCriteria implements Criteria {
<#if queryColumns??>
    <#list queryColumns as column>

    <#if column.queryType = '='>
        @Query
        private ${column.columnType} ${column.changeColumnName};
    </#if>
    <#if column.queryType = 'Like'>
        @Query(type = Query.Type.LIKE)
        private ${column.columnType} ${column.changeColumnName};
    </#if>
    <#if column.queryType = '!='>
        @Query(type = Query.Type.NOT_EQ)
        private ${column.columnType} ${column.changeColumnName};
    </#if>
    <#if column.queryType = 'NotNull'>
        @Query(type = Query.Type.NOT_NULL)
        private ${column.columnType} ${column.changeColumnName};
    </#if>
    <#if column.queryType = '>='>
        @Query(type = Query.Type.GE)
        private ${column.columnType} ${column.changeColumnName};
    </#if>
    <#if column.queryType = '<='>
        @Query(type = Query.Type.LE)
        private ${column.columnType} ${column.changeColumnName};
    </#if>
    </#list>
</#if>
<#if betweens??>
    <#list betweens as column>
    @Query(type = Query.Type.BETWEEN)
    private List<${column.columnType}> ${column.changeColumnName};
    </#list>
</#if>
}
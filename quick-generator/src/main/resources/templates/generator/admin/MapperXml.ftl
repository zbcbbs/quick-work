<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${package}.dao.${originalClassName}Mapper">

	<!-- where -->
	<sql id="where">
		<where>
            <#if queryColumns??>
            <#list queryColumns as column>
			<#if column.queryType == '='>
			<if test="params.${column.changeColumnName} != null and params.${column.changeColumnName} != ''">
				and ${column.columnName} = ${r"#"}{params.${column.changeColumnName}}
			</if>
			</#if>
			<#if column.queryType == 'Like'>
			<if test="params.${column.changeColumnName} != null and params.${column.changeColumnName} != ''">
				and ${column.columnName} like concat('%', ${r"#"}{params.${column.changeColumnName}}, '%')
			</if>
			</#if>
			</#list>
			</#if>
		</where>
	</sql>

	<!-- 条件查询 统计数量 -->
	<select id="count" resultType="int">
		select count(1) from ${tableName}
		<include refid="where" />
	</select>

	<!-- 条件查询 分页 -->
	<select id="selectPage" resultType="${originalClassName}">
		select * from ${tableName}
		<include refid="where" />
		<if test="params.orderBy != null and params.orderBy != ''">
		${r"$"}{params.orderBy}
		</if>
		<if test="limit != null and limit != 0">
			limit ${r"#"}{offset}, ${r"#"}{limit}
		</if>
	</select>

</mapper>
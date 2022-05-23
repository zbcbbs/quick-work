package com.dongzz.quick.generator.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.*;
import com.dongzz.quick.common.utils.DateUtil;
import com.dongzz.quick.common.utils.FileUtil;
import com.dongzz.quick.common.utils.StringUtil;
import com.dongzz.quick.generator.domain.CodeColumnInfo;
import com.dongzz.quick.generator.domain.CodeGenConfig;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

import static com.dongzz.quick.common.utils.FileUtil.SYS_TEM_DIR;

/**
 * 代码生成
 */
public class GenUtil {

    private static final String TIMESTAMP = "Date";
    private static final String BIGDECIMAL = "BigDecimal";
    public static final String PK = "PRI";
    public static final String EXTRA = "auto_increment";

    /**
     * 预览
     *
     * @param columns
     * @param config
     * @return
     */
    public static List<Map<String, Object>> preview(List<CodeColumnInfo> columns, CodeGenConfig config) {
        Map<String, Object> genMap = getGenData(columns, config);
        List<Map<String, Object>> genList = new ArrayList<>();
        // 获取后端模版
        List<String> templates = getAdminTemplateNames();
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));
        for (String templateName : templates) {
            Map<String, Object> map = new HashMap<>(1);
            Template template = engine.getTemplate("generator/admin/" + templateName + ".ftl");
            map.put("content", template.render(genMap));
            map.put("name", templateName);
            genList.add(map);
        }
        // 获取前端模版
        templates = getFrontTemplateNames();
        for (String templateName : templates) {
            Map<String, Object> map = new HashMap<>(1);
            Template template = engine.getTemplate("generator/front/" + templateName + ".ftl");
            map.put(templateName, template.render(genMap));
            map.put("content", template.render(genMap));
            map.put("name", templateName);
            genList.add(map);
        }
        return genList;
    }

    /**
     * 下载
     *
     * @param columns
     * @param config
     * @return 生成文件的临时存储路径
     * @throws IOException
     */
    public static String download(List<CodeColumnInfo> columns, CodeGenConfig config) throws IOException {
        // 临时路径
        String tempPath = SYS_TEM_DIR + "qlq-gen-temp" + File.separator + config.getTableName() + File.separator;
        Map<String, Object> genMap = getGenData(columns, config);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));
        // 生成后端代码
        List<String> templates = getAdminTemplateNames();
        for (String templateName : templates) {
            Template template = engine.getTemplate("generator/admin/" + templateName + ".ftl");
            String filePath = getAdminFilePath(templateName, config, genMap.get("className").toString(), genMap.get("originalClassName").toString(), tempPath + "quick" + File.separator);
            assert filePath != null;
            File file = new File(filePath);
            // 若非覆盖生成
            if (!config.getCover() && FileUtil.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, genMap);
        }
        // 生成前端代码
        templates = getFrontTemplateNames();
        for (String templateName : templates) {
            Template template = engine.getTemplate("generator/front/" + templateName + ".ftl");
            String path = tempPath + "quick-web" + File.separator;
            String apiPath = path + "src" + File.separator + "api" + File.separator;
            String srcPath = path + "src" + File.separator + "views" + File.separator + genMap.get("changeClassName").toString() + File.separator;
            String filePath = getFrontFilePath(templateName, apiPath, srcPath, genMap.get("changeClassName").toString());
            assert filePath != null;
            File file = new File(filePath);
            // 若非覆盖生成
            if (!config.getCover() && FileUtil.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, genMap);
        }
        return tempPath;
    }

    /**
     * 代码生成
     *
     * @param columnInfos 字段信息
     * @param config      配置信息
     * @throws IOException
     */
    public static void generator(List<CodeColumnInfo> columnInfos, CodeGenConfig config) throws IOException {
        Map<String, Object> genMap = getGenData(columnInfos, config); // 解析填充数据
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));
        // 生成后端代码
        List<String> templates = getAdminTemplateNames();
        for (String templateName : templates) {
            Template template = engine.getTemplate("generator/admin/" + templateName + ".ftl");
            String rootPath = System.getProperty("user.dir"); // 项目根路径 \quick-work
            String filePath = getAdminFilePath(templateName, config, genMap.get("className").toString(), genMap.get("originalClassName").toString(), rootPath); // 获取后端文件路径及名称

            assert filePath != null;
            File file = new File(filePath);

            // 若非覆盖生成
            if (!config.getCover() && FileUtil.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, genMap);
        }

        // 生成前端代码
        templates = getFrontTemplateNames();
        for (String templateName : templates) {
            Template template = engine.getTemplate("generator/front/" + templateName + ".ftl");
            String rootPath = System.getProperty("user.dir") + File.separator + config.getModuleName() + File.separator; // 项目根路径 \quick-work
            String filePath = getFrontFilePath(templateName, rootPath + config.getApiPath(), rootPath + config.getPath(), genMap.get("changeClassName").toString());

            assert filePath != null;
            File file = new File(filePath);

            // 如果非覆盖生成
            if (!config.getCover() && FileUtil.exist(file)) {
                continue;
            }
            // 生成代码
            genFile(file, template, genMap);
        }
    }

    public static void main(String[] args) {
        String tableName = "code_column_info";
        System.out.println(StrUtil.removePrefix(tableName, "code_"));
        System.out.println(StringUtil.toCamelCase(tableName));
        System.out.println(StringUtil.toCapitalizeCamelCase(tableName));
        System.out.println(StringUtil.uncapitalize(StringUtil.toCapitalizeCamelCase(tableName)));
        System.out.println(System.getProperty("user.dir"));
    }

    /**
     * 获取模板需要的数据
     */
    private static Map<String, Object> getGenData(List<CodeColumnInfo> columnInfos, CodeGenConfig config) {
        // 存储模板字段数据
        Map<String, Object> genMap = new HashMap<>(16);
        // 控制器和接口文档说明
        genMap.put("apiAlias", config.getApiAlias());
        // 包名称
        genMap.put("package", config.getPack());
        // 模块名称
        genMap.put("moduleName", config.getModuleName());
        // 作者
        genMap.put("author", config.getAuthor());
        // 邮箱
        genMap.put("email", config.getEmail());
        // 注释
        genMap.put("comment", config.getComment());
        // 创建日期
        genMap.put("date", DateUtil.getDate("yyyy/MM/dd HH:mm", new Date()));
        // 表名
        genMap.put("tableName", config.getTableName());
        // 大写开头的类名
        String className = StringUtil.toCapitalizeCamelCase(config.getTableName());
        // 小写开头的类名
        String changeClassName = StringUtil.toCamelCase(config.getTableName());
        // 原始类名
        genMap.put("originalClassName", className);
        // 判断是否去除表前缀
        if (StringUtil.isNotEmpty(config.getPrefix())) {
            className = StringUtil.toCapitalizeCamelCase(StrUtil.removePrefix(config.getTableName(), config.getPrefix()));
            changeClassName = StringUtil.toCamelCase(StrUtil.removePrefix(config.getTableName(), config.getPrefix()));
            changeClassName = StringUtil.uncapitalize(changeClassName);
        }
        // 保存类名
        genMap.put("className", className);
        // 保存小写开头的类名
        genMap.put("changeClassName", changeClassName);
        // 存在 Timestamp 字段
        genMap.put("hasTimestamp", false);
        // 查询类中存在 Timestamp 字段
        genMap.put("queryHasTimestamp", false);
        // 存在 BigDecimal 字段
        genMap.put("hasBigDecimal", false);
        // 查询类中存在 BigDecimal 字段
        genMap.put("queryHasBigDecimal", false);
        // 是否需要创建查询
        genMap.put("hasQuery", false);
        // 自增主键
        genMap.put("auto", false);
        // 存在字典
        genMap.put("hasDict", false);
        // 存在日期注解
        genMap.put("hasDateAnnotation", false);
        // 保存字段信息
        List<Map<String, Object>> columns = new ArrayList<>();
        // 保存查询字段的信息
        List<Map<String, Object>> queryColumns = new ArrayList<>();
        // 存储字典信息
        List<String> dicts = new ArrayList<>();
        // 存储 between 信息
        List<Map<String, Object>> betweens = new ArrayList<>();
        // 存储不为空的字段信息
        List<Map<String, Object>> isNotNullColumns = new ArrayList<>();

        for (CodeColumnInfo column : columnInfos) {
            Map<String, Object> listMap = new HashMap<>(16);
            // 字段描述
            listMap.put("remark", column.getRemark());
            // 字段类型
            listMap.put("columnKey", column.getKeyType());
            // 主键类型
            String colType = ColUtil.cloToJava(column.getColumnType());
            // 小写开头的字段名
            String changeColumnName = StringUtil.toCamelCase(column.getColumnName());
            // 大写开头的字段名
            String capitalColumnName = StringUtil.toCapitalizeCamelCase(column.getColumnName());
            if (PK.equals(column.getKeyType())) {
                // 存储主键类型
                genMap.put("pkColumnType", colType);
                // 存储小写开头的字段名
                genMap.put("pkChangeColName", changeColumnName);
                // 存储大写开头的字段名
                genMap.put("pkCapitalColName", capitalColumnName);
            }
            // 是否存在 Timestamp 类型的字段
            if (TIMESTAMP.equals(colType)) {
                genMap.put("hasTimestamp", true);
            }
            // 是否存在 BigDecimal 类型的字段
            if (BIGDECIMAL.equals(colType)) {
                genMap.put("hasBigDecimal", true);
            }
            // 主键是否自增
            if (EXTRA.equals(column.getExtra())) {
                genMap.put("auto", true);
            }
            // 主键存在字典
            if (StringUtil.isNotBlank(column.getDictName())) {
                genMap.put("hasDict", true);
                dicts.add(column.getDictName());
            }

            // 存储字段类型
            listMap.put("columnType", colType);
            // 存储字段原始名称
            listMap.put("columnName", column.getColumnName());
            // 不为空
            listMap.put("istNotNull", column.getNotNull());
            // 字段列表显示
            listMap.put("columnShow", column.getListShow());
            // 表单显示
            listMap.put("formShow", column.getFormShow());
            // 表单组件类型
            listMap.put("formType", StringUtil.isNotBlank(column.getFormType()) ? column.getFormType() : "Input");
            // 小写开头的字段名称
            listMap.put("changeColumnName", changeColumnName);
            //大写开头的字段名称
            listMap.put("capitalColumnName", capitalColumnName);
            // 字典名称
            listMap.put("dictName", column.getDictName());
            // 日期注解
            listMap.put("dateAnnotation", column.getDateAnnotation());
            if (StringUtil.isNotBlank(column.getDateAnnotation())) {
                genMap.put("hasDateAnnotation", true);
            }
            // 添加非空字段信息
            if (column.getNotNull()) {
                isNotNullColumns.add(listMap);
            }
            // 判断是否有查询，如有则把查询的字段set进columnQuery
            if (!StringUtil.isBlank(column.getQueryType())) {
                // 查询类型
                listMap.put("queryType", column.getQueryType());
                // 是否存在查询
                genMap.put("hasQuery", true);
                if (TIMESTAMP.equals(colType)) {
                    // 查询中存储 Timestamp 类型
                    genMap.put("queryHasTimestamp", true);
                }
                if (BIGDECIMAL.equals(colType)) {
                    // 查询中存储 BigDecimal 类型
                    genMap.put("queryHasBigDecimal", true);
                }
                if ("between".equalsIgnoreCase(column.getQueryType())) {
                    betweens.add(listMap);
                } else {
                    // 添加到查询列表中
                    queryColumns.add(listMap);
                }
            }
            // 添加到字段列表中
            columns.add(listMap);
        }
        // 保存字段列表
        genMap.put("columns", columns);
        // 保存查询列表
        genMap.put("queryColumns", queryColumns);
        // 保存字段列表
        genMap.put("dicts", dicts);
        // 保存查询列表
        genMap.put("betweens", betweens);
        // 保存非空字段信息
        genMap.put("isNotNullColumns", isNotNullColumns);
        return genMap;
    }

    /**
     * 获取后端代码模板名称
     *
     * @return
     */
    private static List<String> getAdminTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("Entity");
        templateNames.add("Dto");
        templateNames.add("Mapper");
        templateNames.add("MapperXml");
        templateNames.add("Controller");
        templateNames.add("QueryCriteria");
        templateNames.add("Service");
        templateNames.add("ServiceImpl");
        return templateNames;
    }

    /**
     * 获取前端代码模板名称
     *
     * @return
     */
    private static List<String> getFrontTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("index");
        templateNames.add("api");
        return templateNames;
    }

    /**
     * 获取后端文件路径以及名称
     */
    private static String getAdminFilePath(String templateName, CodeGenConfig config, String className, String originalClassName, String rootPath) {
        String projectPath = rootPath + File.separator + config.getModuleName(); // \quick-work\+模块名
        String packagePath = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        if (!ObjectUtils.isEmpty(config.getPack())) {
            packagePath += config.getPack().replace(".", File.separator) + File.separator; // 包路径
        }

        if ("Entity".equals(templateName)) {
            return packagePath + "domain" + File.separator + originalClassName + ".java";
        }

        if ("Controller".equals(templateName)) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if ("Service".equals(templateName)) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if ("ServiceImpl".equals(templateName)) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if ("Dto".equals(templateName)) {
            return packagePath + "service" + File.separator + "dto" + File.separator + className + "Dto.java";
        }

        if ("QueryCriteria".equals(templateName)) {
            return packagePath + "service" + File.separator + "dto" + File.separator + className + "QueryCriteria.java";
        }

        if ("Mapper".equals(templateName)) {
            return packagePath + "dao" + File.separator + originalClassName + "Mapper.java";
        }

        if ("MapperXml".equals(templateName)) {
            return packagePath + "dao" + File.separator + originalClassName + "Mapper.xml";
        }

        return null;
    }

    /**
     * 获取前端文件路径以及名称
     */
    private static String getFrontFilePath(String templateName, String apiPath, String path, String apiName) {

        if ("api".equals(templateName)) {
            return apiPath + File.separator + apiName + ".js";
        }

        if ("index".equals(templateName)) {
            return path + File.separator + "index.vue";
        }

        return null;
    }

    /**
     * 生成文件
     *
     * @param file     目标文件
     * @param template 模板
     * @param map      数据
     * @throws IOException
     */
    private static void genFile(File file, Template template, Map<String, Object> map) throws IOException {
        // 生成目标文件
        Writer writer = null;
        try {
            FileUtil.touch(file);
            writer = new FileWriter(file);
            template.render(map, writer);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            assert writer != null;
            writer.close();
        }
    }
}

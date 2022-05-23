package com.dongzz.quick.common.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import com.dongzz.quick.common.annotation.excel.ExcelColumn;
import com.dongzz.quick.common.annotation.excel.ExcelDate;
import com.dongzz.quick.common.annotation.excel.ExcelGroup;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Excel 工具类
 */
public class ExcelUtil {

    private final static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    // 格式
    private final static String EXCEL2003 = "xls";
    private final static String EXCEL2007 = "xlsx";

    //----------------------------------------- 导入 Excel ------------------------------------------

    /**
     * 读取 上传的 Excel
     *
     * @param cls  封装数据的 Class
     * @param file 上传的Excel文件
     * @return
     */
    public static <T> List<T> readExcel(Class<T> cls, MultipartFile file) {
        List<T> list = new ArrayList<>();
        String fileName = file.getOriginalFilename(); // 文件名
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            logger.error("Excel文件格式不正确！");
        }
        try {
            InputStream is = file.getInputStream(); // 输入流
            Workbook wb = fileName.endsWith(EXCEL2003) ? new HSSFWorkbook(is) : new XSSFWorkbook(is);
            list = parseExcel(cls, wb);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 读取 本地的 Excel
     *
     * @param path 文件存储路径
     * @param cls  封装数据的 Class
     * @return
     */
    public static <T> List<T> readExcel(String path, Class<T> cls) {
        List<T> list = new ArrayList<>();
        if (!path.matches("^.+\\.(?i)(xls)$") && !path.matches("^.+\\.(?i)(xlsx)$")) {
            logger.error("Excel文件格式不正确！");
        }
        try {
            InputStream is = new FileInputStream(new File(path));
            Workbook wb = path.endsWith(EXCEL2003) ? new HSSFWorkbook(is) : new XSSFWorkbook(is);
            list = parseExcel(cls, wb);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 解析Excel，将结果封装为 List 集合
     *
     * @param cls 封装数据的 Class
     * @param wb  工作簿
     * @return
     */
    private static <T> List<T> parseExcel(Class<T> cls, Workbook wb) {
        List<T> data = new ArrayList<>(); // 数据集合
        try {
            if (wb != null) {
                // 列标题 => 字段集合
                Map<String, List<Field>> classMap = new HashMap<>();
                List<Field> fields = Stream.of(cls.getDeclaredFields()).collect(Collectors.toList()); // 获取 字段集合
                fields.forEach(field -> {
                            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                            if (annotation != null) {
                                String value = annotation.value();
                                if (StringUtils.isBlank(value)) {
                                    return; // 作用和 continue 相同
                                }
                                if (!classMap.containsKey(value)) {
                                    classMap.put(value, new ArrayList<>());
                                }
                                field.setAccessible(true); // 通过反射 获取 private 属性的值
                                classMap.get(value).add(field);
                            }
                        }
                );
                // 列索引 => 字段集合
                Map<Integer, List<Field>> reflectionMap = new HashMap<>(16);
                // 默认读取第一个Sheet
                Sheet sheet = wb.getSheetAt(0);

                // 是否首行
                boolean firstRow = true;
                // 遍历行
                for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    //首行 提取注解
                    if (firstRow) {
                        for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(j);
                            String cellValue = getCellValue(cell); // 列标题
                            if (classMap.containsKey(cellValue)) {
                                reflectionMap.put(j, classMap.get(cellValue));
                            }
                        }
                        firstRow = false;
                    } else {
                        // 忽略空白行
                        if (row == null) {
                            continue;
                        }
                        try {
                            // 封装行数据
                            T t = cls.newInstance();
                            // 是否空白行
                            boolean allBlank = true;
                            // 将当前行每个单元格内容 赋值到 T 对应的字段，每行对应一个T
                            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                                if (reflectionMap.containsKey(j)) {
                                    Cell cell = row.getCell(j);
                                    String cellValue = getCellValue(cell); // 单元格值
                                    if (StringUtils.isNotBlank(cellValue)) {
                                        allBlank = false;
                                    }

                                    // 单元格 赋值给 T 对应的 字段
                                    // 一个单元格值 可以赋值给 多个字段
                                    // 此处可以对特殊格式的单元格值进行处理，分别赋值给多个
                                    List<Field> fieldList = reflectionMap.get(j); // 单元格 对应的字段集合
                                    if (fieldList.size() == 1) { // 一个字段
                                        Field field = fieldList.get(0);
                                        handleField(t, cellValue, field);
                                    } else { // 多个字段
                                        ExcelGroup ga = fieldList.get(0).getAnnotation(ExcelGroup.class);
                                        String split = ga.value(); // 分隔符
                                        String[] cellValues = cellValue.split(split);
                                        for (int k = 0; k < fieldList.size(); k++) {
                                            Field field = fieldList.get(k);
                                            try {
                                                handleField(t, cellValues[k], field); // 字段赋值
                                            } catch (Exception e) {
                                                logger.error(String.format("Reflect field:%s value:%s exception!", field.getName(), cellValue), e);
                                            }
                                        }
                                    }

                                }
                            }

                            if (!allBlank) {
                                data.add(t); // 添加行
                            } else {
                                logger.warn(String.format("Row:%s is blank ignored！", i)); // 忽略空白行
                            }
                        } catch (Exception e) {
                            logger.error(String.format("Parse row:%s exception！", i), e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(String.format("Parse excel exception！"), e);
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception e) {
                    logger.error(String.format("Parse excel exception！"), e);
                }
            }
        }
        return data;
    }

    /**
     * T对应的字段赋值
     *
     * @param t     对象
     * @param value 字段值
     * @param field 字段
     */
    private static <T> void handleField(T t, String value, Field field) {
        Class<?> type = field.getType(); // 字段类型
        if (type == null || type == void.class || StringUtils.isBlank(value)) {
            return;
        }

        try {
            if (type == Object.class) { // 对象类型
                field.set(t, value);
            } else if (type.getSuperclass() == null || type.getSuperclass() == Number.class) { // 数字类型
                if (type == int.class || type == Integer.class) {
                    field.set(t, NumberUtils.toInt(value));
                } else if (type == long.class || type == Long.class) {
                    field.set(t, NumberUtils.toLong(value));
                } else if (type == byte.class || type == Byte.class) {
                    field.set(t, NumberUtils.toByte(value));
                } else if (type == short.class || type == Short.class) {
                    field.set(t, NumberUtils.toShort(value));
                } else if (type == double.class || type == Double.class) {
                    field.set(t, NumberUtils.toDouble(value));
                } else if (type == float.class || type == Float.class) {
                    field.set(t, NumberUtils.toFloat(value));
                } else if (type == char.class || type == Character.class) {
                    field.set(t, CharUtils.toChar(value));
                } else if (type == boolean.class) {
                    field.set(t, BooleanUtils.toBoolean(value));
                } else if (type == BigDecimal.class) {
                    field.set(t, new BigDecimal(value));
                }
            } else if (type == Boolean.class) { // 布尔格式
                field.set(t, BooleanUtils.toBoolean(value));
            } else if (type == Date.class) { // 日期格式
                Date date = DateUtil.convertStringToDate(value);
                field.set(t, date);
            } else if (type == String.class) { // 字符串
                field.set(t, value);
            } else {
                Constructor<?> constructor = type.getConstructor(String.class); // 字段类型构造器
                field.set(t, constructor.newInstance(value)); // 创建指定类型的值
            }
        } catch (Exception e) {
            logger.error(String.format("Field:%s set value exception！", field.getName()), e);
        }

    }

    /**
     * 获取 单元格值
     *
     * @param cell 单元格
     * @return
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) { // 日期数字
                return HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();
            } else { // 普通数字
                return new BigDecimal(cell.getNumericCellValue()).toString();
            }
        } else if (cell.getCellTypeEnum() == CellType.STRING) { // 字符串
            return StringUtils.trimToEmpty(cell.getStringCellValue());
        } else if (cell.getCellTypeEnum() == CellType.FORMULA) { // 公式
            return StringUtils.trimToEmpty(cell.getCellFormula());
        } else if (cell.getCellTypeEnum() == CellType.BLANK) { // 空白
            return "";
        } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) { // 布尔
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellTypeEnum() == CellType.ERROR) { // 异常
            return "ERROR";
        } else {
            return cell.toString().trim();
        }

    }

    //------------------------------------------ 导出 Excel ------------------------------------------

    /**
     * 输出 Excel 到浏览器
     *
     * @param name     文件名称 a.xlsx 或 a.xls
     * @param data     数据集合
     * @param cls      封装数据的 Class
     * @param response 响应
     */
    public static <T> void writeExcel(String name, List<T> data, Class<T> cls, HttpServletResponse response) {
        String mode = name.endsWith(EXCEL2003) ? EXCEL2003 : EXCEL2007; // 格式
        Workbook wb = getWorkbook(data, cls, mode); // 工作簿
        buildExcelDocument(name, wb, response); // 输出到浏览器
    }

    /**
     * 输出 Excel 到本地
     *
     * @param path     存储路径 d:/files/aa/a.xlsx 或 d:/files/aa/a.xls
     * @param data     数据集合
     * @param cls      封装数据的 Class
     * @param extra    区别重载方法，无实际意义
     * @param response 响应
     */
    public static <T> void writeExcel(String path, List<T> data, Class<T> cls, HttpServletResponse response, boolean extra) {
        String mode = path.endsWith(EXCEL2003) ? EXCEL2003 : EXCEL2007;
        Workbook wb = getWorkbook(data, cls, mode);
        buildExcelFile(path, wb);
    }

    /**
     * 输出 Excel 到浏览器
     * 此处 Map 需要使用 LinkedHashMap，否则顺序混乱
     *
     * @param list     数据集合
     * @param response 响应
     * @throws IOException
     */
    public static void writeExcel(List<Map<String, Object>> list, HttpServletResponse response) throws IOException {
        String tempPath = FileUtil.SYS_TEM_DIR + IdUtil.fastSimpleUUID() + ".xlsx";
        File file = new File(tempPath);
        BigExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getBigWriter(file);
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);
        SXSSFSheet sheet = (SXSSFSheet) writer.getSheet();
        //上面需要强转SXSSFSheet  不然没有trackAllColumnsForAutoSizing方法
        sheet.trackAllColumnsForAutoSizing();
        //列宽自适应
        writer.autoSizeColumnAll();
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");
        ServletOutputStream out = response.getOutputStream();
        // 终止后删除临时文件
        file.deleteOnExit();
        writer.flush(out, true);
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }

    /**
     * 导出 Excel 到本地
     *
     * @param path    存储路径 d:/files/aa/a.xls 或 d:/files/aa/a.xlsx
     * @param headers 表头
     * @param datas   数据集合
     */
    public static void writeExcel(String path, String[] headers, List<Object[]> datas) {
        Workbook workbook = path.endsWith(EXCEL2003) ? getWorkbook(EXCEL2003, headers, datas) : getWorkbook(EXCEL2007, headers, datas);
        if (workbook != null) {
            ByteArrayOutputStream byteArrayOutputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                workbook.write(byteArrayOutputStream);

                File file = new File(path);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(byteArrayOutputStream.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 导出 Excel 到浏览器
     *
     * @param name     文件名
     * @param headers  表头
     * @param datas    数据集合
     * @param response 响应
     */
    public static void writeExcel(String name, String[] headers, List<Object[]> datas, HttpServletResponse response) {
        Workbook workbook = name.endsWith(EXCEL2003) ? getWorkbook(EXCEL2003, headers, datas) : getWorkbook(EXCEL2007, headers, datas);
        if (workbook != null) {
            ByteArrayOutputStream byteArrayOutputStream = null;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                // 工作簿 写入到 输出流
                workbook.write(byteArrayOutputStream);

                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8"));

                OutputStream outputStream = response.getOutputStream();
                outputStream.write(byteArrayOutputStream.toByteArray());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 创建 Excel 工作簿
     *
     * @param data 数据集合
     * @param cls  封装数据的 Class
     * @param mode 格式 xls 或 xlsx
     * @return
     */
    private static <T> Workbook getWorkbook(List<T> data, Class<T> cls, String mode) {
        Field[] fields = cls.getDeclaredFields();
        // 筛选列 支持合并
        // 列标题 => 字段集合
        Map<String, List<Field>> columnsMap = Arrays.stream(fields)
                .filter(field -> { // 过滤
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null && annotation.col() > 0) {
                        field.setAccessible(true);
                        return true;
                    }
                    return false;
                }).sorted(Comparator.comparing(field -> { // 排序
                    int col = 0;
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null) {
                        col = annotation.col();
                    }
                    return col;
                })).collect(Collectors.groupingBy(field -> { // 按照列标题，字段分组
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    return annotation.value();
                }));

        // 列排序
        List<Map.Entry<String, List<Field>>> columns = new ArrayList<>(columnsMap.entrySet());
        Collections.sort(columns, new Comparator<Map.Entry<String, List<Field>>>() {

            @Override
            public int compare(Map.Entry<String, List<Field>> o1, Map.Entry<String, List<Field>> o2) {
                // 根据字段 col 排序
                int c1 = o1.getValue().get(0).getAnnotation(ExcelColumn.class).col();
                int c2 = o2.getValue().get(0).getAnnotation(ExcelColumn.class).col();
                return c1 - c2;
            }

        });

        Workbook wb = EXCEL2003.equals(mode) ? new HSSFWorkbook() : new XSSFWorkbook(); // 工作簿
        Sheet sheet = wb.createSheet("Sheet1"); // 表格
        AtomicInteger ai = new AtomicInteger(); // 行索引 自增

        // Excel 头部
        {
            Row row = sheet.createRow(ai.getAndIncrement());
            AtomicInteger aj = new AtomicInteger(); // 列索引 自增
            columns.forEach((entry) -> {
                Cell cell = row.createCell(aj.getAndIncrement()); // 单元格

                CellStyle cellStyle = wb.createCellStyle(); // 单元格 样式
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                Font font = wb.createFont();
                font.setBold(true);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(entry.getKey()); // 列标题
            });
        }

        // Excel 数据行
        if (CollectionUtils.isNotEmpty(data)) {
            data.forEach(t -> {
                Row row1 = sheet.createRow(ai.getAndIncrement());
                AtomicInteger aj = new AtomicInteger();
                columns.forEach((entry) -> {
                    Cell cell = row1.createCell(aj.getAndIncrement());
                    // 单元格赋值 支持合并
                    handleCell(t, cell, entry.getValue());
                });
            });
        }
        // 冻结窗格
        // 冻结列，冻结行，首列序号，首行序号
        wb.getSheet("Sheet1").createFreezePane(0, 1, 0, 1);
        return wb;
    }

    /**
     * 单元格赋值
     *
     * @param cell   单元格
     * @param fields 字段集合
     */
    private static <T> void handleCell(T t, Cell cell, List<Field> fields) {
        if (fields.size() == 1) {
            Field field = fields.get(0);
            Class<?> type = field.getType(); // 字段类型
            Object value = ""; // 字段值
            try {
                value = field.get(t);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (value != null) { // 单元格赋值
                if (type == Date.class) { // 日期列
                    ExcelDate da = field.getAnnotation(ExcelDate.class); // 日期格式
                    if (null != da) {
                        cell.setCellValue(DateUtil.getDate(da.value(), (Date) value));
                    } else {
                        cell.setCellValue(value.toString());
                    }
                } else {
                    cell.setCellValue(value.toString());
                }
            }
        } else {
            // 字段合并
            String split = fields.get(0).getAnnotation(ExcelGroup.class).value();
            StringBuffer value = new StringBuffer(); // 单元格值
            for (Field field : fields) {
                Class<?> type = field.getType();
                try {
                    Object v = field.get(t);
                    if (v != null) {
                        if (type == Date.class) { // 日期
                            ExcelDate da = field.getAnnotation(ExcelDate.class); // 日期格式
                            if (null != da) {
                                v = DateUtil.getDate(da.value(), (Date) v);
                            }
                        }
                    }
                    // 追加
                    value.append(v.toString()).append(split);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            cell.setCellValue(value.toString().substring(0, value.length() - 1)); // 单元格赋值
        }
    }


    /**
     * 输出 Excel 到浏览器
     *
     * @param name     文件名称
     * @param wb       工作簿
     * @param response 响应
     */
    private static void buildExcelDocument(String name, Workbook wb, HttpServletResponse response) {
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8"));
            response.flushBuffer();
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出 Excel 到本地
     *
     * @param path 存储路径
     * @param wb   工作簿
     */
    private static void buildExcelFile(String path, Workbook wb) {

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            wb.write(new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建 工作簿
     *
     * @param format  格式 xls 或 xlsx
     * @param headers 表头
     * @param datas   数据
     * @return
     */
    private static Workbook getWorkbook(String format, String[] headers, List<Object[]> datas) {
        Workbook workbook = EXCEL2003.equals(format) ? new HSSFWorkbook() : new XSSFWorkbook();

        Sheet sheet = workbook.createSheet();
        Row row = null;
        Cell cell = null;
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER_SELECTION);

        Font font = workbook.createFont();

        int line = 0, maxColumn = 0;
        // 创建表头
        if (headers != null && headers.length > 0) {
            row = sheet.createRow(line++);
            row.setHeightInPoints(23);
            font.setBold(true);
            font.setFontHeightInPoints((short) 13);
            style.setFont(font);

            maxColumn = headers.length;
            for (int i = 0; i < maxColumn; i++) {
                cell = row.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(style);
            }
        }
        // 渲染数据
        if (datas != null && datas.size() > 0) {
            for (int index = 0, size = datas.size(); index < size; index++) {
                Object[] data = datas.get(index);
                if (data != null && data.length > 0) {
                    row = sheet.createRow(line++);
                    row.setHeightInPoints(20);

                    int length = data.length;
                    if (length > maxColumn) {
                        maxColumn = length;
                    }

                    for (int i = 0; i < length; i++) {
                        cell = row.createCell(i);
                        cell.setCellValue(data[i] == null ? null : data[i].toString());
                    }
                }
            }
        }

        // 列自适应
        for (int i = 0; i < maxColumn; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }
}

package com.dongzz.quick.common.utils;

import cn.hutool.core.util.IdUtil;
import com.dongzz.quick.common.domain.UploadVo;
import com.dongzz.quick.common.exception.ApplicationException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件处理 工具类
 */
public class FileUtil extends cn.hutool.core.io.FileUtil {

    public static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 系统临时目录
     * <br>
     * windows 包含路径分割符，但Linux 不包含
     * 在windows \\==\ 前提下，为安全起见 同意拼装 路径分割符
     * <pre>
     *    java.io.tmpdir
     *    windows : C:\Users\xxx\AppData\Local\Temp\\
     *    linux: /temp
     * </pre>
     */
    public static final String SYS_TEM_DIR = System.getProperty("java.io.tmpdir") + File.separator;

    /**
     * GB的计算常量
     */
    private static final int GB = 1024 * 1024 * 1024;
    /**
     * MB的计算常量
     */
    private static final int MB = 1024 * 1024;
    /**
     * KB的计算常量
     */
    private static final int KB = 1024;

    /**
     * 格式化小数
     */
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    /**
     * 文件类型
     */
    public static final String IMAGE = "image";
    public static final String TXT = "doc";
    public static final String MUSIC = "audio";
    public static final String VIDEO = "video";
    public static final String OTHER = "other";


    /**
     * MultipartFile转File
     */
    public static File toFile(MultipartFile multipartFile) {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = "." + getExtensionName(fileName);
        File file = null;
        try {
            // 用uuid作为文件名，防止生成的临时文件重复
            file = new File(SYS_TEM_DIR + IdUtil.simpleUUID() + prefix);
            // MultipartFile to File
            multipartFile.transferTo(file);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return file;
    }

    /**
     * 获取文件扩展名，不带 .
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 获取不带扩展名的文件名
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 文件大小转换
     */
    public static String getSize(long size) {
        String resultSize;
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = DF.format(size / (float) GB) + "GB   ";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = DF.format(size / (float) MB) + "MB   ";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = DF.format(size / (float) KB) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }

    /**
     * inputStream 转 File
     */
    static File inputStreamToFile(InputStream ins, String name) {
        File file = new File(SYS_TEM_DIR + name);
        if (file.exists()) {
            return file;
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead;
            int len = 8192;
            byte[] buffer = new byte[len];
            while ((bytesRead = ins.read(buffer, 0, len)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(os);
            CloseUtil.close(ins);
        }
        return file;
    }

    /**
     * 获取文件类型
     *
     * @param type 类型
     * @return
     */
    public static String getFileType(String type) {
        String documents = "txt doc pdf ppt pps xlsx xls docx";
        String music = "mp3 wav wma mpa ram ra aac aif m4a";
        String video = "avi mpg mpe mpeg asf wmv mov qt rm mp4 flv m4v webm ogv ogg";
        String image = "bmp dib pcp dif wmf gif jpg tif eps psd cdr iff tga pcd mpt png jpeg";
        if (image.contains(type)) {
            return IMAGE;
        } else if (documents.contains(type)) {
            return TXT;
        } else if (music.contains(type)) {
            return MUSIC;
        } else if (video.contains(type)) {
            return VIDEO;
        } else {
            return OTHER;
        }
    }

    /**
     * 限制文件大小
     *
     * @param maxSize 最大支持
     * @param size    实际大小
     */
    public static void checkSize(long maxSize, long size) {
        // 1M
        int len = 1024 * 1024;
        if (size > (maxSize * len)) {
            throw new ApplicationException("文件超出规定大小");
        }
    }

    /**
     * 判断两个文件是否相同
     */
    public static boolean check(File file1, File file2) {
        String img1Md5 = getMd5(file1);
        String img2Md5 = getMd5(file2);
        if (img1Md5 != null) {
            return img1Md5.equals(img2Md5);
        }
        return false;
    }

    /**
     * 判断两个文件是否相同
     */
    public static boolean check(String file1Md5, String file2Md5) {
        return file1Md5.equals(file2Md5);
    }

    /**
     * File 转化为 字节数组
     *
     * @param file 文件
     * @return
     */
    private static byte[] getByte(File file) {
        // 得到文件长度
        byte[] b = new byte[(int) file.length()];
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            try {
                System.out.println(in.read(b));
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        } finally {
            CloseUtil.close(in);
        }
        return b;
    }

    private static String getMd5(byte[] bytes) {
        // 16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(bytes);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            // 移位 输出字符串
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取文件MD5值
     *
     * @param file 文件
     * @return
     */
    public static String getMd5(File file) {
        return getMd5(getByte(file));
    }

    /**
     * 获取文件MD5值
     *
     * @param is 文件流
     * @return
     */
    public static String getMd5(InputStream is) {
        try {
            return DigestUtils.md5Hex(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取日期路径
     *
     * @return 日期路径 \2022\04\18\
     */
    public static String getPath() {
        String date = DateUtil.getDate("yyyy-MM-dd", new Date());
        return File.separator + date.replace("-", File.separator) + File.separator;
    }

    /**
     * 上传
     *
     * @param file     文件
     * @param filePath 存储根路径
     * @return
     */
    public static UploadVo upload(MultipartFile file, String filePath) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssS");
        String name = getFileNameNoEx(file.getOriginalFilename());
        String suffix = getExtensionName(file.getOriginalFilename());
        String nowStr = "-" + format.format(date);
        try {
            UploadVo vo = new UploadVo();
            String fileName = name + nowStr + "." + suffix;
            String path = filePath + fileName;
            // getCanonicalFile 可解析正确各种路径
            File dest = new File(path).getCanonicalFile();
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                if (!dest.getParentFile().mkdirs()) {
                    logger.debug("上传存储目录创建失败！");
                }
            }
            // 记录上传信息
            vo.setFileMd5(getMd5(file.getInputStream()));
            vo.setFileName(file.getOriginalFilename());
            vo.setCacheName(fileName);
            vo.setCachePath(path);
            vo.setContentType(file.getContentType());
            vo.setFileExt("." + suffix);
            vo.setSize(file.getSize());
            vo.setFileSize(getSize(file.getSize()));
            vo.setFileType(getFileType(suffix));
            // 执行上传
            file.transferTo(dest);
            return vo;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * spring mvc 文件下载
     *
     * @param path     存储路径
     * @param fileName 文件名
     * @return
     */
    public static ResponseEntity<byte[]> download(String path, String fileName) {
        // 下载响应体
        ResponseEntity<byte[]> entity = null;
        try {
            // 读取数据
            File file = new File(path);
            byte[] body = null;
            InputStream is = new FileInputStream(file);
            body = new byte[is.available()];
            is.read(body);
            // 响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attchement;filename=" + fileName);
            // 状态码
            HttpStatus statusCode = HttpStatus.OK;

            entity = new ResponseEntity<>(body, headers, statusCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entity;
    }

    /**
     * 通用 文件下载
     *
     * @param path     存储路径
     * @param fileName 文件名
     * @param response 响应
     */
    public static void download(String path, String fileName, HttpServletResponse response) {
        // 响应头和文件名称
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        // 记录已完成的下载的数据量，单位是byte
        // long downloadedLength = 0l;
        try {
            InputStream is = new FileInputStream(path);
            OutputStream os = response.getOutputStream(); // 响应输出流
            byte[] b = new byte[2048]; // 缓冲数组
            int length;
            while ((length = is.read(b)) > 0) {
                os.write(b, 0, length);
                // downloadedLength += b.length;
            }
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通用 文件下载
     *
     * @param is
     * @param fileName
     * @param response
     */
    public static void download(InputStream is, String fileName, HttpServletResponse response) {
        // 响应头和文件名称
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        // 记录已完成的下载的数据量，单位是byte
        // long downloadedLength = 0l;
        try {
            OutputStream os = response.getOutputStream(); // 响应输出流
            byte[] b = new byte[2048]; // 缓冲数组
            int length;
            while ((length = is.read(b)) > 0) {
                os.write(b, 0, length);
                // downloadedLength += b.length;
            }
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param path 存储路径
     * @return
     */
    public static boolean delete(String path) {
        File file = new File(path);
        if (file.exists()) {
            boolean flag = file.delete();
            // 清除空目录
            if (flag) {
                File[] files = file.getParentFile().listFiles();
                if (files == null || files.length == 0) {
                    file.getParentFile().delete();
                }
            }
            return flag;
        }
        return false;
    }

    /**
     * 将文本写入文件
     *
     * @param value 文本
     * @param path  文件路径
     */
    public static void setText(String value, String path) {
        FileWriter writer = null;
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            writer = new FileWriter(file);
            writer.write(value);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文件内容
     *
     * @param path 文件路径
     * @return
     */
    public static String getText(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        try {
            return getText(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 读取文件内容
     *
     * @param is 文件流
     * @return
     */
    public static String getText(InputStream is) {
        InputStreamReader isr = null;
        BufferedReader bufferedReader = null;
        try {
            isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(isr);
            StringBuilder builder = new StringBuilder();
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                string = string + "\n";
                builder.append(string);
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}

package com.dongzz.quick.tools.utils;

import cn.hutool.core.util.IdUtil;
import com.dongzz.quick.common.domain.UploadVo;
import com.dongzz.quick.common.utils.FileUtil;
import com.dongzz.quick.tools.domain.ToolCosConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 腾讯COS存储 工具类
 */
public class CosUtil {

    /**
     * 简单上传
     *
     * @param file   文件
     * @param config 配置
     * @return
     */
    public static UploadVo upload(MultipartFile file, ToolCosConfig config) {
        // 本地临时存储
        File temp = FileUtil.toFile(file);
        COSClient client = createClient(config);
        String bucketName = config.bucket();
        // 信息
        String fileOriginalName = file.getOriginalFilename();
        String fileExtName = FileUtil.getExtensionName(fileOriginalName);
        long size = file.getSize();
        String uuid = IdUtil.simpleUUID();
        String fileType = FileUtil.getFileType(fileExtName);
        String dateDir = FileUtil.getPath();
        String key = fileType + dateDir + uuid + "." + fileExtName; // 存储标记
        key = key.replace("\\", "/");
        String url = config.getDomain() + "/" + key;
        PutObjectRequest request = new PutObjectRequest(bucketName, key, temp);
        request.setStorageClass(StorageClass.Standard_IA); // 设置存储类型 默认是标准(Standard)，低频(standard_ia)
        try {
            // 上传
            PutObjectResult response = client.putObject(request);
            String etag = response.getETag();
            String crc64 = response.getCrc64Ecma();

            UploadVo vo = new UploadVo();
            vo.setFileUuid(uuid);
            vo.setFileName(fileOriginalName);
            vo.setCacheName(uuid + "." + fileExtName);
            vo.setCachePath(key);
            vo.setFileUrl(url);
            vo.setContentType(file.getContentType());
            vo.setFileType(fileType);
            vo.setSize(size);
            vo.setFileSize(FileUtil.getSize(size));
            vo.setFileExt(fileExtName);
            return vo;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(client);
        }
        return null;
    }

    /**
     * 删除
     *
     * @param config 配置
     * @param key    存储标记
     */
    public static boolean delete(ToolCosConfig config, String key) {
        COSClient client = createClient(config);
        try {
            client.deleteObject(config.bucket(), key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            close(client);
        }
        return true;
    }


    /**
     * 下载
     *
     * @param config   配置
     * @param key      存储标记
     * @param response 响应
     */
    public static void download(ToolCosConfig config, String key, HttpServletResponse response) {
        COSClient client = createClient(config);
        try {
            GetObjectRequest request = new GetObjectRequest(config.bucket(), key);
            COSObject cosObject = client.getObject(request);
            COSObjectInputStream cis = cosObject.getObjectContent();
            String fileName = FileUtil.getFileNameNoEx(key);
            FileUtil.download(cis, fileName, response); // 下载
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(client);
        }

    }

    /**
     * 创建客户端连接
     *
     * @param config 配置
     * @return
     */
    public static COSClient createClient(ToolCosConfig config) {
        COSCredentials credentials = new BasicCOSCredentials(config.getSecretId(), config.getSecretKey());
        ClientConfig cc = new ClientConfig(new Region(config.getRegion()));
        return new COSClient(credentials, cc);
    }


    /**
     * 关闭连接 释放资源
     *
     * @param client 客户端
     */
    public static void close(COSClient client) {
        if (null != client) {
            client.shutdown();
        }
    }
}

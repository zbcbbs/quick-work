package com.dongzz.quick.common.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 封装上传结果
 */
@Getter
@Setter
public class UploadVo implements Serializable {

    private String fileMd5; // MD5
    private String fileUuid; // uuid
    private String fileName; // 源名称
    private String cacheName; // 存储名称
    private String cachePath; // 存储路径
    private String contentType; // 内容类型
    private Long size; // 大小
    private String fileSize; // 大小 带单位
    private String fileType; // 类型
    private String fileExt; // 扩展名
    private String fileUrl; // 访问路径
    private String filePath; // 相对路径

}

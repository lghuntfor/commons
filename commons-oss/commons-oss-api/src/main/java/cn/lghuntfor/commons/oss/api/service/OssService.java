package cn.lghuntfor.commons.oss.api.service;

import cn.lghuntfor.commons.oss.api.common.OssResult;
import cn.lghuntfor.commons.oss.api.common.Position;

import java.io.InputStream;

/**
 * Oss对象存储服务接口
 * @author lghuntfor
 * @date 2020/10/12
 */
public interface OssService {

    /**
     * 通过输入流存储文件
     * @author liaogang
     * @date 2020/10/13
     * @param fileName 文件名
     * @param in 输入流
     * @return cn.lghuntfor.commons.oss.api.common.OssResult
     */
    OssResult storeStreamFile(String fileName, InputStream in);

    /**
     * 通过输入流存储文件, 指定是否需要保留原文件名
     * @author liaogang
     * @date 2020/10/13
     * @param fileName 文件名
     * @param keepOriginName 是否保留原文件名
     * @param in 输入流
     * @return cn.lghuntfor.commons.oss.api.common.OssResult
     */
    OssResult storeStreamFile(String fileName, boolean keepOriginName, InputStream in);

    /**
     * 通过输入流存储文件, 自定义文件存储路径
     * @author liaogang
     * @date 2020/10/13
     * @param storePath 在oss中的存储路径
     * @param fileName 文件名
     * @param in 输入流
     * @return cn.lghuntfor.commons.oss.api.common.OssResult
     */
    OssResult storeStreamFile(String storePath, String fileName, InputStream in);

    /**
     * 通过输入流存储文件, 自定义文件存储路径和bucket
     * @author liaogang
     * @date 2020/10/13
     * @param bucketName 文件桶名
     * @param storePath 在oss中的存储路径
     * @param fileName 文件名
     * @param in 输入流
     * @return cn.lghuntfor.commons.oss.api.common.OssResult
     */
    OssResult storeStreamFile(String bucketName, String storePath, String fileName, InputStream in);

    /**
     * 通过本地文件路径存储文件
     * @author liaogang
     * @date 2020/10/13
     * @param filePath 本地文件全路径
     * @return cn.lghuntfor.commons.oss.api.common.OssResult
     */
    OssResult storeFile(String filePath);

    /**
     * 通过本地文件路径存储文件, 自定义文件存储路径
     * @author liaogang
     * @date 2020/10/13
     * @param keepOriginName 在oss中的存储路径
     * @param filePath 本地文件全路径
     * @return cn.lghuntfor.commons.oss.api.common.OssResult
     */
    OssResult storeFile(boolean keepOriginName, String filePath);

    /**
     * 通过本地文件路径存储文件, 自定义文件存储路径和bucket
     * @author liaogang
     * @date 2020/10/13
     * @param bucketName 文件桶名
     * @param storePath 在oss中的存储路径
     * @param filePath 本地文件全路径
     * @return cn.lghuntfor.commons.oss.api.common.OssResult
     */
    OssResult storeFile(String bucketName, String storePath, String filePath);

    /**
     * 通过输入流存储图片, 自定义文件存储路径和bucket
     * @author liaogang
     * @date 2020/10/13
     * @param fileName 文件名
     * @param watermark 图片水印地址, 为空则不添加水印
     * @param position 水印图片位置
     * @param in 输入流
     * @return cn.lghuntfor.commons.oss.api.common.OssResult
     */
    OssResult storeStreamImage(String fileName, String watermark, Position position, InputStream in);

    /**
     * 通过本地文件路径存储图片, 自定义文件存储路径和bucket
     * @author liaogang
     * @date 2020/10/13
     * @param watermark 图片水印地址, 为空则不添加水印
     * @param position 水印图片位置
     * @param filePath 本地文件全路径
     * @return cn.lghuntfor.commons.oss.api.common.OssResult
     */
    OssResult storeImage(String watermark, Position position, String filePath);

    /**
     * 获取存储系统中指定的文件流
     * @author liaogang
     * @date 2020/10/13
     * @param bucketName
     * @param storePath
     * @return java.io.InputStream
     */
    InputStream getFileStream(String bucketName, String storePath);
}

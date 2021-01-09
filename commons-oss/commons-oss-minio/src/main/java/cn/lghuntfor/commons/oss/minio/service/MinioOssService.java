package cn.lghuntfor.commons.oss.minio.service;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.lghuntfor.commons.common.constants.ReturnMsg;
import cn.lghuntfor.commons.oss.api.service.OssService;
import cn.lghuntfor.commons.oss.api.common.OssResult;
import cn.lghuntfor.commons.oss.api.common.OssUtils;
import cn.lghuntfor.commons.oss.api.common.Position;
import cn.lghuntfor.commons.oss.api.config.OssProperties;
import cn.lghuntfor.commons.oss.api.exception.OssException;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

import  io.minio.ObjectWriteArgs;

/**
 * minio文件服务
 * @author lghuntfor
 * @date 2020/10/12
 */
@Slf4j
public class MinioOssService implements OssService {

    private final int MULTIPART_SIZE = ObjectWriteArgs.MIN_MULTIPART_SIZE;

    @Autowired
    private MinioClient minioClient;
    @Autowired
    private OssProperties ossProperties;

    @Override
    public OssResult storeStreamFile(String fileName, InputStream in) {
        return storeStreamFile(fileName, false, in);
    }

    @Override
    public OssResult storeStreamFile(String fileName, boolean keepOriginName, InputStream in) {
        String storePath = OssUtils.getStorePath(fileName, keepOriginName);
        return storeStreamFile(ossProperties.getBucketName(), storePath, fileName, in);
    }


    @Override
    public OssResult storeStreamFile(String storePath, String fileName, InputStream in) {
        return storeStreamFile(ossProperties.getBucketName(), storePath, fileName, in);
    }

    @Override
    public OssResult storeStreamFile(String bucketName, String storePath, String fileName, InputStream in) {
        try {
            if (storePath.startsWith(OssUtils.SLASH)) {
                storePath = storePath.substring(1);
            }
            checkBucketExist(bucketName);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(storePath)
                    .contentType(OssUtils.getContentType(fileName))
                    .stream(in, in.available(), MULTIPART_SIZE)
                    .build());
        } catch (Exception e) {
            throw new OssException(ReturnMsg.SERVER_ERROR.getCode(), "文件上传失败", e);
        }
        return getOssResult(bucketName, storePath);
    }

    @Override
    public OssResult storeFile(String filePath) {
        return storeFile(ossProperties.getBucketName(), OssUtils.getStorePath(filePath, false), filePath);
    }

    @Override
    public OssResult storeFile(boolean keepOriginName, String filePath) {
        return storeFile(ossProperties.getBucketName(), OssUtils.getStorePath(filePath, keepOriginName), filePath);
    }

    @Override
    public OssResult storeFile(String bucketName, String storePath, String filePath) {
        try {
            checkBucketExist(bucketName);
            minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(storePath)
                    .contentType(OssUtils.getContentType(filePath))
                    .filename(filePath)
                    .build());
        } catch (Exception e) {
            throw new OssException(ReturnMsg.SERVER_ERROR.getCode(), "文件上传失败", e);
        }
        return getOssResult(bucketName, storePath);
    }

    @Override
    public OssResult storeStreamImage(String fileName, String watermark, Position position, InputStream in) {
        if (StrUtil.isNotBlank(watermark)) {
            FileOutputStream out = null;
            File file = null;
            try {
                /** 获取水印图 */
                boolean isUrl = watermark.startsWith("http");
                BufferedImage watermarkImage = isUrl ? ImgUtil.read(new URL(watermark)) : ImgUtil.read(watermark);

                /** 添加水印之后的临时图片路径 */
                String tmpWatermarkFilePath = "/tmp/images/" + IdUtil.fastSimpleUUID() + "-" +fileName;
                file = new File(tmpWatermarkFilePath);

                BufferedImage sourceImage = ImgUtil.read(in);
                /** 获取水印图的坐标点 */
                int[] xy = OssUtils.getXY(sourceImage, watermarkImage, position);
                out = new FileOutputStream(tmpWatermarkFilePath);
                /** 给源图片添加水印 */
                ImgUtil.pressImage(sourceImage, out, watermarkImage, xy[0], xy[1], 0.5f);
                return storeFile(tmpWatermarkFilePath);
            } catch (Exception e) {
                throw new OssException(ReturnMsg.SERVER_ERROR.getCode(), "图片上传失败", e);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (file != null && file.exists()) {
                    file.delete();
                }
             }
        }

        return storeStreamFile(fileName, in);
    }

    @Override
    public OssResult storeImage(String watermark, Position position, String filePath) {
        FileInputStream in = null;
        try {
            String fileName = StrUtil.sub(filePath, filePath.lastIndexOf(OssUtils.FILE_SEPARATOR) + 1, filePath.length());
            in = new FileInputStream(filePath);
            return storeStreamImage(fileName, watermark, position, in);
        } catch (Exception e) {
            throw new OssException(ReturnMsg.SERVER_ERROR.getCode(), "图片上传失败", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public InputStream getFileStream(String bucketName, String storePath) {
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(storePath).build());
        } catch (Exception e) {
            throw new OssException(ReturnMsg.SERVER_ERROR.getCode(), "文件获取失败", e);
        }
    }

    /**
     * 获取文件上传结果信息
     * @param bucketName
     * @param storePath
     */
    private OssResult getOssResult(String bucketName, String storePath) {
        return new OssResult("/" + bucketName + "/" + storePath
                , ossProperties.getCdn() + "/" + bucketName + "/" + storePath);
    }

    /**
     * 检查bucket, 不存在则创建
     * @param bucketName
     */
    private void checkBucketExist(String bucketName) {
        try {
            boolean bucketExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new OssException(ReturnMsg.SERVER_ERROR.getCode(), "文件桶操作失败", e);
        }
    }
}

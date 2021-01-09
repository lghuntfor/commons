package cn.lghuntfor.commons.oss.api.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

/**
 * Oss 文件工具类
 * @author liaogang
 * @date 2020/10/13
 */
public class OssUtils {

    /** 斜线 */
    public static final String SLASH = "/";

    /** 当前系统文件目录分隔符 */
    public static final String FILE_SEPARATOR = File.separator;



    /**
     * 根据“文件名的后缀”获取文件内容类型（而非根据File.getContentType()读取的文件类型）
     * @author liaogang
     * @date 2020/10/13
     * @param fileName
     * @return java.lang.String
     */
    public static String getContentType(String fileName) {
        String contentType = "application/octet-stream";
        if (fileName.lastIndexOf(".") < 0) {
            return contentType;
        }
        fileName = fileName.toLowerCase();
        fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (fileName.equals("html") || fileName.equals("htm") || fileName.equals("shtml")) {
            contentType = "text/html";
        } else if (fileName.equals("apk")) {
            contentType = "application/vnd.android.package-archive";
        } else if (fileName.equals("sis")) {
            contentType = "application/vnd.symbian.install";
        } else if (fileName.equals("sisx")) {
            contentType = "application/vnd.symbian.install";
        } else if (fileName.equals("exe")) {
            contentType = "application/x-msdownload";
        } else if (fileName.equals("msi")) {
            contentType = "application/x-msdownload";
        } else if (fileName.equals("css")) {
            contentType = "text/css";
        } else if (fileName.equals("xml")) {
            contentType = "text/xml";
        } else if (fileName.equals("bpmn")) {
            contentType = "text/xml";
        } else if (fileName.equals("gif")) {
            contentType = "image/gif";
        } else if (fileName.equals("jpeg") || fileName.equals("jpg")) {
            contentType = "image/jpeg";
        } else if (fileName.equals("js")) {
            contentType = "application/x-javascript";
        } else if (fileName.equals("atom")) {
            contentType = "application/atom+xml";
        } else if (fileName.equals("rss")) {
            contentType = "application/rss+xml";
        } else if (fileName.equals("mml")) {
            contentType = "text/mathml";
        } else if (fileName.equals("txt")) {
            contentType = "text/plain";
        } else if (fileName.equals("jad")) {
            contentType = "text/vnd.sun.j2me.app-descriptor";
        } else if (fileName.equals("wml")) {
            contentType = "text/vnd.wap.wml";
        } else if (fileName.equals("htc")) {
            contentType = "text/x-component";
        } else if (fileName.equals("png")) {
            contentType = "image/png";
        } else if (fileName.equals("tif") || fileName.equals("tiff")) {
            contentType = "image/tiff";
        } else if (fileName.equals("wbmp")) {
            contentType = "image/vnd.wap.wbmp";
        } else if (fileName.equals("ico")) {
            contentType = "image/x-icon";
        } else if (fileName.equals("jng")) {
            contentType = "image/x-jng";
        } else if (fileName.equals("bmp")) {
            contentType = "image/x-ms-bmp";
        } else if (fileName.equals("svg")) {
            contentType = "image/svg+xml";
        } else if (fileName.equals("jar") || fileName.equals("var")
                || fileName.equals("ear")) {
            contentType = "application/java-archive";
        } else if (fileName.equals("doc")) {
            contentType = "application/msword";
        } else if (fileName.equals("pdf")) {
            contentType = "application/pdf";
        } else if (fileName.equals("rtf")) {
            contentType = "application/rtf";
        } else if (fileName.equals("xls")) {
            contentType = "application/vnd.ms-excel";
        } else if (fileName.equals("ppt")) {
            contentType = "application/vnd.ms-powerpoint";
        } else if (fileName.equals("7z")) {
            contentType = "application/x-7z-compressed";
        } else if (fileName.equals("rar")) {
            contentType = "application/x-rar-compressed";
        } else if (fileName.equals("swf")) {
            contentType = "application/x-shockwave-flash";
        } else if (fileName.equals("rpm")) {
            contentType = "application/x-redhat-package-manager";
        } else if (fileName.equals("der") || fileName.equals("pem")
                || fileName.equals("crt")) {
            contentType = "application/x-x509-ca-cert";
        } else if (fileName.equals("xhtml")) {
            contentType = "application/xhtml+xml";
        } else if (fileName.equals("zip")) {
            contentType = "application/zip";
        } else if (fileName.equals("mid") || fileName.equals("midi")
                || fileName.equals("kar")) {
            contentType = "audio/midi";
        } else if (fileName.equals("mp3")) {
            contentType = "audio/mpeg";
        } else if (fileName.equals("ogg")) {
            contentType = "audio/ogg";
        } else if (fileName.equals("m4a")) {
            contentType = "audio/x-m4a";
        } else if (fileName.equals("ra")) {
            contentType = "audio/x-realaudio";
        } else if (fileName.equals("3gpp")
                || fileName.equals("3gp")) {
            contentType = "video/3gpp";
        } else if (fileName.equals("mp4")) {
            contentType = "video/mp4";
        } else if (fileName.equals("mpeg")
                || fileName.equals("mpg")) {
            contentType = "video/mpeg";
        } else if (fileName.equals("mov")) {
            contentType = "video/quicktime";
        } else if (fileName.equals("flv")) {
            contentType = "video/x-flv";
        } else if (fileName.equals("m4v")) {
            contentType = "video/x-m4v";
        } else if (fileName.equals("mng")) {
            contentType = "video/x-mng";
        } else if (fileName.equals("asx") || fileName.equals("asf")) {
            contentType = "video/x-ms-asf";
        } else if (fileName.equals("wmv")) {
            contentType = "video/x-ms-wmv";
        } else if (fileName.equals("avi")) {
            contentType = "video/x-msvideo";
        }
        return contentType;
    }

    /**
     * 通过文件名称获取类型
     * @author liaogang
     * @date 2020/10/13
     * @param fileName
     * @return java.lang.String
     */
    public static String getFileType(String fileName){
        return getType(getContentType(fileName));
    }

    /**
     * 通过contentType获取类型
     * @author liaogang
     * @date 2020/10/13
     * @param contentType
     * @return java.lang.String
     */
    public static String getType(String contentType){
        String firstType = "";
        if(contentType.contains(SLASH)){
            firstType = contentType.substring(0, contentType.indexOf(SLASH, 0));
        }
        switch (firstType) {
            case "audio":
                return "audio";
            case "video":
                return "video";
            case "image":
                return "image";
            case "text":
                return "text";
            default:
                return "file";
        }
    }

    /**
     * 获取文件存储路径
     * 按 /文件类型/yyyy/MM/dd/文件名
     * @param fileName
     * @param keepOriginName
     */
    public static String getStorePath(String fileName, boolean keepOriginName) {
        fileName = StrUtil.sub(fileName, fileName.lastIndexOf(FILE_SEPARATOR) + 1, fileName.length());
        String fileSuffix = StrUtil.sub(fileName, fileName.lastIndexOf("."), fileName.length());
        String prefix = OssUtils.getFileType(fileName);
        String storePath = new StringBuilder(prefix)
                .append(DateUtil.format(new Date(), "/yyyy/MM/dd/"))
                .append((keepOriginName ? fileName : IdUtil.fastSimpleUUID() + fileSuffix))
                .toString();
        return storePath;
    }

    /**
     * 获取图片水印坐标点
     * @param sourceImage
     * @param watermarkImage
     * @return
     */
    public static int[] getXY(BufferedImage sourceImage, BufferedImage watermarkImage, Position position) {
        int[] xy = new int[2];
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        int w = watermarkImage.getWidth();
        int h = watermarkImage.getHeight();

        int x = 0;
        int y = 0;

        switch (position) {
            case RIGHT_TOP:
                x = (width - w) / 2;
                y = -(height - h) / 2;
                break;
            case RIGHT_BOTTOM:
                x = (width - w) / 2;
                y = (height - h) / 2;
                break;
            case LEFT_TOP:
                x = -(width - w) / 2;
                y = -(height - h) / 2;
                break;
            case LEFT_BOTTOM:
                x = -(width - w) / 2;
                y = (height - h) / 2;
                break;
        }

        xy[0] = x;
        xy[1] = y;

        return xy;
    }
}

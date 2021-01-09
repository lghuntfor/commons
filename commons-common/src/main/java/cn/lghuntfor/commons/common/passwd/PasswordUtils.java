package cn.lghuntfor.commons.common.passwd;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.digest.MD5;

import java.io.UnsupportedEncodingException;

/**
 * 用户密码工具类
 * @author liaogang
 * @date 2020/9/24 18:37
 */
public class PasswordUtils {

    /**
     * 密码加密
     * @author liaogang
     * @date 2020/9/24
     * @param plaintext
     * @param salt
     * @return java.lang.String
     */
    public static String encodePassword(String plaintext, String salt) {
        return encodePassword(plaintext, salt, 20);
    }

    /**
     * 密码加密, 并指定摘要次数
     * @author liaogang
     * @date 2020/9/24
     * @param plaintext
     * @param salt
     * @param digestCount
     * @return java.lang.String
     */
    public static String encodePassword(String plaintext, String salt, int digestCount) {
        try {
            MD5 md5 = new MD5(salt.getBytes(CharsetUtil.UTF_8), 0, digestCount);
            return md5.digestHex(plaintext);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 密码验证
     * @author liaogang
     * @date 2020/9/24
     * @param ciphertext
     * @param plaintext
     * @param salt
     * @return boolean
     */
    public static boolean validatePassword(String ciphertext, String plaintext, String salt) {
        return ciphertext.equals(encodePassword(plaintext, salt));
    }

}

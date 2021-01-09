package cn.lghuntfor.commons.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;

import java.io.Serializable;

/**
 * 请添加描述
 *
 * @author liaogang
 * @date 2020/9/1 17:46
 */
@Data
public class VerifyR implements Serializable {

    /** 是否验证通过 */
    private Boolean verifyPass;

    /** 是否已过期 */
    private Boolean expired;

    /** 是否将要过期 */
    private Boolean willExpire;

    private DecodedJWT decodedJWT;

    private String result;

}

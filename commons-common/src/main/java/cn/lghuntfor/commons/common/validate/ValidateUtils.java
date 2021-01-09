package cn.lghuntfor.commons.common.validate;

import cn.hutool.core.collection.CollUtil;

import javax.validation.*;
import java.util.Set;

/**
 * validate验证工具类
 * @author liaogang
 * @date 2020/11/18 10:24
 */
public class ValidateUtils {

    /** Validator对象单例 */
    private static Validator VALIDATOR = null;

    /**
     * 获取单例Validator对象
     * @author liaogang
     * @date 2020/11/18
     * @return javax.validation.Validator
     */
    public static Validator getValidator() {
        if (VALIDATOR == null) {
            synchronized (ValidateUtils.class) {
                if (VALIDATOR == null) {
                    VALIDATOR = createValidator();
                }
            }
        }
        return VALIDATOR;
    }

    /**
     * 创建新的Validator
     * @author liaogang
     * @date 2020/11/18
     * @return javax.validation.Validator
     */
    public static Validator createValidator() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        return validator;
    }

    /**
     * 针对object进行验证
     * 返回验证不能过的信息
     * @author liaogang
     * @date 2020/11/18
     * @param obj
     * @return java.util.Set<javax.validation.ConstraintViolation<T>>
     */
    public static <T> Set<ConstraintViolation<T>> validate(T obj) {
        Validator validator = getValidator();
        Set<ConstraintViolation<T>> validateSet = validator.validate(obj);
        return validateSet;
    }

    /**
     * 针对object进行验证
     * 如果验证不通过, 则抛出ConstraintViolationException异常
     * @author liaogang
     * @date 2020/11/18
     * @param obj
     * @return java.util.Set<javax.validation.ConstraintViolation<T>>
     */
    public static <T> void validateAndThrow(T obj) {
        Validator validator = getValidator();
        Set<ConstraintViolation<T>> validateSet = validator.validate(obj);
        if (CollUtil.isNotEmpty(validateSet)) {
            throw new ConstraintViolationException(validateSet);
        }
    }

}

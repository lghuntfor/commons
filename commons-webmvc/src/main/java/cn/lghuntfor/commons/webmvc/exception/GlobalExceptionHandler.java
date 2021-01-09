package cn.lghuntfor.commons.webmvc.exception;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import cn.lghuntfor.commons.common.constants.ReturnMsg;
import cn.lghuntfor.commons.common.exception.CommonException;
import cn.lghuntfor.commons.common.result.ReturnData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 全局异常处理
 * @author liaogang
 * @date 2020/9/10 08:49
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /** Validation中验证信息的提取 */
    private static final Pattern REGEX_VALIDATION_MSG = Pattern.compile(".*messageTemplate='(.+)'.*");

    /**
     * 处理CommonException
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {CommonException.class})
    @ResponseBody
    public ReturnData<Object> handleCommonException(CommonException ex, HttpServletRequest request){
        log.error(this.getRequestInfo(request), ex);
        return new ReturnData(ex.getExceptionCode(), ex.getExceptionMsg());
    }

    /**
     * 处理ValidationException
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {ValidationException.class})
    @ResponseBody
    public ReturnData<Object> handleValidationException(ValidationException ex, HttpServletRequest request){
        StringBuilder errorMsg = new StringBuilder();
        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException exs = (ConstraintViolationException) ex;
            Set<ConstraintViolation<?>> constraintViolations = exs.getConstraintViolations();
            for(ConstraintViolation item : constraintViolations){
                errorMsg.append(item.getMessage());
                errorMsg.append("-");
            }
            errorMsg.deleteCharAt(errorMsg.length() - 1);
        } else {
            /** 针对ValidationException中的检验异常信息提取 */
            String msg = ReUtil.extractMulti(REGEX_VALIDATION_MSG, ex.getMessage(), "$1");
            if (StrUtil.isNotBlank(msg)) {
                errorMsg.append(msg);
            } else {
                errorMsg.append(ReturnMsg.BAD_REQUEST.getMsg());
            }
        }
        log.error(this.getRequestInfo(request), ex);
        return new ReturnData(ReturnMsg.BAD_REQUEST.getCode(), errorMsg.toString());
    }

    /**
     * 针对ConstraintViolationException异常的检验
     * 主要dubbo接口验证中
     * @author liaogang
     * @date 2020/9/17
     * @param ex
     * @param request
     * @return cn.lghuntfor.commons.common.result.ReturnData<java.lang.Object>
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseBody
    public ReturnData<Object> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request){
        StringBuilder errorMsg = new StringBuilder();
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        for(ConstraintViolation item : constraintViolations){
            errorMsg.append(item.getMessage());
            errorMsg.append("-");
        }
        errorMsg.deleteCharAt(errorMsg.length() - 1);
        log.error(this.getRequestInfo(request), ex);
        return new ReturnData(ReturnMsg.BAD_REQUEST.getCode(), errorMsg.toString());
    }

    /**
     * 处理MethodArgumentNotValidException
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    public ReturnData<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request){
        log.error(this.getRequestInfo(request), ex);
        return new ReturnData(ReturnMsg.BAD_REQUEST.getCode(), getValidErrorMsg(ex.getBindingResult()));
    }

    /**
     * 处理 MethodArgumentTypeMismatchException
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    @ResponseBody
    public ReturnData<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request){
        log.error(this.getRequestInfo(request), ex);
        return new ReturnData(ReturnMsg.BAD_REQUEST.getCode(), "方法参数类型不匹配");
    }

    /**
     * 处理BindException
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {BindException.class})
    @ResponseBody
    public ReturnData<Object> handleBindException(BindException ex, HttpServletRequest request){
        log.error(this.getRequestInfo(request), ex);
        return new ReturnData(ReturnMsg.BAD_REQUEST.getCode(), getValidErrorMsg(ex.getBindingResult()));
    }

    private String getValidErrorMsg(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        if (errors != null && !errors.isEmpty()) {
            StringBuffer errorStringBuffer = new StringBuffer();
            for (ObjectError oe : errors) {
                errorStringBuffer.append(oe.getDefaultMessage());
                errorStringBuffer.append(",");
            }
            if(StrUtil.isNotBlank(errorStringBuffer)){
                errorStringBuffer.deleteCharAt(errorStringBuffer.length() - 1);
            }
            return errorStringBuffer.toString();
        }
        return "";
    }

    /**
     * controller层请求异常的通用处理逻辑
     * @author liaogang
     * @date 2020/9/4
     * @param ex
     * @param request
     * @return cn.lghuntfor.commons.common.result.ReturnData
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ReturnData exceptionHandler(Exception ex, HttpServletRequest request) throws IOException {
        ReturnData returnData = new ReturnData();
        try {
            if (ex instanceof HttpMessageNotReadableException) {
                returnData.setErrorMsg(400, ex.getMessage());
            } else if (ex.getCause() instanceof InvalidFormatException) {
                StringBuilder errorMsg = new StringBuilder(200);
                errorMsg.append("请求参数类型不匹配:").append(((InvalidFormatException) ex.getCause()).getValue())
                        .append(", 正确的参数类型为:").append(((InvalidFormatException) ex.getCause()).getTargetType().getSimpleName());
                returnData.setErrorMsg(400, errorMsg.toString());
            } else {
                returnData.buildErrorMsg();
            }
            log.error(this.getRequestInfo(request), ex);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return returnData;
    }

    private String getRequestInfo(HttpServletRequest request) {
        StringBuilder errorMsg = new StringBuilder("===global exception handler===, method=")
                .append(request.getMethod())
                .append(", url=")
                .append(request.getRequestURL());
        return errorMsg.toString();
    }

}

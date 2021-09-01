package cn.ex.project.scaffold.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一错误码异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public R errorHandler2ApiException(HttpServletRequest request, ApiException exception) {
        log.error("[ApiException]执行错误：" + exception.getMsg() + "，接口：" + request.getRequestURI());
        return R.error(exception.getMsg());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R errorHandler2MissingServletRequestParameterException(HttpServletRequest request, MissingServletRequestParameterException exception) {
        log.error("[MissingServletRequestParameterException]系统参数异常：" + "，接口：" + request.getRequestURI(),exception);
        return R.error("系统参数异常");
    }

    @ExceptionHandler(value = RuntimeException.class)
    public R errorHandler2RuntimeException(HttpServletRequest request, RuntimeException exception) {
        log.error("[RuntimeException]运行时异常" + "，接口：" + request.getRequestURI(),exception);
        return R.error(exception.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public R errorHandler2Exception(HttpServletRequest request, Exception exception) {
        log.error("[Exception]系统异常" + "，接口：" + request.getRequestURI(),exception);
        return R.error("系统异常");
    }

    @ExceptionHandler(value = Throwable.class)
    public R errorHandler2Throwable(HttpServletRequest request, Throwable throwable) {
        log.error("[Throwable]系统异常" + "，接口：" + request.getRequestURI(),throwable);
        return R.error("系统异常#2");
    }

}

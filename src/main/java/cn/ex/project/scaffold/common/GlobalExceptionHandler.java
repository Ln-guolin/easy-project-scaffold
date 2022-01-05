package cn.ex.project.scaffold.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ResponseFacade;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;

import org.apache.catalina.connector.Response;
import org.apache.tomcat.util.http.MimeHeaders;

/**
 * 统一错误码异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public R errorHandler2ApiException(HttpServletRequest request, HttpServletResponse response,ApiException exception) {
        log.error("[ApiException]执行错误：" + exception.getMsg() + "，接口：" + request.getRequestURI());
        response.setContentType("application/json");
        removeHeader(response,"Content-Disposition");
        return R.error(exception.getMsg());
    }



    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R errorHandler2MissingServletRequestParameterException(HttpServletRequest request,HttpServletResponse response, MissingServletRequestParameterException exception) {
        log.error("[MissingServletRequestParameterException]系统参数异常：" + "，接口：" + request.getRequestURI(),exception);
        response.setContentType("application/json");
        removeHeader(response,"Content-Disposition");
        return R.error("系统参数异常");
    }

    @ExceptionHandler(value = RuntimeException.class)
    public R errorHandler2RuntimeException(HttpServletRequest request, HttpServletResponse response,RuntimeException exception) {
        log.error("[RuntimeException]运行时异常" + "，接口：" + request.getRequestURI(),exception);
        response.setContentType("application/json");
        removeHeader(response,"Content-Disposition");
        return R.error(exception.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public R errorHandler2Exception(HttpServletRequest request,HttpServletResponse response, Exception exception) {
        log.error("[Exception]系统异常" + "，接口：" + request.getRequestURI(),exception);
        response.setContentType("application/json");
        removeHeader(response,"Content-Disposition");
        return R.error("系统异常");
    }

    @ExceptionHandler(value = Throwable.class)
    public R errorHandler2Throwable(HttpServletRequest request, HttpServletResponse response,Throwable throwable) {
        log.error("[Throwable]系统异常" + "，接口：" + request.getRequestURI(),throwable);
        response.setContentType("application/json");
        removeHeader(response,"Content-Disposition");
        return R.error("系统异常#2");
    }

    private void removeHeader(HttpServletResponse response,String removeHeaderName){
        try {
            Field declaredField = ResponseFacade.class.getDeclaredField("response");
            declaredField.setAccessible(true);
            Response resp = (Response) declaredField.get(response);
            org.apache.coyote.Response coyoteResponse = resp.getCoyoteResponse();
            MimeHeaders mimeHeaders = coyoteResponse.getMimeHeaders();
            mimeHeaders.removeHeader(removeHeaderName);
        } catch (Exception e) {
            log.info("[err]移除header参数异常",e);
        }
    }

}

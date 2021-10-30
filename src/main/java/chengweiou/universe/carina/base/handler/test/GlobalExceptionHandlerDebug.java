package chengweiou.universe.carina.base.handler.test;


import org.springframework.context.annotation.Profile;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import chengweiou.universe.blackhole.exception.BaseExceptionHandlerDebug;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.util.LogUtil;

@Profile("!prod")
@RestControllerAdvice
public class GlobalExceptionHandlerDebug extends BaseExceptionHandlerDebug {

    @ExceptionHandler(ProjException.class)
    public Rest handleProjException(ProjException ex) {
        return super.handleProjException(ex);
    }
    @ExceptionHandler(ParamException.class)
    public Rest handleParamException(ParamException ex) {
        return super.handleParamException(ex);
    }
    @ExceptionHandler(UnauthException.class)
    public Rest handleUnauthException(UnauthException ex) {
        Rest rest = Rest.fail(BasicRestCode.UNAUTH);
        rest.setMessage(ex.getMessage());
        LogUtil.e(rest.toString(), ex);
        return rest;
    }
    @ExceptionHandler(BindException.class)
    public Rest handleParamException(BindException ex) {
        Rest rest = Rest.fail(BasicRestCode.PARAM);
        rest.setMessage(ex.getMessage());
        return rest;
    }
    @ExceptionHandler(MissingRequestHeaderException.class)
    public Rest handleParamException(MissingRequestHeaderException ex) {
        Rest rest = Rest.fail(BasicRestCode.PARAM);
        rest.setMessage(ex.getHeaderName() + "cannot be null");
        return rest;
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Rest handleException(HttpRequestMethodNotSupportedException ex) {
        Rest rest = Rest.fail(BasicRestCode.FAIL);
        rest.setMessage(ex.getMessage());
        LogUtil.i(ex.getMessage(), ex);
        return rest;
    }
    @ExceptionHandler(FailException.class)
    public Rest handleFailException(FailException ex) {
        return super.handleFailException(ex);
    }
    @ExceptionHandler(Exception.class)
    public Rest handleException(Exception ex) {
        return super.handleException(ex);
    }
}

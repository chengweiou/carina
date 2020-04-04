package com.chengweiou.universe.carina.base.handler;


import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.util.LogUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Profile("prod")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProjException.class)
    public Rest handleProjException(ProjException ex) {
        return Rest.fail(ex.getCode());
    }
    @ExceptionHandler(ParamException.class)
    public Rest handleParamException(ParamException ex) {
        return Rest.fail(BasicRestCode.PARAM);
    }
    @ExceptionHandler(BindException.class)
    public Rest handleParamException(BindException ex) {
        return Rest.fail(BasicRestCode.PARAM);
    }
    @ExceptionHandler(MissingRequestHeaderException.class)
    public Rest handleParamException(MissingRequestHeaderException ex) {
        return Rest.fail(BasicRestCode.PARAM);
    }
    @ExceptionHandler(FailException.class)
    public Rest handleFailException(FailException ex) {
        Rest rest = Rest.fail(BasicRestCode.FAIL);
        LogUtil.i(rest.toString(), ex);
        return rest;
    }
    @ExceptionHandler(Exception.class)
    public Rest handleException(Exception ex) {
        Rest rest = Rest.fail(BasicRestCode.FAIL);
        LogUtil.e(rest.toString(), ex);
        return rest;
    }
}

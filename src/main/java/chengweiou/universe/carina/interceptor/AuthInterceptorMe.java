package chengweiou.universe.carina.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import org.springframework.web.servlet.HandlerInterceptor;

import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.util.GsonUtil;
import chengweiou.universe.carina.base.converter.Account;

public class AuthInterceptorMe implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accountJson = request.getHeader("loginAccount");
        if (accountJson == null) return unauth(response);
        Account loginAccount = GsonUtil.create().fromJson(accountJson, Account.class);
        if (loginAccount.getPerson() == null) return unauth(response);
        return true;
    }

    private boolean unauth(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(GsonUtil.create().toJson(Rest.fail(BasicRestCode.UNAUTH)));
        return false;
    }
}

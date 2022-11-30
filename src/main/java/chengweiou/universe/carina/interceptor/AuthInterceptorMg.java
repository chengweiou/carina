package chengweiou.universe.carina.interceptor;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.util.GsonUtil;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.model.entity.person.PersonType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthInterceptorMg implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (checkInServer(request)) return true;
        String accountJson = request.getHeader("loginAccount");
        if (accountJson == null) return unauth(response);
        Account loginAccount = GsonUtil.create().fromJson(accountJson, Account.class);
        PersonType personType = PersonType.valueOf(loginAccount.getExtra());
        if (personType == PersonType.SUPER) return true;
        boolean additionalAuth = checkAdditionalAuth(personType, handler);
        if (additionalAuth) return true;
        return unauth(response);
    }

    private boolean unauth(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(GsonUtil.create().toJson(Rest.fail(BasicRestCode.UNAUTH)));
        return false;
    }

    private boolean checkInServer(HttpServletRequest request) {
        return Boolean.valueOf(request.getHeader("inServer"));
    }

    private boolean checkAdditionalAuth(PersonType personType, Object handler) {
        Auth auth = ((HandlerMethod) handler).getMethodAnnotation(Auth.class);
        if (auth == null) auth = ((HandlerMethod) handler).getMethod().getDeclaringClass().getAnnotation(Auth.class);
        return auth != null && Arrays.asList(auth.value()).contains(personType);
    }
}

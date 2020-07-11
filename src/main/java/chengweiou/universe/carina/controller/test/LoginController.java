package chengweiou.universe.carina.controller.test;


import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.base.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("testLoginController")
@RequestMapping("test")
public class LoginController {
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Rest<String> save(Account e) throws ParamException, FailException, ProjException {
        Valid.check("account.person", e.getPerson()).isNotNull();
        Valid.check("account.person.id", e.getPerson().getId()).isNotNull();
        String token = jwtUtil.sign(e);
        return Rest.ok(token);
    }

}

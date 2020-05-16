package chengweiou.universe.carina.manager;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.sdk.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountManager {
    @Autowired
    private AccountService accountService;
    public Long save(Account account) throws FailException {
        Rest<Long> rest = accountService.save(account);
        if (rest.getCode() != BasicRestCode.OK) throw new FailException("account service return code of: " + rest.getCode() + ". and message:" + rest.getMessage());
        return rest.getData();
    }
}

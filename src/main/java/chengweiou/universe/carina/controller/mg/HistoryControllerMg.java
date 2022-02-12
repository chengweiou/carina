package chengweiou.universe.carina.controller.mg;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.service.history.HistoryDio;

@RestController
@RequestMapping("mg")
public class HistoryControllerMg {
    @Autowired
    private HistoryDio dio;

    @PostMapping("/history")
    public Rest<Long> save(History e) throws ParamException, FailException, ProjException {
        Valid.check("history.sender", e.getSender()).isNotNull();
        Valid.check("history.sender.id", e.getSender().getId()).is().positive();
        Valid.check("history.room", e.getRoom()).isNotNull();
        Valid.check("history.room.id", e.getRoom().getId()).is().positive();
        Valid.check("history.v", e.getV()).is().lengthIn(100);
        dio.save(e);
        return Rest.ok(e.getId());
    }

    @DeleteMapping("/history/{id}")
    public Rest<Boolean> delete(History e) throws ParamException, FailException {
        Valid.check("history.id", e.getId()).is().positive();
        dio.delete(e);
        return Rest.ok(true);
    }
    @PutMapping("/history/{id}")
    public Rest<Boolean> update(History e) throws ParamException {
        Valid.check("history.id", e.getId()).is().positive();
        Valid.check("history.person | room | sender | type | v",
                e.getPerson(), e.getRoom(), e.getType(), e.getV()).are().notAllNull();
        boolean success = dio.update(e) == 1;
        return Rest.ok(success);
    }

    @GetMapping("/history/{id}")
    public Rest<History> findById(History e) throws ParamException {
        Valid.check("history.id", e.getId()).is().positive();
        History indb = dio.findById(e);
        return Rest.ok(indb);
    }

    @GetMapping("/history/count")
    public Rest<Long> count(SearchCondition searchCondition) {
        long count = dio.count(searchCondition, null);
        return Rest.ok(count);
    }

    @GetMapping("/history")
    public Rest<List<History>> find(SearchCondition searchCondition) {
        List<History> list = dio.find(searchCondition, null);
        return Rest.ok(list);
    }
}

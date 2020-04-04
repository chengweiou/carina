package com.chengweiou.universe.carina.service;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.model.Builder;
import com.chengweiou.universe.carina.data.Data;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.history.History;
import com.chengweiou.universe.carina.service.history.HistoryService;
import com.chengweiou.universe.carina.service.history.HistoryTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@SpringBootTest
@ActiveProfiles("test")
public class HistoryTest {
	@Autowired
	private HistoryService service;
	@Autowired
	private HistoryTask task;
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException {
		History e = Builder.set("person", data.personList.get(1)).set("sender", data.personList.get(0)).set("room", data.roomList.get(0)).set("v", "service test").to(new History());
		service.save(e);
		Assertions.assertEquals(true, e.getId()> 0);
		service.delete(e);
	}

	@Test
	public void deleteMulti() throws FailException, ExecutionException, InterruptedException {
		History e1 = Builder.set("person", data.personList.get(1)).set("sender", data.personList.get(0)).set("room", data.roomList.get(0)).set("v", "service test").to(new History());
		service.save(e1);
		History e2 = Builder.set("person", data.personList.get(1)).set("sender", data.personList.get(0)).set("room", data.roomList.get(0)).set("v", "service test").to(new History());
		service.save(e2);
		Future<Long> future = task.delete(Arrays.asList(e1, e2));
		Assertions.assertEquals(2, future.get());
	}

	@Test
	public void update() {
		String old = data.historyList.get(0).getV();
		History e = Builder.set("id", data.historyList.get(0).getId()).set("v", "service update").to(new History());
		long count = service.update(e);
		Assertions.assertEquals(1, count);
		History indb = service.findById(e);
		Assertions.assertEquals("service update", indb.getV());
		Builder.set("v", old).to(e);
		service.update(e);
	}

	@Test
	public void count() {
		long count = service.count(new SearchCondition());
		Assertions.assertEquals(2, count);
	}

	@Test
	public void find() {
		List<History> list = service.find(new SearchCondition());
		Assertions.assertEquals(2, list.size());
	}

	@BeforeEach
	public void init() {
		data.init();
	}
}

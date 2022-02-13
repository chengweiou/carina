package chengweiou.universe.carina.service;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.carina.data.Data;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.service.history.HistoryDio;
import chengweiou.universe.carina.service.history.HistoryService;
import chengweiou.universe.carina.service.history.HistoryTask;
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
	private HistoryDio dio;
	@Autowired
	private HistoryTask task;
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException {
		History e = Builder.set("person", data.personList.get(1)).set("sender", data.personList.get(0)).set("room", data.roomList.get(0)).set("v", "service test").to(new History());
		dio.save(e);
		Assertions.assertEquals(true, e.getId()> 0);
		dio.delete(e);
	}

	@Test
	public void deleteMulti() throws FailException, ExecutionException, InterruptedException {
		History e1 = Builder.set("person", data.personList.get(1)).set("sender", data.personList.get(0)).set("room", data.roomList.get(0)).set("v", "service test").to(new History());
		dio.save(e1);
		History e2 = Builder.set("person", data.personList.get(1)).set("sender", data.personList.get(0)).set("room", data.roomList.get(0)).set("v", "service test").to(new History());
		dio.save(e2);
		Future<Long> future = task.deleteByList(Arrays.asList(e1, e2));
		Assertions.assertEquals(null, future.get());
	}

	@Test
	public void update() {
		String old = data.historyList.get(0).getV();
		History e = Builder.set("id", data.historyList.get(0).getId()).set("v", "service update").to(new History());
		long count = dio.update(e);
		Assertions.assertEquals(1, count);
		History indb = dio.findById(e);
		Assertions.assertEquals("service update", indb.getV());
		Builder.set("v", old).to(e);
		dio.update(e);
	}

	@Test
	public void updateRead() {
		History e = Builder.set("room", data.roomList.get(0)).set("person", data.personList.get(0)).to(new History());
		long count = service.updateReadByPersonAndRoom(e);
		Assertions.assertEquals(1, count);
		History indb = dio.findById(data.historyList.get(0));
		Assertions.assertEquals(false, indb.getUnread());

		dio.update(data.historyList.get(0));
	}

	@Test
	public void count() {
		long count = dio.count(new SearchCondition(), null);
		Assertions.assertEquals(2, count);
	}

	@Test
	public void find() {
		List<History> list = dio.find(new SearchCondition(), null);
		Assertions.assertEquals(2, list.size());
	}

	@BeforeEach
	public void init() {
		data.init();
	}
}

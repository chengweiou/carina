package chengweiou.universe.carina.service;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.carina.data.Data;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.service.message.MsgService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@ActiveProfiles("test")
public class MsgTest {
	@Autowired
	private MsgService service;
	@Autowired
	private Data data;

	@Test
	public void sendAndRead() throws FailException, ExecutionException, InterruptedException, ProjException {
		History e = Builder.set("sender", data.personList.get(0)).set("room", data.roomList.get(1)).set("v", "service test").to(new History());
		service.send(e);
		Assertions.assertEquals(true, e.getId()> 0);
		// delete by read
		List<History> h1List = service.read(new SearchCondition(), data.personList.get(0), data.roomList.get(1));
		Assertions.assertEquals(0, h1List.size());
		List<History> h2List = service.read(new SearchCondition(), data.personList.get(1), data.roomList.get(1));
		Assertions.assertEquals(1, h2List.size());
		h2List = service.read(new SearchCondition(), data.personList.get(1), data.roomList.get(1));
		Assertions.assertEquals(0, h2List.size());
		List<History> h3List = service.read(new SearchCondition(), data.personList.get(2), data.roomList.get(1));
		Assertions.assertEquals(1, h3List.size());
		h3List = service.read(new SearchCondition(), data.personList.get(2), data.roomList.get(1));
		Assertions.assertEquals(0, h3List.size());
	}


	@BeforeEach
	public void init() {
		data.init();
	}
}

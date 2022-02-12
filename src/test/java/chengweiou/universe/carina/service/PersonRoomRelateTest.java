package chengweiou.universe.carina.service;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.carina.data.Data;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.service.room.PersonRoomRelateDio;
import chengweiou.universe.carina.service.room.PersonRoomRelateService;
import chengweiou.universe.carina.service.room.PersonRoomRelateTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@SpringBootTest
@ActiveProfiles("test")
public class PersonRoomRelateTest {
	@Autowired
	private PersonRoomRelateService service;
	@Autowired
	private PersonRoomRelateDio dio;
	@Autowired
	private PersonRoomRelateTask task;
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException, ProjException {
		PersonRoomRelate e = Builder.set("person", data.personList.get(2)).set("room", data.roomList.get(0)).set("name", "service test").to(new PersonRoomRelate());
		service.save(e);
		Assertions.assertEquals(true, e.getId()> 0);
		dio.delete(e);
	}

	@Test
	public void update() {
		String old = data.personRoomRelateList.get(0).getLastMessage();
		PersonRoomRelate e = Builder.set("id", data.personRoomRelateList.get(0).getId()).set("lastMessage", "service update").to(new PersonRoomRelate());
		long count = dio.update(e);
		Assertions.assertEquals(1, count);
		PersonRoomRelate indb = dio.findById(e);
		Assertions.assertEquals("service update", indb.getLastMessage());
		Builder.set("lastMessage", old).to(e);
		dio.update(e);
	}

	@Test
	public void updateTaskUnreadAndMessage() throws ExecutionException, InterruptedException {
		String old = data.personRoomRelateList.get(0).getUnread().toString();
		PersonRoomRelate e = Builder.set("id", data.personRoomRelateList.get(0).getId()).set("unread", 20).to(new PersonRoomRelate());
		Future<Long> count = task.update(e);
		Assertions.assertEquals(1, count.get());
		PersonRoomRelate indb = dio.findById(e);
		Assertions.assertEquals(20, indb.getUnread());
		Builder.set("unread", old).to(e);
		dio.update(e);
	}

	@Test
	public void updateTaskOtherByPersonNameChange() throws ExecutionException, InterruptedException {
		Person e = Builder.set("id", data.personList.get(0).getId()).set("name", "task change name").to(new Person());
		Future<Long> count = task.updateSoloOtherByPerson(e);
		Assertions.assertEquals(1, count.get());
		PersonRoomRelate indb = dio.findById(data.personRoomRelateList.get(1));
		Assertions.assertEquals("task change name", indb.getName());
		dio.update(data.personRoomRelateList.get(1));
	}

	@Test
	public void findByKey() {
		PersonRoomRelate e = Builder.set("person", data.personList.get(0)).set("room", data.roomList.get(0)).to(new PersonRoomRelate());
		PersonRoomRelate indb = dio.findByKey(e);
		Assertions.assertEquals(data.personRoomRelateList.get(0).getId(), indb.getId());
	}

	@Test
	public void count() {
		long count = dio.count(new SearchCondition(), null);
		Assertions.assertEquals(5, count);
	}

	@Test
	public void find() {
		List<PersonRoomRelate> list = dio.find(new SearchCondition(), null);
		Assertions.assertEquals(5, list.size());
	}



	@BeforeEach
	public void init() {
		data.init();
	}
}

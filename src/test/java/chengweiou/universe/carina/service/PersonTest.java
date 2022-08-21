package chengweiou.universe.carina.service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.carina.data.Data;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.service.person.PersonDio;
import chengweiou.universe.carina.service.person.PersonService;
import chengweiou.universe.carina.service.person.PersonTask;

@SpringBootTest
@ActiveProfiles("test")
public class PersonTest {
	@Autowired
	private PersonService service;
	@Autowired
	private PersonDio dio;
	@Autowired
	private PersonTask task;
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException {
		Person e = Builder.set("id", 20).set("name", "service test").to(new Person());
		dio.save(e);
		Assertions.assertEquals(true, e.getId()> 0);
		dio.delete(e);
	}

	@Test
	public void update() throws FailException {
		String old = data.personList.get(0).getName();
		Person e = Builder.set("id", data.personList.get(0).getId()).set("name", "service update").to(new Person());
		long count = dio.update(e);
		Assertions.assertEquals(1, count);
		Person indb = dio.findById(e);
		Assertions.assertEquals("service update", indb.getName());
		Builder.set("name", old).to(e);
		dio.update(e);
	}

	@Test
	public void updateTaskUnread() throws ExecutionException, InterruptedException, FailException {
		String old = data.personList.get(0).getUnread().toString();
		Person e = Builder.set("id", data.personList.get(0).getId()).set("unread", "10").to(new Person());
		Future<Long> count = task.update(e);
		Assertions.assertEquals(1, count.get());
		Person indb = dio.findById(e);
		Assertions.assertEquals(10, indb.getUnread());
		Builder.set("unread", old).to(e);
		dio.update(e);
	}

	@Test
	public void count() {
		long count = dio.count(new SearchCondition(), null);
		Assertions.assertEquals(3, count);
	}

	@Test
	public void find() {
		SearchCondition searchCondition = Builder.set("k", "ou").set("sort", "id").to(new SearchCondition());
		List<Person> list = dio.find(searchCondition, null);
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals(data.personList.get(0).getId(), list.get(0).getId());
	}



	@BeforeEach
	public void init() {
		data.init();
	}
}

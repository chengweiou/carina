package com.chengweiou.universe.carina.service;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.model.Builder;
import com.chengweiou.universe.carina.data.Data;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import com.chengweiou.universe.carina.service.room.PersonRoomRelateService;
import com.chengweiou.universe.carina.service.room.PersonRoomRelateTask;
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
	private PersonRoomRelateTask task;
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException {
		// todo enter room 如果是两人的新对话, 旧对话，如何找出。只有人的id, 没有roomid
		PersonRoomRelate e = Builder.set("person", data.personList.get(0)).set("room", data.roomList.get(0)).set("name", "service test").to(new PersonRoomRelate());
		service.save(e);
		Assertions.assertEquals(true, e.getId()> 0);
		service.delete(e);
	}

	@Test
	public void update() {
		String old = data.personRoomRelateList.get(0).getLastMessage();
		PersonRoomRelate e = Builder.set("id", data.personRoomRelateList.get(0).getId()).set("lastMessage", "service update").to(new PersonRoomRelate());
		long count = service.update(e);
		Assertions.assertEquals(1, count);
		PersonRoomRelate indb = service.findById(e);
		Assertions.assertEquals("service update", indb.getLastMessage());
		Builder.set("lastMessage", old).to(e);
		service.update(e);
	}

	@Test
	public void updateTaskUnreadAndMessage() throws ExecutionException, InterruptedException {
		String old = data.personRoomRelateList.get(0).getUnread().toString();
		PersonRoomRelate e = Builder.set("id", data.personRoomRelateList.get(0).getId()).set("unread", 20).to(new PersonRoomRelate());
		Future<Long> count = task.update(e);
		Assertions.assertEquals(1, count.get());
		PersonRoomRelate indb = service.findById(e);
		Assertions.assertEquals(20, indb.getUnread());
		Builder.set("unread", old).to(e);
		service.update(e);
	}

	@Test
	public void findByKey() {
		PersonRoomRelate e = Builder.set("person", data.personList.get(0)).set("room", data.roomList.get(0)).to(new PersonRoomRelate());
		PersonRoomRelate indb = service.findByKey(e);
		Assertions.assertEquals(data.personRoomRelateList.get(0).getId(), indb.getId());
	}

	@Test
	public void count() {
		long count = service.count(new SearchCondition());
		Assertions.assertEquals(5, count);
	}

	@Test
	public void find() {
		List<PersonRoomRelate> list = service.find(new SearchCondition());
		Assertions.assertEquals(5, list.size());
	}



	@BeforeEach
	public void init() {
		data.init();
	}
}

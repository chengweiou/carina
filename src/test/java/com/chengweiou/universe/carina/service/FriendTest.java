package com.chengweiou.universe.carina.service;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.model.Builder;
import com.chengweiou.universe.carina.data.Data;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.friend.Friend;
import com.chengweiou.universe.carina.model.entity.person.Person;
import com.chengweiou.universe.carina.service.friend.FriendService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class FriendTest {
	@Autowired
	private FriendService service;
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException {
		Friend e = Builder.set("person", data.personList.get(1)).set("target", data.personList.get(0)).to(new Friend());
		service.save(e);
		Assertions.assertEquals(true, e.getId()> 0);
		service.delete(e);
	}

	@Test
	public void update() {
		Person old = data.friendList.get(0).getTarget();
		Friend e = Builder.set("id", data.friendList.get(0).getId()).set("target", Builder.set("id", 30).to(new Person())).to(new Friend());
		long count = service.update(e);
		Assertions.assertEquals(1, count);
		Friend indb = service.findById(e);
		Assertions.assertEquals(30, indb.getTarget().getId());
		Builder.set("target", old).to(e);
		service.update(e);
	}

	@Test
	public void count() {
		long count = service.count(new SearchCondition());
		Assertions.assertEquals(2, count);
	}

	@Test
	public void find() {
		List<Friend> list = service.find(new SearchCondition());
		Assertions.assertEquals(2, list.size());
	}

	@BeforeEach
	public void init() {
		data.init();
	}
}

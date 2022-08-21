package chengweiou.universe.carina.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.carina.data.Data;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.friend.Friend;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.service.friend.FriendDio;
import chengweiou.universe.carina.service.friend.FriendService;

@SpringBootTest
@ActiveProfiles("test")
public class FriendTest {
	@Autowired
	private FriendService service;
	@Autowired
	private FriendDio dio;
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException, ProjException {
		Friend e = Builder.set("person", data.personList.get(1)).set("target", data.personList.get(2)).to(new Friend());
		dio.save(e);
		Assertions.assertEquals(true, e.getId()> 0);
		dio.delete(e);
	}

	@Test
	public void update() throws FailException {
		Person old = data.friendList.get(0).getTarget();
		Friend e = Builder.set("id", data.friendList.get(0).getId()).set("target", Builder.set("id", 30).to(new Person())).to(new Friend());
		long count = dio.update(e);
		Assertions.assertEquals(1, count);
		Friend indb = dio.findById(e);
		Assertions.assertEquals(30, indb.getTarget().getId());
		Builder.set("target", old).to(e);
		dio.update(e);
	}

	@Test
	public void saveOrUpdate() throws FailException, ProjException {
		Friend e1 = Builder.set("person", data.personList.get(0)).set("target", data.personList.get(2)).to(new Friend());
		dio.saveOrUpdateByKey(e1);
		Friend e2 = Builder.set("person", data.personList.get(0)).set("target", data.personList.get(2)).to(new Friend());
		dio.saveOrUpdateByKey(e2);
		Assertions.assertEquals(true, e1.getId()> 0);
		Assertions.assertEquals(e2.getId(), e1.getId());
		dio.delete(e2);
	}

	@Test
	public void countByKey() {
		long count = dio.countByKey(data.friendList.get(0));
		Assertions.assertEquals(1, count);
		count = dio.countByKey(Builder.set("person", data.personList.get(0)).set("target", data.personList.get(2)).to(new Friend()));
		Assertions.assertEquals(0, count);
	}

	@Test
	public void count() {
		long count = dio.count(new SearchCondition(), null);
		Assertions.assertEquals(2, count);
	}

	@Test
	public void find() {
		List<Friend> list = dio.find(new SearchCondition(), null);
		Assertions.assertEquals(2, list.size());
	}



	@BeforeEach
	public void init() {
		data.init();
	}
}

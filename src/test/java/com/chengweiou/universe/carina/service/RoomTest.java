package com.chengweiou.universe.carina.service;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.model.Builder;
import com.chengweiou.universe.carina.data.Data;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.room.Room;
import com.chengweiou.universe.carina.service.room.RoomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class RoomTest {
	@Autowired
	private RoomService service;
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException {
		Room e = Builder.to(new Room());
		service.save(e);
		Assertions.assertEquals(true, e.getId()> 0);
		service.delete(e);
	}

	@Test
	public void update() {
		List<Long> old = data.roomList.get(0).getPersonIdList();
		Room e = Builder.set("id", data.roomList.get(0).getId()).set("personIdList", Arrays.asList(100L)).to(new Room());
		long count = service.update(e);
		Assertions.assertEquals(1, count);
		Room indb = service.findById(e);
		Assertions.assertEquals("100", indb.getPersonIdListString());
		Builder.set("personIdList", old).to(e);
		service.update(e);
	}

	@Test
	public void count() {
		long count = service.count(new SearchCondition());
		Assertions.assertEquals(2, count);
	}

	@Test
	public void find() {
		List<Room> list = service.find(new SearchCondition());
		Assertions.assertEquals(2, list.size());
	}



	@BeforeEach
	public void init() {
		data.init();
	}
}

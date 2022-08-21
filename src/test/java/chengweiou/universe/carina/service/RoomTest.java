package chengweiou.universe.carina.service;

import java.util.Arrays;
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
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.model.entity.room.Room;
import chengweiou.universe.carina.model.entity.room.RoomType;
import chengweiou.universe.carina.service.room.PersonRoomRelateDio;
import chengweiou.universe.carina.service.room.RoomDio;
import chengweiou.universe.carina.service.room.RoomService;

@SpringBootTest
@ActiveProfiles("test")
public class RoomTest {
	@Autowired
	private RoomService service;
	@Autowired
	private RoomDio dio;
	@Autowired
	private PersonRoomRelateDio personRoomRelateDio;
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException, ProjException {
		Room e = Builder.set("type", RoomType.SOLO).to(new Room());
		dio.save(e);
		Assertions.assertEquals(true, e.getId()> 0);
		dio.delete(e);
	}

	@Test
	public void update() throws FailException {
		List<Long> old = data.roomList.get(0).getPersonIdList();
		Room e = Builder.set("id", data.roomList.get(0).getId()).set("personIdList", Arrays.asList(100L)).to(new Room());
		long count = dio.update(e);
		Assertions.assertEquals(1, count);
		Room indb = dio.findById(e);
		Assertions.assertEquals(Arrays.asList(100L), indb.getPersonIdList());
		Builder.set("personIdList", old).to(e);
		dio.update(e);
	}

	@Test
	public void count() {
		long count = dio.count(new SearchCondition(), null);
		Assertions.assertEquals(2, count);
	}

	@Test
	public void find() {
		List<Room> list = dio.find(new SearchCondition(), null);
		Assertions.assertEquals(2, list.size());
	}

	@Test
	public void enterRoomById() throws FailException, ProjException {
		Room e = Builder.set("id", data.roomList.get(0).getId()).to(new Room());
		Room indb = service.enter(e);
		Assertions.assertEquals(data.roomList.get(0).getId(), indb.getId());
	}

	@Test
	public void enterRoomNew() throws FailException, ProjException {
		Room e = Builder.set("personIdList", Arrays.asList(1L, 3L)).set("type", RoomType.SOLO).to(new Room());
		Room indb = service.enter(e);
		Assertions.assertEquals(true, e.getId()> 0);
		List<PersonRoomRelate> relateList = personRoomRelateDio.find(new SearchCondition(), Builder.set("room", indb).to(new PersonRoomRelate()));
		Assertions.assertEquals(2, relateList.size());
		dio.delete(indb);
		for (PersonRoomRelate relate : relateList) {
			personRoomRelateDio.delete(relate);
		}
	}

	@Test
	public void enterRoomOld() throws FailException, ProjException {
		Room e = Builder.set("personIdList", Arrays.asList(2L, 1L)).set("type", RoomType.SOLO).to(new Room());
		Room indb = service.enter(e);
		Assertions.assertEquals(data.roomList.get(0).getId(), indb.getId());
	}

	@Test
	public void createGroup() throws FailException, ProjException {
		Room e = Builder.set("personIdList", Arrays.asList(1L,2L,3L,4L)).set("type", RoomType.GROUP).to(new Room());
		Room indb = service.createGroup(e, "service-test", "");
		Assertions.assertEquals(true, indb.getId() > 0);
		List<PersonRoomRelate> relateList = personRoomRelateDio.find(new SearchCondition(), Builder.set("room", indb).to(new PersonRoomRelate()));
		Assertions.assertEquals(3, relateList.size());
		dio.delete(e);
		for (PersonRoomRelate relate : relateList) {
			personRoomRelateDio.delete(relate);
		}
	}

	@Test
	public void enterRoomOldGroup() throws FailException, ProjException {
		Room e = Builder.set("personIdList", Arrays.asList(1L,2L,3L)).set("type", RoomType.GROUP).to(new Room());
		Room indb = service.enter(e);
		Assertions.assertEquals(data.roomList.get(1).getId(), indb.getId());
	}

	// todo
//	@Test
//	public void joinGroup() throws FailException {
//		Room indb = service.joinGroup(data.roomList.get(1), Builder.set("id", 10).to(new Person()));
//		Assertions.assertEquals(true, indb.getId() > 0);
//		List<PersonRoomRelate> relateList = personRoomRelateDio.find(new SearchCondition(), Builder.set("room", indb).to(new PersonRoomRelate()));
//		Assertions.assertEquals(4, relateList.size());
//		service.leaveGroup(data.roomList.get(1), Builder.set("id", 10).to(new Person()));
//		relateList = personRoomRelateDio.find(new SearchCondition(), Builder.set("room", indb).to(new PersonRoomRelate()));
//		Assertions.assertEquals(4, relateList.size());
//	}

	@BeforeEach
	public void init() {
		data.init();
	}
}

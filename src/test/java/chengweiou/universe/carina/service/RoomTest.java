package chengweiou.universe.carina.service;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.carina.data.Data;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.model.entity.room.Room;
import chengweiou.universe.carina.model.entity.room.RoomType;
import chengweiou.universe.carina.service.room.PersonRoomRelateService;
import chengweiou.universe.carina.service.room.RoomService;
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
	private PersonRoomRelateService personRoomRelateService;
	@Autowired
	private Data data;

	@Test
	public void saveDelete() throws FailException {
		Room e = Builder.set("type", RoomType.SOLO).to(new Room());
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

	@Test
	public void enterRoomById() throws FailException {
		Room e = Builder.set("id", data.roomList.get(0).getId()).to(new Room());
		Room indb = service.enter(e);
		Assertions.assertEquals(data.roomList.get(0).getId(), indb.getId());
	}

	@Test
	public void enterRoomNew() throws FailException {
		Room e = Builder.set("personIdList", Arrays.asList(1L, 3L)).set("type", RoomType.SOLO).to(new Room());
		Room indb = service.enter(e);
		Assertions.assertEquals(true, e.getId()> 0);
		List<PersonRoomRelate> relateList = personRoomRelateService.find(new SearchCondition(), Builder.set("room", indb).to(new PersonRoomRelate()));
		Assertions.assertEquals(2, relateList.size());
		service.delete(indb);
		for (PersonRoomRelate relate : relateList) {
			personRoomRelateService.delete(relate);
		}
	}

	@Test
	public void enterRoomOld() throws FailException {
		Room e = Builder.set("personIdList", Arrays.asList(2L, 1L)).set("type", RoomType.SOLO).to(new Room());
		Room indb = service.enter(e);
		Assertions.assertEquals(data.roomList.get(0).getId(), indb.getId());
	}

	@Test
	public void createGroup() throws FailException {
		Room e = Builder.set("personIdList", Arrays.asList(1L,2L,3L,4L)).set("type", RoomType.GROUP).to(new Room());
		Room indb = service.createGroup(e, "service-test", "");
		Assertions.assertEquals(true, indb.getId() > 0);
		List<PersonRoomRelate> relateList = personRoomRelateService.find(new SearchCondition(), Builder.set("room", indb).to(new PersonRoomRelate()));
		Assertions.assertEquals(3, relateList.size());
		service.delete(e);
		for (PersonRoomRelate relate : relateList) {
			personRoomRelateService.delete(relate);
		}
	}

	@Test
	public void enterRoomOldGroup() throws FailException {
		Room e = Builder.set("personIdList", Arrays.asList(1L,2L,3L)).set("type", RoomType.GROUP).to(new Room());
		Room indb = service.enter(e);
		Assertions.assertEquals(data.roomList.get(1).getId(), indb.getId());
	}

	// todo
//	@Test
//	public void joinGroup() throws FailException {
//		Room indb = service.joinGroup(data.roomList.get(1), Builder.set("id", 10).to(new Person()));
//		Assertions.assertEquals(true, indb.getId() > 0);
//		List<PersonRoomRelate> relateList = personRoomRelateService.find(new SearchCondition(), Builder.set("room", indb).to(new PersonRoomRelate()));
//		Assertions.assertEquals(4, relateList.size());
//		service.leaveGroup(data.roomList.get(1), Builder.set("id", 10).to(new Person()));
//		relateList = personRoomRelateService.find(new SearchCondition(), Builder.set("room", indb).to(new PersonRoomRelate()));
//		Assertions.assertEquals(4, relateList.size());
//	}

	@BeforeEach
	public void init() {
		data.init();
	}
}

package chengweiou.universe.carina.service;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.carina.config.ProjConfig;
import chengweiou.universe.carina.data.Data;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.service.history.HistoryDio;
import chengweiou.universe.carina.service.message.MsgService;
import chengweiou.universe.carina.service.person.PersonDio;
import chengweiou.universe.carina.service.room.PersonRoomRelateDio;
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
	private HistoryDio historyDio;
	@Autowired
	private PersonDio personDio;
	@Autowired
	private PersonRoomRelateDio personRoomRelateDio;
	@Autowired
	private Data data;
	@Autowired
	private ProjConfig config;

	@Test
	public void sendNoServerHistory() throws FailException, ExecutionException, InterruptedException, ProjException {
		config.setServerHistory(false);

		// 自己0， 房间1
		History e = Builder.set("sender", data.personList.get(0)).set("room", data.roomList.get(1)).set("v", "service test").to(new History());
		service.send(e);

		// 验证数字
		// 需要在service future.get()来等待异步值
		Person p1 = personDio.findById(data.personList.get(0));
		Assertions.assertEquals(0, p1.getUnread());
		Person p2 = personDio.findById(data.personList.get(1));
		Assertions.assertEquals(1, p2.getUnread());
		PersonRoomRelate r1 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(0)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(0, r1.getUnread());
		Assertions.assertEquals("service test", r1.getLastMessage());
		PersonRoomRelate r2 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(1, r2.getUnread());
		Assertions.assertEquals("service test", r2.getLastMessage());

		personDio.update(data.personList.get(0));
		personDio.update(data.personList.get(1));
		personDio.update(data.personList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(0));
		personRoomRelateDio.update(data.personRoomRelateList.get(1));
		personRoomRelateDio.update(data.personRoomRelateList.get(2));
		List<History> delList = historyDio.find(new SearchCondition(), Builder.set("room", data.roomList.get(1)).to(new History()));
		historyDio.delete(delList);
	}

	@Test
	public void readNoServerHistory() throws FailException, ExecutionException, InterruptedException, ProjException {
		config.setServerHistory(false);

		// 自己0， 房间1
		History e = Builder.set("sender", data.personList.get(0)).set("room", data.roomList.get(1)).set("v", "service test").to(new History());
		service.send(e);

		// delete by read
		// 自己看 自己刚发的消息
		List<History> h1List = service.read(new SearchCondition(), data.personList.get(0), data.roomList.get(1));
		Assertions.assertEquals(0, h1List.size());
		// 对方1号看 自己刚发的消息
		List<History> h2List = service.read(new SearchCondition(), data.personList.get(1), data.roomList.get(1));
		Assertions.assertEquals(1, h2List.size());
		// 对方1号看后，就被删除了, 再看没有
		// 需要在service future.get()来等待异步值
//		h2List = service.readAndDelete(new SearchCondition(), data.personList.get(1), data.roomList.get(1));
//		Assertions.assertEquals(0, h2List.size());
		// 对方2号
		List<History> h3List = service.read(new SearchCondition(), data.personList.get(2), data.roomList.get(1));
		Assertions.assertEquals(1, h3List.size());

		// 验证数字
		// 需要在service future.get()来等待异步值
		Person p1 = personDio.findById(data.personList.get(0));
		Assertions.assertEquals(1, p1.getUnread());
		Person p2 = personDio.findById(data.personList.get(1));
		Assertions.assertEquals(1, p2.getUnread());
		PersonRoomRelate r1 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(0)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(0, r1.getUnread());
		Assertions.assertEquals("service test", r1.getLastMessage());
		PersonRoomRelate r2 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(0, r2.getUnread());
		Assertions.assertEquals("service test", r2.getLastMessage());

		personDio.update(data.personList.get(0));
		personDio.update(data.personList.get(1));
		personDio.update(data.personList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(0));
		personRoomRelateDio.update(data.personRoomRelateList.get(1));
		personRoomRelateDio.update(data.personRoomRelateList.get(2));
	}

	@Test
	public void sendWithServerHistory() throws FailException, ExecutionException, InterruptedException, ProjException {
		config.setServerHistory(true);

		History e = Builder.set("sender", data.personList.get(0)).set("room", data.roomList.get(1)).set("v", "service test").to(new History());
		service.send(e);

		// 验证数字
		// 需要在service future.get()来等待异步值
		Person p1 = personDio.findById(data.personList.get(0));
		Assertions.assertEquals(0, p1.getUnread());
		Person p2 = personDio.findById(data.personList.get(1));
		Assertions.assertEquals(1, p2.getUnread());
		PersonRoomRelate r1 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(0)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(0, r1.getUnread());
		Assertions.assertEquals("service test", r1.getLastMessage());
		PersonRoomRelate r2 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(1, r2.getUnread());
		Assertions.assertEquals("service test", r2.getLastMessage());

		personDio.update(data.personList.get(0));
		personDio.update(data.personList.get(1));
		personDio.update(data.personList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(0));
		personRoomRelateDio.update(data.personRoomRelateList.get(1));
		personRoomRelateDio.update(data.personRoomRelateList.get(2));
		List<History> delList = historyDio.find(new SearchCondition(), Builder.set("room", data.roomList.get(1)).to(new History()));
		historyDio.delete(delList);
	}

	@Test
	public void readWithServerHistory() throws FailException, ExecutionException, InterruptedException, ProjException {
		config.setServerHistory(true);

		History e = Builder.set("sender", data.personList.get(0)).set("room", data.roomList.get(1)).set("v", "service test").to(new History());
		service.send(e);

		// 自己 已读
		List<History> h1List = service.read(new SearchCondition(), data.personList.get(0), data.roomList.get(1));
		Assertions.assertEquals(1, h1List.size());
		Assertions.assertEquals(false, h1List.get(0).getUnread());
		historyDio.delete(h1List);
		// 对方 第一次未读，之后已读
		List<History> h2List = service.read(new SearchCondition(), data.personList.get(1), data.roomList.get(1));
		Assertions.assertEquals(1, h2List.size());
		Assertions.assertEquals(true, h2List.get(0).getUnread());
		h2List = service.read(new SearchCondition(), data.personList.get(1), data.roomList.get(1));
		Assertions.assertEquals(1, h2List.size());
		Assertions.assertEquals(false, h2List.get(0).getUnread());
		historyDio.delete(h2List);
		List<History> h3List = service.read(new SearchCondition(), data.personList.get(2), data.roomList.get(1));
		Assertions.assertEquals(1, h3List.size());
		historyDio.delete(h3List);

		// 验证数字
		// 需要在service future.get()来等待异步值
		Person p1 = personDio.findById(data.personList.get(0));
		Assertions.assertEquals(1, p1.getUnread());
		Person p2 = personDio.findById(data.personList.get(1));
		Assertions.assertEquals(1, p2.getUnread());
		PersonRoomRelate r1 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(0)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(0, r1.getUnread());
		Assertions.assertEquals("service test", r1.getLastMessage());
		PersonRoomRelate r2 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(0, r2.getUnread());
		Assertions.assertEquals("service test", r2.getLastMessage());

		personDio.update(data.personList.get(0));
		personDio.update(data.personList.get(1));
		personDio.update(data.personList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(0));
		personRoomRelateDio.update(data.personRoomRelateList.get(1));
		personRoomRelateDio.update(data.personRoomRelateList.get(2));
	}

	@BeforeEach
	public void init() {
		data.init();
	}
}

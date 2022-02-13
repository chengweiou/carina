package chengweiou.universe.carina.service;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
import chengweiou.universe.carina.service.room.RoomService;

// test 需要吧service task 变为dio
@SpringBootTest
@ActiveProfiles("test")
public class MsgTest {
	@Autowired
	private MsgService service;
	@Autowired
	private RoomService roomService;
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
	public void sendNoServerHistory() throws FailException, ProjException {
		config.setServerHistory(false);

		// 自己0， 房间1
		History e = Builder.set("sender", data.personList.get(0)).set("room", data.roomList.get(1)).set("v", "service test").to(new History());
		List<History> list = service.send(e);
		Assertions.assertEquals(3, list.size());

		// 验证数字
		Person p1 = personDio.findById(data.personList.get(0));
		Assertions.assertEquals(0, p1.getUnread());
		Person p2 = personDio.findById(data.personList.get(1));
		Assertions.assertEquals(2, p2.getUnread());
		PersonRoomRelate r1 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(0)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(0, r1.getUnread());
		Assertions.assertEquals("service test", r1.getLastMessage());
		PersonRoomRelate r2 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(1, r2.getUnread());
		Assertions.assertEquals("service test", r2.getLastMessage());

		personDio.update(data.personList.get(0));
		personDio.update(data.personList.get(1));
		personDio.update(data.personList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(3));
		personRoomRelateDio.update(data.personRoomRelateList.get(4));
		list.removeIf(each -> each.getId() == null); // 自己消息没有id，删除
		historyDio.deleteByIdList(list.stream().map(h -> h.getId().toString()).toList());
	}

	@Test
	public void readNoServerHistory() throws FailException, ProjException, InterruptedException {
		// todo 现在要看这条记录是什么时候进来的2
		List<History> indbHistoryList1 = historyDio.find(new SearchCondition(), Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new History()));
		Assertions.assertEquals(0, indbHistoryList1.size());
		config.setServerHistory(false);

		// 自己0， 房间1
		History e = Builder.set("sender", data.personList.get(0)).set("room", data.roomList.get(1)).set("v", "service test").to(new History());
		List<History> list = service.send(e);
		Assertions.assertEquals(3, list.size());

		// delete by read
		// 自己看 自己刚发的消息
		List<History> h0List = service.read(new SearchCondition(), data.personList.get(0), data.roomList.get(1));
		Assertions.assertEquals(0, h0List.size());
		// 对方1号看 自己刚发的消息
		List<History> h1List = service.read(new SearchCondition(), data.personList.get(1), data.roomList.get(1));
		Assertions.assertEquals(1, h1List.size());
		// 对方1号看后，就被删除了, 再看没有
		h1List = service.read(new SearchCondition(), data.personList.get(1), data.roomList.get(1));
		Assertions.assertEquals(0, h1List.size());
		// 对方2号
		List<History> h2List = service.read(new SearchCondition(), data.personList.get(2), data.roomList.get(1));
		Assertions.assertEquals(1, h2List.size());

		// 验证数字
		Person p0 = personDio.findById(data.personList.get(0));
		Assertions.assertEquals(0, p0.getUnread());
		Person p1 = personDio.findById(data.personList.get(1));
		Assertions.assertEquals(1, p1.getUnread());
		PersonRoomRelate r0 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(0)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(0, r0.getUnread());
		Assertions.assertEquals("service test", r0.getLastMessage());
		PersonRoomRelate r1 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(0, r1.getUnread());
		Assertions.assertEquals("service test", r1.getLastMessage());

		Thread.sleep(100);

		personDio.update(data.personList.get(0));
		personDio.update(data.personList.get(1));
		personDio.update(data.personList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(3));
		personRoomRelateDio.update(data.personRoomRelateList.get(4));

	}

	@Test
	public void sendWithServerHistory() throws FailException, ProjException {
		config.setServerHistory(true);

		History e = Builder.set("sender", data.personList.get(0)).set("room", data.roomList.get(1)).set("v", "service test").to(new History());
		List<History> list = service.send(e);
		Assertions.assertEquals(3, list.size());

		// 验证数字
		Person p1 = personDio.findById(data.personList.get(0));
		Assertions.assertEquals(0, p1.getUnread());
		Person p2 = personDio.findById(data.personList.get(1));
		Assertions.assertEquals(2, p2.getUnread());
		PersonRoomRelate r1 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(0)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(0, r1.getUnread());
		Assertions.assertEquals("service test", r1.getLastMessage());
		PersonRoomRelate r2 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(1, r2.getUnread());
		Assertions.assertEquals("service test", r2.getLastMessage());

		personDio.update(data.personList.get(0));
		personDio.update(data.personList.get(1));
		personDio.update(data.personList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(3));
		personRoomRelateDio.update(data.personRoomRelateList.get(4));
		historyDio.deleteByIdList(list.stream().map(h -> h.getId().toString()).toList());
	}

	@Test
	public void readWithServerHistory() throws FailException, ProjException {
		config.setServerHistory(true);

		History e = Builder.set("sender", data.personList.get(0)).set("room", data.roomList.get(1)).set("v", "service test").to(new History());
		List<History> list = service.send(e);
		Assertions.assertEquals(3, list.size());

		// 自己 已读
		List<History> h1List = service.read(new SearchCondition(), data.personList.get(0), data.roomList.get(1));
		Assertions.assertEquals(0, h1List.size());
		List<History> h1AllList = historyDio.find(new SearchCondition(), Builder.set("person", data.personList.get(0)).set("room", data.roomList.get(1)).to(new History()));
		Assertions.assertEquals(1, h1AllList.size());
		Assertions.assertEquals(false, h1AllList.get(0).getUnread());
		historyDio.deleteByIdList(h1AllList.stream().map(h -> h.getId().toString()).toList());


		// 对方 第一次未读，之后已读
		List<History> h2List = service.read(new SearchCondition(), data.personList.get(1), data.roomList.get(1));
		Assertions.assertEquals(1, h2List.size());
		Assertions.assertEquals(true, h2List.get(0).getUnread());
		List<History> h2ListAgain = service.read(new SearchCondition(), data.personList.get(1), data.roomList.get(1));
		Assertions.assertEquals(0, h2ListAgain.size());
		historyDio.deleteByIdList(h2List.stream().map(h -> h.getId().toString()).toList());
		List<History> h3List = service.read(new SearchCondition(), data.personList.get(2), data.roomList.get(1));
		Assertions.assertEquals(1, h3List.size());
		historyDio.deleteByIdList(h3List.stream().map(h -> h.getId().toString()).toList());

		// 验证数字
		Person p1 = personDio.findById(data.personList.get(0));
		Assertions.assertEquals(0, p1.getUnread());
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
		personRoomRelateDio.update(data.personRoomRelateList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(3));
		personRoomRelateDio.update(data.personRoomRelateList.get(4));

	}

	@Test
	public void clearByRoomServerHistory() throws FailException, ProjException, InterruptedException {
		// todo 现在要看这条记录是什么时候进来的
		List<History> indbHistoryList1 = historyDio.find(new SearchCondition(), Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new History()));
		Assertions.assertEquals(0, indbHistoryList1.size());
		config.setServerHistory(true);
		History history = Builder.set("sender", data.personList.get(0)).set("room", data.roomList.get(1)).set("v", "service test").to(new History());
		List<History> historyList = service.send(history);

		roomService.leaveRoom(data.personList.get(1), data.roomList.get(1));
		Thread.sleep(10);
		Person p1 = personDio.findById(data.personList.get(1));
		Assertions.assertEquals(1, p1.getUnread());
		PersonRoomRelate indb = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(0, indb.getUnread());
		List<History> indbHistoryList = historyDio.find(new SearchCondition(), Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new History()));
		Assertions.assertEquals(1, indbHistoryList.size());
		Assertions.assertEquals(false, indbHistoryList.get(0).getUnread());

		historyDio.deleteByIdList(historyList.stream().map(h -> h.getId().toString()).toList());
		personDio.update(data.personList.get(0));
		personDio.update(data.personList.get(1));
		personDio.update(data.personList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(3));
		personRoomRelateDio.update(data.personRoomRelateList.get(4));
	}

	@Test
	public void clearByRoomNoServerHistory() throws FailException, ProjException, InterruptedException {
		config.setServerHistory(false);
		History history = Builder.set("sender", data.personList.get(0)).set("room", data.roomList.get(1)).set("v", "service test").to(new History());
		List<History> historyList = service.send(history);

		roomService.leaveRoom(data.personList.get(1), data.roomList.get(1));
		Thread.sleep(10);
		Person p1 = personDio.findById(data.personList.get(1));
		Assertions.assertEquals(1, p1.getUnread());
		PersonRoomRelate indb = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(0, indb.getUnread());
		List<History> indbHistoryList = historyDio.find(new SearchCondition(), Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new History()));
		Assertions.assertEquals(0, indbHistoryList.size());
		roomService.leaveRoom(data.personList.get(2), data.roomList.get(1));
		Thread.sleep(10);

		personDio.update(data.personList.get(0));
		personDio.update(data.personList.get(1));
		personDio.update(data.personList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(3));
		personRoomRelateDio.update(data.personRoomRelateList.get(4));

	}

	@Test
	public void readByIdServerHistory() throws FailException, ProjException {
		config.setServerHistory(true);
		History history = Builder.set("sender", data.personList.get(0)).set("room", data.roomList.get(1)).set("v", "service test").to(new History());
		List<History> historyList = service.send(history);

		for (History e : historyList) {
			if (e.getPerson().getId() == data.personList.get(0).getId()) continue;
			// 测试多次读取
			service.readById(e);
			service.readById(e);
		}
		Person p1 = personDio.findById(data.personList.get(1));
		Assertions.assertEquals(1, p1.getUnread());
		Person p2 = personDio.findById(data.personList.get(2));
		Assertions.assertEquals(0, p2.getUnread());
		PersonRoomRelate r1 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(0, r1.getUnread());
		PersonRoomRelate r1Room0 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(0)).to(new PersonRoomRelate()));
		Assertions.assertEquals(1, r1Room0.getUnread());
		PersonRoomRelate r2 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(2)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(0, r2.getUnread());
		List<History> h1List = historyDio.find(new SearchCondition(), Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new History()));
		Assertions.assertEquals(1, h1List.size());
		Assertions.assertEquals(false, h1List.get(0).getUnread());

		historyDio.deleteByIdList(historyList.stream().map(h -> h.getId().toString()).toList());
		personDio.update(data.personList.get(0));
		personDio.update(data.personList.get(1));
		personDio.update(data.personList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(3));
		personRoomRelateDio.update(data.personRoomRelateList.get(4));
	}

	@Test
	public void readByIdNoServerHistory() throws FailException, ProjException {
		config.setServerHistory(false);
		History history = Builder.set("sender", data.personList.get(0)).set("room", data.roomList.get(1)).set("v", "service test").to(new History());
		List<History> historyList = service.send(history);

		for (History e : historyList) {
			if (e.getPerson().getId() == data.personList.get(0).getId()) continue;
			service.readById(e);
		}
		Person p1 = personDio.findById(data.personList.get(1));
		Assertions.assertEquals(1, p1.getUnread());
		Person p2 = personDio.findById(data.personList.get(2));
		Assertions.assertEquals(0, p2.getUnread());
		PersonRoomRelate r1 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(0, r1.getUnread());
		PersonRoomRelate r1Room0 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(0)).to(new PersonRoomRelate()));
		Assertions.assertEquals(1, r1Room0.getUnread());
		PersonRoomRelate r2 = personRoomRelateDio.findByKey(Builder.set("person", data.personList.get(2)).set("room", data.roomList.get(1)).to(new PersonRoomRelate()));
		Assertions.assertEquals(0, r2.getUnread());
		List<History> h1List = historyDio.find(new SearchCondition(), Builder.set("person", data.personList.get(1)).set("room", data.roomList.get(1)).to(new History()));
		Assertions.assertEquals(0, h1List.size());

		personDio.update(data.personList.get(0));
		personDio.update(data.personList.get(1));
		personDio.update(data.personList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(3));
		personRoomRelateDio.update(data.personRoomRelateList.get(4));
	}

	@BeforeEach
	public void init() {
		data.init();
	}
	@AfterEach
	public void check() {
		data.check();
	}
}

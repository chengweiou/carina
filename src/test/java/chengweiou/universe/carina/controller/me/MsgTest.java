package chengweiou.universe.carina.controller.me;


import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.util.GsonUtil;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.config.ProjConfig;
import chengweiou.universe.carina.data.Data;
import chengweiou.universe.carina.model.ProjectRestCode;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.Room;
import chengweiou.universe.carina.service.history.HistoryDio;
import chengweiou.universe.carina.service.person.PersonDio;
import chengweiou.universe.carina.service.room.PersonRoomRelateDio;

@SpringBootTest
@ActiveProfiles("test")
public class MsgTest {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private Data data;
	private Account loginAccount;
	@Autowired
	private HistoryDio historyDio;
	@Autowired
	private PersonDio personDio;
	@Autowired
	private PersonRoomRelateDio personRoomRelateDio;
	@Autowired
	private ProjConfig config;
	@Test
	public void sendAndRead() throws Exception {
		config.setServerHistory(false);

		String result = mvc.perform(MockMvcRequestBuilders.post("/me/msg")
				.header("loginAccount", GsonUtil.create().toJson(loginAccount))
				.param("room.id", "2").param("v", "ctrl save")
		).andReturn().getResponse().getContentAsString();
		Rest<Long> saveRest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode());

		result = mvc.perform(MockMvcRequestBuilders.get("/me/msg")
				.header("loginAccount", GsonUtil.create().toJson(loginAccount))
				.param("room.id", "2").param("v", "ctrl save")
		).andReturn().getResponse().getContentAsString();
		Rest<List> findRest = Rest.from(result, List.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode());
		Assertions.assertEquals(0, findRest.getData().size());

		List<History> deleteList = historyDio.find(new SearchCondition(), Builder.set("room", Builder.set("id", 2).to(new Room())).to(new History()));
		historyDio.delete(deleteList);
		personDio.update(data.personList.get(0));
		personDio.update(data.personList.get(1));
		personRoomRelateDio.update(data.personRoomRelateList.get(2));
		personRoomRelateDio.update(data.personRoomRelateList.get(3));
		personRoomRelateDio.update(data.personRoomRelateList.get(4));
	}

	@Test
	public void sendFailRoomNotExists() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/me/msg")
				.header("loginAccount", GsonUtil.create().toJson(loginAccount))
				.param("room.id", "10").param("v", "ctrl save")
		).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result, ProjectRestCode.class);
		Assertions.assertEquals(ProjectRestCode.NOT_EXISTS, rest.getCode());
	}

	@Test
	public void readById() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/me/msg/" + data.historyList.get(0).getId() + "/read")
				.header("loginAccount", GsonUtil.create().toJson(loginAccount))
		).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result);
	}

	@Test
	public void readByIdFailNotInRoom() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/me/msg/10/read")
				.header("loginAccount", GsonUtil.create().toJson(loginAccount))
		).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.FAIL, rest.getCode());
	}

	@BeforeEach
	public void before() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		loginAccount = Builder.set("person", Builder.set("id", 1L).to(new Person()))
				.set("extra", "aaa")
				.to(new Account());
	}
	@BeforeEach
	public void init() {
		data.init();
	}
}

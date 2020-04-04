package com.chengweiou.universe.carina.controller.me;


import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import com.chengweiou.universe.carina.model.ProjectRestCode;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.history.History;
import com.chengweiou.universe.carina.model.entity.room.Room;
import com.chengweiou.universe.carina.service.history.HistoryDio;
import com.google.gson.Gson;
import com.chengweiou.universe.carina.base.converter.Account;
import com.chengweiou.universe.carina.data.Data;
import com.chengweiou.universe.carina.model.entity.person.Person;
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

import java.util.List;

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
	@Test
	public void send() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/me/msg")
				.header("loginAccount", new Gson().toJson(loginAccount))
				.param("room.id", "2").param("v", "ctrl save")
		).andReturn().getResponse().getContentAsString();
		Rest<Long> saveRest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode());

		List<History> deleteList = historyDio.find(new SearchCondition(), Builder.set("room", Builder.set("id", 2).to(new Room())).to(new History()));
		historyDio.delete(deleteList);
		// todo 还原person表里面unread， personroomrelate.unread, message等
	}

	@Test
	public void sendFailRoomNotExists() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/me/msg")
				.header("loginAccount", new Gson().toJson(loginAccount))
				.param("room.id", "10").param("v", "ctrl save")
		).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result, ProjectRestCode.class);
		Assertions.assertEquals(ProjectRestCode.NOT_EXISTS, rest.getCode());
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

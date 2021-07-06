package chengweiou.universe.carina.controller.me;


import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.util.GsonUtil;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.data.Data;
import chengweiou.universe.carina.model.entity.person.Person;
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
public class FriendTest {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private Data data;
	private Account loginAccount;

	@Test
	public void saveDelete() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/me/friend")
				.header("loginAccount", GsonUtil.create().toJson(loginAccount))
				.param("target.id", "30")
		).andReturn().getResponse().getContentAsString();
		Rest<Long> saveRest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode(), saveRest.getMessage());

		result = mvc.perform(MockMvcRequestBuilders.delete("/me/friend/" + saveRest.getData())
				.header("loginAccount", GsonUtil.create().toJson(loginAccount))
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> delRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, delRest.getCode());
	}

	@Test
	public void saveDeleteFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/me/friend")
				.header("loginAccount", GsonUtil.create().toJson(loginAccount))
		).andReturn().getResponse().getContentAsString();
		Rest<Long> saveRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.PARAM, saveRest.getCode());
	}

	@Test
	public void check() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/me/friend/check")
				.header("loginAccount", GsonUtil.create().toJson(loginAccount))
				.param("target.id", "30")
		).andReturn().getResponse().getContentAsString();
		Rest<Long> saveRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode());
		Assertions.assertEquals(false, saveRest.getData());
	}

	@Test
	public void updateFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/me/friend")
				.header("loginAccount", GsonUtil.create().toJson(loginAccount))
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.PARAM, rest.getCode());
	}
	@Test
	public void count() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/me/friend/count")
				.header("loginAccount", GsonUtil.create().toJson(loginAccount))
		).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(1, rest.getData());
	}

	@Test
	public void find() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/me/friend")
				.header("loginAccount", GsonUtil.create().toJson(loginAccount))
				.param("target.id", "chi")
		).andReturn().getResponse().getContentAsString();
		Rest<List<Account>> rest = Rest.from(result, List.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(1, rest.getData().size());
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

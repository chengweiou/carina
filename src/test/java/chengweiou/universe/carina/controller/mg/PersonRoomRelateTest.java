package chengweiou.universe.carina.controller.mg;


import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.data.Data;
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
public class PersonRoomRelateTest {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private Data data;
	@Test
	public void saveDelete() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/mg/personRoomRelate")
				.header("inServer", "true")
				.param("room.id", "33").param("person.id", "1")
			).andReturn().getResponse().getContentAsString();
		Rest<Long> saveRest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode());

		result = mvc.perform(MockMvcRequestBuilders.delete("/mg/personRoomRelate/" + saveRest.getData())
				.header("inServer", "true")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> delRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, delRest.getCode());
	}

	@Test
	public void saveDeleteFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.post("/mg/personRoomRelate")
				.header("inServer", "true")
				.param("name", "controler test")
		).andReturn().getResponse().getContentAsString();
		Rest<Long> saveRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.PARAM, saveRest.getCode());
	}

	@Test
	public void update() throws Exception {
		String old = data.personRoomRelateList.get(0).getName();

		String result = mvc.perform(MockMvcRequestBuilders.put("/mg/personRoomRelate/" + data.personRoomRelateList.get(0).getId())
				.header("inServer", "true")
				.param("name", "control update")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(true, rest.getData());

		mvc.perform(MockMvcRequestBuilders.put("/mg/personRoomRelate/" + data.personRoomRelateList.get(0).getId())
				.header("inServer", "true")
				.param("name", old)
		).andReturn().getResponse().getContentAsString();
	}

	@Test
	public void updateFail() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.put("/mg/personRoomRelate/" + data.personRoomRelateList.get(0).getId())
				.header("inServer", "true")
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> rest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.PARAM, rest.getCode());
	}

	@Test
	public void count() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/mg/personRoomRelate/count")
				.header("inServer", "true")
		).andReturn().getResponse().getContentAsString();
		Rest<Long> rest = Rest.from(result, Long.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(5, rest.getData());
	}

	@Test
	public void find() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/mg/personRoomRelate")
				.header("inServer", "true")
		).andReturn().getResponse().getContentAsString();
		Rest<List<Account>> rest = Rest.from(result, List.class);
		Assertions.assertEquals(BasicRestCode.OK, rest.getCode());
		Assertions.assertEquals(5, rest.getData().size());
	}

	@BeforeEach
	public void before() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	@BeforeEach
	public void init() {
		data.init();
	}
}

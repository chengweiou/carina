package chengweiou.universe.carina.controller.me;


import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.data.Data;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.Room;
import chengweiou.universe.carina.service.room.PersonRoomRelateDio;
import com.google.gson.Gson;
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

@SpringBootTest
@ActiveProfiles("test")
public class RoomTest {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private Data data;
	private Account loginAccount;
	@Autowired
	private PersonRoomRelateDio personRoomRelateDio;

	@Test
	public void enterRoom() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/me/room")
				.header("loginAccount", new Gson().toJson(loginAccount))
				.param("personIdList", "11")
			).andReturn().getResponse().getContentAsString();
		Rest<Room> saveRest = Rest.from(result, Room.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode());

		result = mvc.perform(MockMvcRequestBuilders.delete("/mg/room/" + saveRest.getData().getId())
		).andReturn().getResponse().getContentAsString();
		Rest<Boolean> delRest = Rest.from(result);
		Assertions.assertEquals(BasicRestCode.OK, delRest.getCode());
		// person 不存在，relate 表没有数据存入，不用删

	}

	@BeforeEach
	public void before() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		loginAccount = Builder.set("person", Builder.set("id", 10L).to(new Person()))
				.set("extra", "aaa")
				.to(new Account());
	}
	@BeforeEach
	public void init() {
		data.init();
	}
}

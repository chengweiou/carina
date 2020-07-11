package chengweiou.universe.carina.controller.me;


import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.data.Data;
import chengweiou.universe.carina.model.entity.person.Person;
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
public class PersonTest {
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private Data data;
	private Account loginAccount;

	@Test
	public void get() throws Exception {
		String result = mvc.perform(MockMvcRequestBuilders.get("/me")
				.header("loginAccount", new Gson().toJson(loginAccount))
		).andReturn().getResponse().getContentAsString();
		Rest<Person> saveRest = Rest.from(result, Person.class);
		Assertions.assertEquals(BasicRestCode.OK, saveRest.getCode(), saveRest.getMessage());
		Assertions.assertEquals(data.personList.get(0).getName(), saveRest.getData().getName());
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

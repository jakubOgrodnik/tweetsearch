package pl.kuba.search.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.kuba.SpringTutorialApplication;
import pl.kuba.search.StubTwitterSearchConfig;
import pl.kuba.user.User;
import pl.kuba.user.UserRepository;
import pl.kuba.utils.JsonUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {
        SpringTutorialApplication.class,
        StubTwitterSearchConfig.class
})
@WebAppConfiguration
public class SearchApiControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        userRepository.reset(new User("robert@spring.io"));
    }
    @Test
    public void should_search() throws Exception {
        this.mockMvc.perform(
                get("/api/search/mixed;keywords=spring").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].text", is("Treść tweeta")))
                .andExpect(jsonPath("$[1].text", is("Treść innego tweeta")));
    }
    @Test
    public void should_list_users() throws Exception {
        this.mockMvc.perform(
                get("/api/users").accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", is("robert@spring.io")));
    }
    @Test
    public void should_create_new_user() throws Exception {
        User user = new User("janusz@spring.io");
        this.mockMvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(user))
        )
                .andExpect(status().isCreated());
        assertThat(userRepository.findAll())
                .extracting(User::getEmail)
                .containsOnly("robert@spring.io", "janusz@spring.io");
    }
    @Test
    public void should_delete_user() throws Exception {
        this.mockMvc.perform(
                delete("/api/user/robert@spring.io")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
        assertThat(userRepository.findAll()).hasSize(0);
    }
    @Test
    public void should_return_not_found_when_deleting_unknown_user() throws Exception {
        this.mockMvc.perform(
                delete("/api/user/nie-znaleziony@mail.com")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());
    }
    @Test
    public void put_should_update_existing_user() throws Exception {
        User user = new User("nowy@spring.io");
        this.mockMvc.perform(
                put("/api/user/robert@spring.io")
                        .content(JsonUtils.toJson(user))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
        assertThat(userRepository.findAll())
                .extracting(User::getEmail)
                .containsOnly("robert@spring.io");
    }
}
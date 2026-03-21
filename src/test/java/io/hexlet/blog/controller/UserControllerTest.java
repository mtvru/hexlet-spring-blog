package io.hexlet.blog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.blog.model.User;
import io.hexlet.blog.repository.UserRepository;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Faker faker;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper om;

    @Test
    public void testIndex() throws Exception {
        User user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        userRepository.save(user);
        User user2 = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        userRepository.save(user2);
        MvcResult result = this.mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        assertThatJson(body);
    }

    @Test
    public void testShow() throws Exception {
        User user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        user = userRepository.save(user);
        MvcResult result = this.mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        assertThatJson(body);
    }

    @Test
    public void testCreate() throws Exception {
        final String email = "john@example.com";
        User user = Instancio.of(User.class)
                .supply(Select.field(User::getEmail), () -> email)
                .create();
        MockHttpServletRequestBuilder request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(user));
        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value(email))
                .andReturn();
        String body = result.getResponse().getContentAsString();
        System.out.println("Compare response testCreate: ");
        System.out.println(om.writeValueAsString(user));
        System.out.println(body);
    }

    @Test
    public void testUpdate() throws Exception {
        User user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        userRepository.save(user);

        HashMap<String, String> data = new HashMap<>();
        data.put("firstName", "Mike");
        data.put("email", user.getEmail());

        MockHttpServletRequestBuilder request = put("/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        user = userRepository.findById(user.getId()).get();
        assertThat(user.getFirstName()).isEqualTo(("Mike"));
    }

    @Test
    public void testDelete() throws Exception {
        User user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        userRepository.save(user);
        MockHttpServletRequestBuilder request = delete("/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        boolean deleted = userRepository.findById(user.getId()).isEmpty();
        assertThat(deleted).isEqualTo(true);
    }
}

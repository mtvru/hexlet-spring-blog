package io.hexlet.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.blog.model.Post;
import io.hexlet.blog.model.User;
import io.hexlet.blog.repository.PostRepository;
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

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Faker faker;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper om;

    @Test
    public void testIndex() throws Exception {
        User user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getPosts))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        userRepository.save(user);
        Post post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getAuthor))
                .ignore(Select.field(Post::getTags))
                .create();
        post.setAuthor(user);
        postRepository.save(post);
        Post post2 = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getAuthor))
                .ignore(Select.field(Post::getTags))
                .create();
        post2.setAuthor(user);
        postRepository.save(post2);
        MvcResult result = this.mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        assertThatJson(body);
    }

    @Test
    public void testShow() throws Exception {
        User user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getPosts))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        userRepository.save(user);
        Post post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getAuthor))
                .ignore(Select.field(Post::getTags))
                .create();
        post.setAuthor(user);
        post = postRepository.save(post);
        MvcResult result = this.mockMvc.perform(get("/api/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        assertThatJson(body);
    }


    @Test
    public void testUpdate() throws Exception {
        User user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getPosts))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        userRepository.save(user);
        Post post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getAuthor))
                .ignore(Select.field(Post::getTags))
                .create();
        post.setAuthor(user);
        post = postRepository.save(post);
        HashMap<String, String> data = new HashMap<>();
        data.put("title", "Mike title");
        data.put("content", "Mike content");
        MockHttpServletRequestBuilder request = put("/api/posts/" + post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk());
        post = postRepository.findById(post.getId()).get();
        assertThat(post.getName()).isEqualTo(("Mike title"));
        assertThat(post.getContent()).isEqualTo(("Mike content"));
    }

    @Test
    public void testDelete() throws Exception {
        User user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getPosts))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
        userRepository.save(user);
        Post post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .ignore(Select.field(Post::getAuthor))
                .ignore(Select.field(Post::getTags))
                .create();
        post.setAuthor(user);
        postRepository.save(post);
        MockHttpServletRequestBuilder request = delete("/api/posts/" + post.getId())
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        boolean deleted = postRepository.findById(post.getId()).isEmpty();
        assertThat(deleted).isEqualTo(true);
    }
}

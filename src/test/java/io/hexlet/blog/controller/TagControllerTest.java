package io.hexlet.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.blog.model.Tag;
import io.hexlet.blog.repository.TagRepository;
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
public class TagControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Faker faker;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ObjectMapper om;

    @Test
    public void testIndex() throws Exception {
        Tag tag = Instancio.of(Tag.class)
            .ignore(Select.field(Tag::getId))
            .ignore(Select.field(Tag::getPosts))
            .create();
        tagRepository.save(tag);
        MvcResult result = this.mockMvc.perform(get("/api/tags"))
            .andExpect(status().isOk())
            .andReturn();
        String body = result.getResponse().getContentAsString();
        assertThatJson(body);
    }

    @Test
    public void testShow() throws Exception {
        Tag tag = Instancio.of(Tag.class)
            .ignore(Select.field(Tag::getId))
            .ignore(Select.field(Tag::getPosts))
            .create();
        tagRepository.save(tag);
        MvcResult result = this.mockMvc.perform(get("/api/tags/{id}", tag.getId()))
            .andExpect(status().isOk())
            .andReturn();
        String body = result.getResponse().getContentAsString();
        assertThatJson(body);
    }

    @Test
    public void testCreate() throws Exception {
        HashMap<String, String> data = new HashMap<>();
        String name = faker.lorem().word();
        data.put("name", name);
        MockHttpServletRequestBuilder request = post("/api/tags")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(data));
        MvcResult result = mockMvc.perform(request)
            .andExpect(status().isCreated())
            .andReturn();
        String body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
            v -> v.node("name").isEqualTo(name)
        );
    }

    @Test
    public void testUpdate() throws Exception {
        Tag tag = Instancio.of(Tag.class)
            .ignore(Select.field(Tag::getId))
            .ignore(Select.field(Tag::getPosts))
            .create();
        tagRepository.save(tag);
        HashMap<String, String> data = new HashMap<>();
        String name = faker.lorem().word();
        data.put("name", name);
        MockHttpServletRequestBuilder request = put("/api/tags/{id}", tag.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(data));
        mockMvc.perform(request)
            .andExpect(status().isOk());
        tag = tagRepository.findById(tag.getId()).get();
        assertThat(tag.getName()).isEqualTo(name);
    }

    @Test
    public void testDelete() throws Exception {
        Tag tag = Instancio.of(Tag.class)
            .ignore(Select.field(Tag::getId))
            .ignore(Select.field(Tag::getPosts))
            .create();
        tagRepository.save(tag);
        MockHttpServletRequestBuilder request = delete("/api/tags/{id}", tag.getId());
        mockMvc.perform(request)
            .andExpect(status().isNoContent());
        assertThat(tagRepository.findById(tag.getId())).isEmpty();
    }

    @Test
    public void testCreateWithInvalidData() throws Exception {
        // Blank name
        HashMap<String, String> data1 = new HashMap<>();
        data1.put("name", "");
        mockMvc.perform(post("/api/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data1)))
            .andExpect(status().isUnprocessableEntity());

        // Name too short
        HashMap<String, String> data2 = new HashMap<>();
        data2.put("name", "a");
        mockMvc.perform(post("/api/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data2)))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testUpdateWithInvalidData() throws Exception {
        Tag tag = Instancio.of(Tag.class)
            .ignore(Select.field(Tag::getId))
            .ignore(Select.field(Tag::getPosts))
            .create();
        tagRepository.save(tag);

        HashMap<String, String> data = new HashMap<>();
        data.put("name", ""); // Blank name

        mockMvc.perform(put("/api/tags/{id}", tag.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data)))
            .andExpect(status().isUnprocessableEntity());
    }
}

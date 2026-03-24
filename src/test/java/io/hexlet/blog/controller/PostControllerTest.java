package io.hexlet.blog.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.blog.dto.PostDTO;
import io.hexlet.blog.model.Post;
import io.hexlet.blog.model.User;
import io.hexlet.blog.model.Tag;
import io.hexlet.blog.repository.PostRepository;
import io.hexlet.blog.repository.TagRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private TagRepository tagRepository;
    @Autowired
    private ObjectMapper om;

    @Test
    @Transactional
    public void testIndex() throws Exception {
        User user = Instancio.of(User.class)
            .ignore(Select.field(User::getId))
            .ignore(Select.field(User::getPosts))
            .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
            .create();
        userRepository.save(user);
        Tag tag = Instancio.of(Tag.class)
            .ignore(Select.field(Tag::getId))
            .ignore(Select.field(Tag::getPosts))
            .create();
        tagRepository.save(tag);
        Post post = Instancio.of(Post.class)
            .ignore(Select.field(Post::getId))
            .ignore(Select.field(Post::getAuthor))
            .ignore(Select.field(Post::getTags))
            .supply(Select.field(Post::isPublished), () -> true)
            .create();

        post.setAuthor(user);
        post.addTag(tag);
        postRepository.save(post);

        MvcResult result = this.mockMvc.perform(get("/api/posts"))
            .andExpect(status().isOk())
            .andReturn();

        String body = result.getResponse().getContentAsString();
        Map<String, Object> responsePage = om.readValue(body, new TypeReference<>() {});
        List<PostDTO> posts = om.convertValue(responsePage.get("content"), new TypeReference<>() {
        });
        assertThat(posts).anySatisfy(postDto -> {
            assertThat(postDto.getAuthorId()).isEqualTo(user.getId());
            assertThat(postDto.getTitle()).isEqualTo(post.getName());
            assertThat(postDto.getTags()).anySatisfy(tagDto -> {
                assertThat(tagDto.getName()).isEqualTo(tag.getName());
                assertThat(tagDto.getId()).isEqualTo(tag.getId());
            });
        });
    }

    @Test
    @Transactional
    public void testShow() throws Exception {
        User user = Instancio.of(User.class)
            .ignore(Select.field(User::getId))
            .ignore(Select.field(User::getPosts))
            .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
            .create();
        userRepository.save(user);
        Tag tag = Instancio.of(Tag.class)
            .ignore(Select.field(Tag::getId))
            .ignore(Select.field(Tag::getPosts))
            .create();
        tagRepository.save(tag);
        Post post = Instancio.of(Post.class)
            .ignore(Select.field(Post::getId))
            .ignore(Select.field(Post::getAuthor))
            .ignore(Select.field(Post::getTags))
            .create();
        post.setAuthor(user);
        post.addTag(tag);
        postRepository.save(post);
        MvcResult result = this.mockMvc.perform(get("/api/posts/" + post.getId()))
            .andExpect(status().isOk())
            .andReturn();
        String body = result.getResponse().getContentAsString();
        PostDTO resultPost = om.readValue(body, PostDTO.class);
        assertThat(resultPost.getTitle()).isEqualTo(post.getName());
        assertThat(resultPost.getContent()).isEqualTo(post.getContent());
        assertThat(resultPost.getAuthorId()).isEqualTo(user.getId());
        assertThat(resultPost.getTags()).anySatisfy(tagDto -> {
            assertThat(tagDto.getName()).isEqualTo(tag.getName());
            assertThat(tagDto.getId()).isEqualTo(tag.getId());
        });
    }

    @Test
    @Transactional
    public void testCreate() throws Exception {
        User user = Instancio.of(User.class)
            .ignore(Select.field(User::getId))
            .ignore(Select.field(User::getPosts))
            .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
            .create();
        userRepository.save(user);
        Tag tag = Instancio.of(Tag.class)
            .ignore(Select.field(Tag::getId))
            .ignore(Select.field(Tag::getPosts))
            .create();
        tagRepository.save(tag);

        HashMap<String, Object> data = new HashMap<>();
        data.put("title", "New Mike Title");
        data.put("content", "New Mike Content that is long enough");
        data.put("authorId", user.getId());
        data.put("tagIds", List.of(tag.getId()));

        MockHttpServletRequestBuilder request = post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(data));

        mockMvc.perform(request)
            .andExpect(status().isCreated())
            .andReturn();

        Post post = postRepository.findAll().stream()
            .filter(p -> p.getName().equals("New Mike Title"))
            .findFirst().orElseThrow();

        assertThat(post.getContent()).isEqualTo("New Mike Content that is long enough");
        assertThat(post.getAuthor().getId()).isEqualTo(user.getId());
        assertThat(post.getTags()).containsExactly(tag);
    }

    @Test
    @Transactional
    public void testUpdate() throws Exception {
        User user = Instancio.of(User.class)
            .ignore(Select.field(User::getId))
            .ignore(Select.field(User::getPosts))
            .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
            .create();
        userRepository.save(user);
        final boolean published = true;
        Post post = Instancio.of(Post.class)
            .ignore(Select.field(Post::getId))
            .ignore(Select.field(Post::getAuthor))
            .ignore(Select.field(Post::getTags))
            .supply(Select.field(Post::isPublished), () -> published)
            .create();
        post.setAuthor(user);
        post = postRepository.save(post);
        Tag tag = Instancio.of(Tag.class)
            .ignore(Select.field(Tag::getId))
            .ignore(Select.field(Tag::getPosts))
            .create();
        tagRepository.save(tag);

        Map<String, Object> data = new HashMap<>();
        String newTitle = "Mike title";
        String newContent = "Mike content";
        data.put("title", newTitle);
        data.put("content", newContent);
        data.put("tagIds", List.of(tag.getId()));

        MockHttpServletRequestBuilder request = put("/api/posts/" + post.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(data));
        mockMvc.perform(request)
            .andExpect(status().isOk());
        Post updatedPost = postRepository.findById(post.getId()).get();
        assertThat(updatedPost.getName()).isEqualTo(newTitle);
        assertThat(updatedPost.getContent()).isEqualTo(newContent);
        assertThat(updatedPost.getTags()).containsExactly(tag);
        assertThat(updatedPost.isPublished()).isFalse();
    }

    @Test
    @Transactional
    public void testPatch() throws Exception {
        User user = Instancio.of(User.class)
            .ignore(Select.field(User::getId))
            .ignore(Select.field(User::getPosts))
            .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
            .create();
        userRepository.save(user);
        final boolean published = true;
        Post post = Instancio.of(Post.class)
            .ignore(Select.field(Post::getId))
            .ignore(Select.field(Post::getAuthor))
            .ignore(Select.field(Post::getTags))
            .supply(Select.field(Post::isPublished), () -> published)
            .create();
        post.setAuthor(user);
        post = postRepository.save(post);

        String newTitle = "Mike patch title";
        Map<String, Object> data = new HashMap<>();
        data.put("title", newTitle);

        MockHttpServletRequestBuilder request = patch("/api/posts/" + post.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(data));
        mockMvc.perform(request)
            .andExpect(status().isOk());
        Post patchedPost = postRepository.findById(post.getId()).get();
        assertThat(patchedPost.getName()).isEqualTo(newTitle);
        assertThat(patchedPost.getContent()).isEqualTo(post.getContent());
        assertThat(patchedPost.isPublished()).isEqualTo(published);
        assertThat(patchedPost.getTags()).isEmpty();
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

    @Test
    @Transactional
    public void testFilter() throws Exception {
        User user1 = Instancio.of(User.class)
            .ignore(Select.field(User::getId))
            .ignore(Select.field(User::getPosts))
            .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
            .create();
        userRepository.save(user1);
        User user2 = Instancio.of(User.class)
            .ignore(Select.field(User::getId))
            .ignore(Select.field(User::getPosts))
            .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
            .create();
        userRepository.save(user2);

        Post post1 = Instancio.of(Post.class)
            .ignore(Select.field(Post::getId))
            .ignore(Select.field(Post::getAuthor))
            .ignore(Select.field(Post::getTags))
            .supply(Select.field(Post::getName), () -> "Unique title")
            .create();
        post1.setAuthor(user1);
        postRepository.save(post1);

        Post post2 = Instancio.of(Post.class)
            .ignore(Select.field(Post::getId))
            .ignore(Select.field(Post::getAuthor))
            .ignore(Select.field(Post::getTags))
            .supply(Select.field(Post::getName), () -> "Another title")
            .create();
        post2.setAuthor(user2);
        postRepository.save(post2);

        MvcResult result = this.mockMvc.perform(get("/api/posts/filter?titleCont=niqu"))
            .andExpect(status().isOk())
            .andReturn();
        String body = result.getResponse().getContentAsString();
        Map<String, Object> responsePage = om.readValue(body, new TypeReference<>() {});
        List<PostDTO> posts = om.convertValue(responsePage.get("content"), new TypeReference<>() {});
        assertThat(posts).hasSize(1);
        assertThat(posts.getFirst().getTitle()).isEqualTo(post1.getName());

        result = this.mockMvc.perform(get("/api/posts/filter?authorId=" + user2.getId()))
            .andExpect(status().isOk())
            .andReturn();
        body = result.getResponse().getContentAsString();
        responsePage = om.readValue(body, new TypeReference<>() {});
        List<PostDTO> posts2 = om.convertValue(responsePage.get("content"), new TypeReference<>() {});
        assertThat(posts2).hasSize(1);
        assertThat(posts2.getFirst().getTitle()).isEqualTo(post2.getName());
    }

    @Test
    public void testCreateWithInvalidData() throws Exception {
        User user = Instancio.of(User.class)
            .ignore(Select.field(User::getId))
            .ignore(Select.field(User::getPosts))
            .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
            .create();
        userRepository.save(user);

        // Blank title
        Map<String, Object> data1 = new HashMap<>();
        data1.put("title", "");
        data1.put("content", "Valid content that is long enough");
        data1.put("authorId", user.getId());
        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data1)))
            .andExpect(status().isUnprocessableEntity());

        // Title too short
        Map<String, Object> data2 = new HashMap<>();
        data2.put("title", "ab");
        data2.put("content", "Valid content that is long enough");
        data2.put("authorId", user.getId());
        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data2)))
            .andExpect(status().isUnprocessableEntity());

        // Content too short
        Map<String, Object> data3 = new HashMap<>();
        data3.put("title", "Valid Title");
        data3.put("content", "short");
        data3.put("authorId", user.getId());
        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data3)))
            .andExpect(status().isUnprocessableEntity());

        // Missing authorId
        Map<String, Object> data4 = new HashMap<>();
        data4.put("title", "Valid Title");
        data4.put("content", "Valid content that is long enough");
        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data4)))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testUpdateWithInvalidData() throws Exception {
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

        Map<String, Object> data = new HashMap<>();
        data.put("title", ""); // Invalid title
        data.put("content", "Still valid content");

        mockMvc.perform(put("/api/posts/" + post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data)))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testPatchWithInvalidData() throws Exception {
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

        Map<String, Object> data = new HashMap<>();
        data.put("title", ""); // Invalid title
        data.put("content", "Still valid content");

        mockMvc.perform(patch("/api/posts/" + post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data)))
            .andExpect(status().isUnprocessableEntity());
    }
}

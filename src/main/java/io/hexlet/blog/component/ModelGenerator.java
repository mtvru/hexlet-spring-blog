package io.hexlet.blog.component;

import io.hexlet.blog.model.Post;
import io.hexlet.blog.model.User;
import io.hexlet.blog.repository.PostRepository;
import io.hexlet.blog.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

@Component
public class ModelGenerator {

    private final Faker faker;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    public ModelGenerator(Faker faker, UserRepository userRepository, PostRepository postRepository, PasswordEncoder passwordEncoder) {
        this.faker = faker;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void generateData() {
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setEmail(faker.internet().emailAddress());
            user.setPasswordDigest(passwordEncoder.encode("password"));
            userRepository.save(user);

            Post post = new Post();
            post.setName(faker.book().title());
            post.setContent(faker.lorem().paragraph());
            post.setPublished(faker.bool().bool());
            post.setAuthor(user);
            postRepository.save(post);
        }
    }
}

package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    private User user;

    private BlogPost blogPost;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setAccountStatus(AccountStatus.CONFIRMED);
        user.setEmail("aaaaaa@domain.com");

        blogPost = new BlogPost();

        blogPost.setUser(user);
        blogPost.setEntry("asdasdasd");
    }

    @Test
    void shouldSaveLikedPost() {
        entityManager.persist(user);
        entityManager.persist(blogPost);

        entityManager.flush();

        LikePost likePost = new LikePost();

        likePost.setUser(user);
        likePost.setPost(blogPost);
        LikePost persistedLikePost = likePostRepository.save(likePost);

        assertThat(persistedLikePost.getId(), notNullValue());
    }

    @Test
    void shouldAddLikedPost() {
        entityManager.persist(user);
        entityManager.persist(blogPost);
        entityManager.flush();

        LikePost likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);
        LikePost persistedLikePost = likePostRepository.save(likePost);

        entityManager.refresh(blogPost);
        assertThat(blogPost.getLikesCount(), equalTo(1));
        assertThat(blogPost.getLikes().get(0), equalTo(persistedLikePost));
    }

}
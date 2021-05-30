package edu.iis.mto.blog.domain.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;
    private final String name = "aaaaa";
    private final String email = "aaa@domain.com";
    private final String dummyValue = "<<No-Name>>";
    @BeforeEach
    void setUp() {
        repository.deleteAll();
        repository.flush();
        user = new User();
        user.setFirstName("Jan");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    void NotFind() {

        int expectedNumberOfResults = 0;
        entityManager.persist(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(" ", " ", " ");

        assertEquals(expectedNumberOfResults, users.size());
    }
    @Test
    void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        assertThat(users, hasSize(0));
    }

    @Test
    void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = repository.save(user);
        List<User> users = repository.findAll();

        assertThat(users, hasSize(1));
        assertThat(users.get(0)
                        .getEmail(),
                equalTo(persistedUser.getEmail()));
    }

    @Test
    void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        assertThat(persistedUser.getId(), notNullValue());
    }
    @Test
    void shouldFindOneUserByPartOfName() {
        User persistedUser = repository.save(user);

        final String partOfLowerCaseName = user.getFirstName().substring(0, 2).toLowerCase();
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(partOfLowerCaseName, dummyValue, "");

        assertThat(users, hasSize(1));
        assertThat(users.get(0).getEmail(), equalTo(persistedUser.getEmail()));
    }
    @Test
    void shouldFindTwoUserByPartOfEmail() {
        User user2 = new User();
        user2.setEmail("prefix" + email);
        user2.setAccountStatus(AccountStatus.NEW);

        repository.save(user);
        repository.save(user2);

        List<User> users =  repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("partOfLowerCaseName", dummyValue, "");

        assertThat(users, hasSize(2));
    }
    @Test
    void shouldFindTwoUsers() {
        User user2 = new User();
        user2.setFirstName("aaa");
        user2.setLastName(user.getLastName());
        user2.setEmail("DummyPrefix" + email);
        user2.setAccountStatus(AccountStatus.NEW);

        repository.save(user);
        repository.save(user2);

        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(user.getFirstName(), "asadasdasd", "");
        assertThat(users, hasSize(2));
    }

}

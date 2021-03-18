package telegram.currencyexchange.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import telegram.currencyexchange.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService service;

    @Test
    @DisplayName("the size of users should be increased")
    void addUser() {
        User user = new User(1,"Vasya","Kokorin",0);
        List<User> allUsers1 = service.findAllUsers();
        service.addUser(user);
        List<User> allUsers2 = service.findAllUsers();
        Assertions.assertEquals(allUsers2.size(),allUsers1.size() + 1);
    }
}
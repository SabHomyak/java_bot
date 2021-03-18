package telegram.currencyexchange;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import telegram.currencyexchange.bot.BotContext;
import telegram.currencyexchange.bot.BotState;
import telegram.currencyexchange.bot.ChatBot;
import telegram.currencyexchange.model.User;
import telegram.currencyexchange.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static telegram.currencyexchange.bot.BotContext.of;

@SpringBootTest
class CurrencyExchangeApplicationTests {
    @Autowired
    UserService userService;

    @Test
    @DisplayName("The state of the bot should increase")
    void test() {
        BotState botState = BotState.getInitialState();
        User user = new User(1,"Max","Ivanov",null);
        BotContext context = of(new ChatBot(userService), user, "/start", 1L);
        Assertions.assertNotEquals(botState.ordinal(), 1);
        botState.enter(context);
        botState.handleInput(context);
        Assertions.assertNotEquals(botState.ordinal(), 2);
    }

}

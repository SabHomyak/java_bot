package telegram.currencyexchange.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram.currencyexchange.model.User;
import telegram.currencyexchange.service.UserService;


@Component
@PropertySource("classpath:telegram.properties")
public class ChatBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LogManager.getLogger(ChatBot.class);

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;

    private final UserService userService;

    @Autowired
    public ChatBot(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if ((!update.hasMessage() || !update.getMessage().hasText()) && !update.hasCallbackQuery()) {
            return;
        }

        Message message = update.getMessage();
        String input = null;
        org.telegram.telegrambots.meta.api.objects.User from = null;
        if (message == null) {
            message = update.getCallbackQuery().getMessage();
            input = update.getCallbackQuery().getData();
            from = update.getCallbackQuery().getFrom();
        } else {
            from = message.getFrom();
            input = message.getText();
        }
        Long chatId = message.getChatId();
        User user = userService.findById(from.getId());

        BotState state = null;
        BotContext context = null;

        if (user == null) {
            state = BotState.getInitialState();

            user = new User(from.getId(), from.getFirstName(), from.getLastName(), state.ordinal());
            userService.addUser(user);

            context = BotContext.of(this, user, input, chatId);
            state.enter(context);

            LOGGER .info("New user registered: " + chatId);
        } else {
            context = BotContext.of(this, user, input, chatId);
            state = BotState.byId(user.getState());
            LOGGER.info("Update received for user: " + chatId);
        }
        state.handleInput(context);

        state = state.nextState();
        state.enter(context);
        user.setState(state.ordinal());
        userService.updateUser(user);

    }

    protected void sendMessage(Long id, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(id.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}

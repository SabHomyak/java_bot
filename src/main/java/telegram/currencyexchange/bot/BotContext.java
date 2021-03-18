package telegram.currencyexchange.bot;

import telegram.currencyexchange.model.User;

public class BotContext {
    private final ChatBot bot;
    private final User user;
    private final String input;
    private final Long chatId;

    public static BotContext of(ChatBot bot, User user, String text, Long chatId) {
        return new BotContext(bot, user, text,chatId);
    }

    private BotContext(ChatBot bot, User user, String input,Long chatId) {
        this.bot = bot;
        this.user = user;
        this.input = input;
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }

    public ChatBot getBot() {
        return bot;
    }

    public User getUser() {
        return user;
    }

    public String getInput() {
        return input;
    }
}

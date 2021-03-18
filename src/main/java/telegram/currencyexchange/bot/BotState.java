package telegram.currencyexchange.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram.currencyexchange.CurrencyApi.BankFromMinfin;
import telegram.currencyexchange.CurrencyApi.Currency;

import java.util.*;

public enum BotState {
    START {
        BotState next;

        @Override
        public void enter(BotContext context) {

        }

        @Override
        public void handleInput(BotContext context) {
            if (context.getInput().equals("/start")) {
                next = ASK_CURRENCY;
                String message = String.format("Добрый день, %s!", context.getUser().getFirstName());
                sendMessage(context, message);
            } else {
                String message = "Чтобы запустить бота напишите /start";
                sendMessage(context, message);
                next = START;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    ASK_CURRENCY {
        BotState next;

        @Override
        public void enter(BotContext context) {
            InlineKeyboardButton btn1 = new InlineKeyboardButton();
            InlineKeyboardButton btn2 = new InlineKeyboardButton();
            InlineKeyboardButton btn3 = new InlineKeyboardButton();
            btn1.setText(CurrencyE.USD.name());
            btn2.setText(CurrencyE.EUR.name());
            btn3.setText(CurrencyE.RUB.name());
            btn1.setCallbackData(CurrencyE.USD.name());
            btn2.setCallbackData(CurrencyE.EUR.name());
            btn3.setCallbackData(CurrencyE.RUB.name());
            sendButtonsInRows(context, "Выберите валюту, которую будем искать", new InlineKeyboardButton[]{btn1, btn2, btn3});
        }

        @Override
        public void handleInput(BotContext context) {
            String input = context.getInput();
            try {
                CurrencyE currency = CurrencyE.valueOf(input);
                Map<String, String> data = new HashMap<>();
                data.put("Currency", currency.name());
                storage.put(context.getUser().getId(), data);
                next = ASK_BANK;
            } catch (RuntimeException e) {
                sendMessage(context, "Пожалуйста выберите валюту из списка!");
                next = ASK_CURRENCY;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    ASK_BANK {
        BotState next;

        @Override
        public void enter(BotContext context) {
            InlineKeyboardButton[] bankButtons = Utils.createBankButtons(
                    Bank.ALFABANK,
                    Bank.OSCHADBANK,
                    Bank.OTPBANK,
                    Bank.RAYFFAYZENBANK,
                    Bank.UKRSIBANK,
                    Bank.PRIVAT24
            );
            sendButtonsInRows(context, "Выберите источник данных", bankButtons);
        }

        @Override
        public void handleInput(BotContext context) {
            String input = context.getInput();
            try {
                Bank bank = Bank.valueOf(input);
                Map<String, String> stringStringMap = storage.get(context.getUser().getId());
                stringStringMap.put("Bank", String.valueOf(bank.ordinal()));
                next = END;
            } catch (RuntimeException e) {
                sendMessage(context, "Пожалуйста выберите источник из списка!");
                next = ASK_BANK;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    END {
        @Override
        public void enter(BotContext context) {
            Map<String, String> request = storage.get(context.getUser().getId());
            String bankOrdinal = request.get("Bank");
            Bank bank = Bank.values()[Integer.parseInt(bankOrdinal)];
            String curr = request.get("Currency");
            BankFromMinfin bankFromMinfin = new BankFromMinfin(bank);
            Currency currency = bankFromMinfin.getCurrency(curr);
            storage.remove(context.getUser().getId());
            String data = "Источник:" + bank.getName() +
                    "\nвалюта:" + curr +
                    "\nпокупка:" + currency.getBuy()
                    + "\nпродажа:" + currency.getSell();
            sendMessage(context, data);
            sendMessage(context, "Спасибо за выбор нашего супер-бота!Что бы начать заново используйте команду /start");
        }

        @Override
        public void handleInput(BotContext context) {
        }

        @Override
        public BotState nextState() {
            return ASK_CURRENCY;
        }
    };
    private static BotState[] states;
    private static Map<Integer, Map<String, String>> storage = new HashMap<>();

    BotState() {
    }

    public static BotState getInitialState() {
        return byId(0);
    }

    public static BotState byId(int id) {
        if (states == null) {
            states = BotState.values();
        }
        return states[id];
    }

    protected void sendButtonsInRows(BotContext botContext, String text, InlineKeyboardButton[] buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (InlineKeyboardButton button : buttons) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            rows.add(row);
        }
        markup.setKeyboard(rows);
        SendMessage message = new SendMessage();
        message.setChatId(botContext.getChatId().toString());
        message.setText(text);
        message.setReplyMarkup(markup);
        try {
            botContext.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected void sendMessage(BotContext context, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(context.getChatId().toString());
        message.setText(text);
        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void handleInput(BotContext context) {

    }

    public abstract void enter(BotContext context);

    public abstract BotState nextState();
}

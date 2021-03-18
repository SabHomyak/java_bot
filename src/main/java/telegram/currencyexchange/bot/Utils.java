package telegram.currencyexchange.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;

public class Utils {
    public static InlineKeyboardButton[] createBankButtons(Bank ...bank){
        InlineKeyboardButton[] buttons = new InlineKeyboardButton[bank.length];
        for (int i = 0; i < bank.length; i++) {
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(bank[i].getName());
            btn.setCallbackData(bank[i].name());
            buttons[i] = btn;
        }
        return buttons;
    }
}

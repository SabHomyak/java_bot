//package telegram.currencyexchange;
//
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//public enum BotState {
//    AAA
//    protected void sendMessage(BotContext context,String text){
//        SendMessage message = new SendMessage()
//                .setChatId(context.getUser().getChatId())
//                .setText(text);
//        try{
//            context.getBot().execute(message);
//        } catch (TelegramApiException e){
//            e.printStackTrace();
//        }
//    }
//}
//

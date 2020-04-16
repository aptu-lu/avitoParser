package bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

public class TelegramBot extends TelegramLongPollingBot {
    private String botUserName = "NotificationAvitoBot";
    private String botToken = "991533128:AAEmzp9-125TNInP1cb5uT448hh2aCiVXlk";
    private ArrayList<ThreadAvitoParser> threads = new ArrayList<>();

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message.hasText()) {
            if (message.getText().equals("/start")) {
                sendMsg(message, "Скопируйте и вставьте ссылку с сайта avito с нужным товаром отсортированным по дате.\n" +
                        "Пример: \n" +
                        "https://www.avito.ru/rossiya/chasy_i_ukrasheniya/kupit-chasy-ASgBAgICAUTQAYYG?s=104&q=apple+watch" +
                        "\n Для прекращения уведомлений напишите /stop");
            }
            if (Pattern.matches("https://www.avito.ru/+.*", message.getText())) {
                try {
                    System.out.println("link detected");
                    URL url = new URL(message.getText());
                    ThreadAvitoParser threadAvitoParser = new ThreadAvitoParser(url, message);
                    threads.add(threadAvitoParser);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    sendMsg(message, "Введен не корректный url");
                }
            }
            if (message.getText().equals("/stop")) {
                Long chatId = message.getChatId();
                for (Iterator<ThreadAvitoParser> tap = threads.iterator(); tap.hasNext(); ) {
                    ThreadAvitoParser threadElement = tap.next();
                    if (threadElement.getMessage().getChatId().equals(chatId)) {
                        threadElement.setStopped(true);
                        tap.remove();
                    }
                }
                sendMsg(message, "Все оповещения отключены.");
            }
        }
    }


    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setParseMode("HTML");
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
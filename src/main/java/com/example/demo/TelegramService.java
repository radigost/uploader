package com.example.demo;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramService extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    @Autowired
    private DropboxService dropboxService;

    @Autowired
    private MessageFactory messageFactory;

    @Override
    public void onUpdateReceived(Update update) {
        try {
            var chatId = update.getMessage().getChatId().toString();
            var telegramMessage =  messageFactory.createMessage(update.getMessage());

            var filename = dropboxService.uploadAttachment(telegramMessage);
            if (filename != null){
                postMessage(chatId,String.format("File %s is uploaded", filename));
                dropboxService.appendLinkToEndOfFile(telegramMessage,filename);
            }
            var uploadedPath = dropboxService.uploadTextFile(telegramMessage.getText());
            postMessage(chatId,String.format("File %s is created", uploadedPath));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    private void postMessage(String chatId, String text){
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        execute(message);
    }

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }
}

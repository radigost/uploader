package com.example.demo;

import java.io.File;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Log4j2
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
            var telegramMessage = messageFactory.createMessage(update.getMessage());

            String filename = null;
            if (telegramMessage.getFileId() != null) {
                GetFile getFileMethod = new GetFile();
                getFileMethod.setFileId(telegramMessage.getFileId());
                var attachment = execute(getFileMethod);
                var file = downloadFile(attachment.getFilePath());
                filename = dropboxService.uploadFileToDataFolder(file, attachment.getFilePath());
                var result = String.format("File %s is uploaded", filename);
                log.info(result);
                postMessage(chatId, result);
            }
            String text = Optional.ofNullable(telegramMessage.getText()).orElse(" ");

            if (filename != null) {
                text = text + String.format("[[%s]]", filename);
            }
            var uploadedPath = dropboxService.uploadTextFile(text);
            var result = String.format("File %s is created", uploadedPath);
            log.info(result);
            postMessage(chatId, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void postMessage(String chatId, String text) throws TelegramApiException {
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

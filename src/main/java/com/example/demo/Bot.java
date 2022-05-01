package com.example.demo;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {
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
            String filename = null;
            var receivedMessage = update.getMessage();
            var telegramMessage =  messageFactory.createMessage(receivedMessage);
            var voiceMessage = receivedMessage.getVoice();

            if (voiceMessage != null) {
                GetFile getFileMethod = new GetFile();
                getFileMethod.setFileId(voiceMessage.getFileId());

                var telegramFile  = execute(getFileMethod);
                var file = this.downloadFile(telegramFile.getFilePath());

                filename = dropboxService.uploadFile(file,dropboxService.getFileName(telegramFile.getFilePath()));

            }
            else {
                filename = dropboxService.uploadTextFile(update.getMessage().getText());
            }


            var message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(String.format("File %s uploaded", filename));
            execute(message);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }
}

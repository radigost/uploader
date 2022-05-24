package com.example.demo;

import com.example.domain.DropBoxServiceStatus;
import com.example.domain.TelegramMessage;
import java.util.Optional;
import lombok.SneakyThrows;
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
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        var chatId = update.getMessage().getChatId().toString();
        var telegramMessage = messageFactory.createMessage(update.getMessage());

        // TODO extract selection to dedicated method/structure
        if (DropBoxServiceStatus.NOT_AUTHORIZED.equals(dropboxService.getStatus())) {
            provideAuthUrl(chatId);
        } else if (DropBoxServiceStatus.WAITING_AUTH_CODE.equals(dropboxService.getStatus())) {
            completeAuth(telegramMessage, chatId);
        } else {
            composeMdFile(telegramMessage, chatId);
        }
    }

    @SneakyThrows
    private void provideAuthUrl(String chatId) {
        var url = dropboxService.generateAuthUrl();
        postMessage(chatId, "Please authorize the application before sending data." +
            "\n 1) Go to the link below" +
            "\n 2) Click 'Continue', then click 'Allow'" +
            "\n 3) Copy the code and send it as the next message");
        postMessage(chatId, url);
    }

    @SneakyThrows
    private void completeAuth(TelegramMessage telegramMessage, String chatId) {
        var res = dropboxService.completeAuth(
            Optional.ofNullable(telegramMessage.getText()).orElse(" ")
        );
        if (res) {
            postMessage(chatId, "auth was successfull, now you can send messages");
        } else {
            postMessage(chatId, "Failed to  auth, please try later");
        }
    }

    @SneakyThrows
    private void composeMdFile(TelegramMessage telegramMessage, String chatId) {
        String filename = null;
        if (telegramMessage.getFileId() != null) {
            GetFile getFileMethod = new GetFile();
            getFileMethod.setFileId(telegramMessage.getFileId());
            var attachment = execute(getFileMethod);
            var file = downloadFile(attachment.getFilePath());
            filename = dropboxService.uploadFileToDataFolder(file, attachment.getFilePath());
            // TODO provide link to dropbox file
            var result = String.format("File %s is uploaded", filename);
            log.info(result);
            postMessage(chatId, result);
        }
        String text = Optional.ofNullable(telegramMessage.getText()).orElse(" ");

        if (filename != null) {
            text = text.concat(String.format("\n ![[%s]]", filename));
        }
        var uploadedPath = dropboxService.createAndUploadMdFile(text);
        // TODO provide url to dropbox file
        var result = String.format("File %s is created", uploadedPath);
        log.info(result);
        postMessage(chatId, result);
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

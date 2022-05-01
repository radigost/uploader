package com.example.domain;

import java.io.File;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Message;

public class VoiceTelegramMessage extends TelegramMessage implements HasAttachments {
    public VoiceTelegramMessage(Message message) {
        super(message);
    }

    @Override
    public File getAttachment() {
        GetFile getFileMethod = new GetFile();
        getFileMethod.setFileId(message.getVoice().getFileId());

        var telegramFile  = execute(getFileMethod);
        var file = this.downloadFile(telegramFile.getFilePath());
        return null;
    }

    @Override
    public String getText() {
        return "";
    }

    private String getFileName(String path) {
        return path.substring(path.lastIndexOf("/")+1);
    }

}

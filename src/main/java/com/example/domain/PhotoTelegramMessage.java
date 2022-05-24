package com.example.domain;

import org.telegram.telegrambots.meta.api.objects.Message;

public class PhotoTelegramMessage extends TelegramMessage {
    public PhotoTelegramMessage(Message message) {
        super(message);
    }

    @Override
    public String getText() {
        return message.getCaption();
    }

    @Override
    public String getFileId() {
        return message.getPhoto().get(message.getPhoto().size()-1).getFileId();
    }
}

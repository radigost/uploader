package com.example.domain;

import org.telegram.telegrambots.meta.api.objects.Message;

public class PlainTextTelegramMessage extends TelegramMessage{

    public PlainTextTelegramMessage(Message message) {
        super(message);
    }

    @Override
    public String getText() {
        return message.getText();
    }

    @Override
    public String getFileId() {
        return null;
    }


}

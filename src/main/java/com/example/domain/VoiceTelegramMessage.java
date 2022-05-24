package com.example.domain;

import org.telegram.telegrambots.meta.api.objects.Message;

public class VoiceTelegramMessage extends TelegramMessage implements HasAttachments {
    public VoiceTelegramMessage(Message message) {
        super(message);
    }

    @Override
    public String getFileId() {
        return message.getVoice().getFileId();
    }

    @Override
    public String getText() {
        return null;
    }



}

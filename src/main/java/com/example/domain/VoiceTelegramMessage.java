package com.example.domain;

import java.io.File;
import org.telegram.telegrambots.meta.api.objects.Message;

public class VoiceTelegramMessage extends TelegramMessage {
    public VoiceTelegramMessage(Message message) {
        super(message);
    }

    @Override
    public File getAttachment() {
        return null;
    }

    @Override
    public String getText() {
        return null;
    }
}

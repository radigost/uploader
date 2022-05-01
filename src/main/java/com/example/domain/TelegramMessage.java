package com.example.domain;

import java.io.File;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;

@AllArgsConstructor
public abstract class TelegramMessage {
    protected Message message;

    public String getText() {
        return message.getText();
    }

}

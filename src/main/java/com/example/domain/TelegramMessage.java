package com.example.domain;

import java.io.File;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;

@AllArgsConstructor
public abstract class TelegramMessage {
    public abstract File getAttachment();

    public abstract String getText();

    private Message message;

}

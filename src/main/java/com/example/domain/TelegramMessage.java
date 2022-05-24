package com.example.domain;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;

@AllArgsConstructor
public abstract class TelegramMessage {
    protected Message message;

    abstract public String getText();

    // TODO check if message can contain several files (probably not)
    public abstract String getFileId();

}

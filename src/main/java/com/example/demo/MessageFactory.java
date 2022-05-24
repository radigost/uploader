package com.example.demo;

import com.example.domain.PhotoTelegramMessage;
import com.example.domain.PlainTextTelegramMessage;
import com.example.domain.TelegramMessage;
import com.example.domain.VoiceTelegramMessage;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class MessageFactory {

    public TelegramMessage createMessage(Message message) {
        TelegramMessage telegramMessage = null;
        if (message.getVoice() != null) {
            telegramMessage = new VoiceTelegramMessage(message);
        }
        else if (message.getPhoto() != null) {
            telegramMessage = new PhotoTelegramMessage(message);
        } else {
            telegramMessage = new PlainTextTelegramMessage(message);
        }
        return telegramMessage;

    }
}

package com.telegram.dost.bot.service.reply;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@PropertySource("classpath:messages.properties")
@Slf4j
public class EnterReminderReplyService implements ReplyService {

    @Value("${reply.set.reminder}")
    private String replyText;

    @Override
    public SendMessage getReplyMessage(Update update) {

        String languageCode = update.getMessage().getFrom().getLanguageCode();
        log.info("Language code: " + languageCode);

        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(replyText);

        return message;
    }
}

package com.telegram.dost.bot.service.reply;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@PropertySource("classpath:messages.properties")
public class EnterReminderReplyService implements ReplyService {

    @Value("${reply.set.reminder}")
    private String replyText;

    @Override
    public SendMessage getReplyMessage(Update update) {

        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(replyText);

        return message;
    }
}

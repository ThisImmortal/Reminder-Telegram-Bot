package com.telegram.dost.bot.service.reply;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ReplyService {

    public SendMessage getReplyMessage(Update update);
}

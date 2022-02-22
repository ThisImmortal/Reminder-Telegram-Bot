package com.telegram.dost.bot.handler;

import com.telegram.dost.bot.bot.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


public interface ReplyMessageHandler {

    SendMessage createReplyMessage(Update update);

    BotState getMessageServiceName();



}

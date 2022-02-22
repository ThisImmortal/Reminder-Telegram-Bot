package com.telegram.dost.bot.bot;

import com.telegram.dost.bot.handler.ReplyMessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotStateContext {

    private Map<BotState, ReplyMessageHandler> messageServices = new HashMap<>();

    public BotStateContext(List<ReplyMessageHandler> messageServices){
        messageServices.forEach(messageService -> this.messageServices.put(messageService.getMessageServiceName(), messageService));
    }

    public SendMessage defineMessageService(BotState currentBotState, Update update){

        ReplyMessageHandler replyMessageHandler = messageServices.get(currentBotState);
        SendMessage message = replyMessageHandler.createReplyMessage(update);
        return message;

    }
}

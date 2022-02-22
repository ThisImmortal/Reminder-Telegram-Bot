package com.telegram.dost.bot.handler.reminder;

import com.telegram.dost.bot.bot.BotState;
import com.telegram.dost.bot.cache.UserDataCache;
import com.telegram.dost.bot.handler.ReplyMessageHandler;
import com.telegram.dost.bot.service.reply.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ProcessingReminderHandler implements ReplyMessageHandler {

    private UserDataCache userDataCache;
    private ReplyService replyService;


    @Autowired
    public ProcessingReminderHandler(UserDataCache userDataCache,
                                     @Qualifier("processingReminderReplyService") ReplyService replyService){
        this.userDataCache = userDataCache;
        this.replyService = replyService;
    }

    @Override
    public SendMessage createReplyMessage(Update update) {

        int userId = update.getMessage().getFrom().getId().intValue();

        userDataCache.setUsersCurrentBotState(userId, BotState.PROCESSING_REMINDER_MESSAGE);

        return replyService.getReplyMessage(update);
    }

    @Override
    public BotState getMessageServiceName() {
        return BotState.PROCESSING_REMINDER_MESSAGE;
    }
}

package com.telegram.dost.bot.handler.helpmenu;

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
public class HelpMenuHandler implements ReplyMessageHandler {


    private UserDataCache userDataCache;

    private ReplyService replyService;

    @Autowired
    public HelpMenuHandler(UserDataCache userDataCache,
                           @Qualifier("helpMenuReplyService") ReplyService replyService){
        this.userDataCache = userDataCache;
        this.replyService = replyService;
    }

    @Override
    public SendMessage createReplyMessage(Update update) {

        return replyService.getReplyMessage(update);
    }

    @Override
    public BotState getMessageServiceName() {
        return BotState.HELP_MENU;
    }
}

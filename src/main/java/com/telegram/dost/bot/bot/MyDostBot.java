package com.telegram.dost.bot.bot;

import com.telegram.dost.bot.cache.UserDataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
public class MyDostBot extends TelegramLongPollingBot {

    private UserDataCache userDataCache;
    private BotStateContext botStateContext;


    @Autowired
    public MyDostBot(UserDataCache userDataCache,
                     BotStateContext botStateContext)
    {
        this.userDataCache = userDataCache;
        this.botStateContext = botStateContext;
    }

    @Value("${telegrambot.username}")
    private String botUsername;

    @Value("${telegrambot.botToken}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }


    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            int userId = update.getMessage().getFrom().getId().intValue();
            SendMessage replyMessage;

            BotState botState;

            switch (messageText){
                case "/start":
                   botState = BotState.REMINDER_MENU;
                   break;
                case "/help":
                    botState = BotState.HELP_MENU;
                    break;
                case "How to use?":
                    botState = BotState.HOW_TO_USE_BOT;
                    break;
                case "About author":
                    botState = BotState.ABOUT_AUTHOR;
                    break;
                case "Set new reminder":
                    botState = BotState.ENTER_REMINDER_WITH_SUCH_FORMAT;
                    break;
                case "Show my reminders":
                    botState = BotState.SHOW_MY_REMINDERS;
                    break;
                default:
                    botState = userDataCache.getUsersCurrentBotState(userId);
                    break;
            }

            userDataCache.setUsersCurrentBotState(userId, botState);
            replyMessage = botStateContext.defineReplyService(botState, update);

            try {
                execute(replyMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }

    }

}

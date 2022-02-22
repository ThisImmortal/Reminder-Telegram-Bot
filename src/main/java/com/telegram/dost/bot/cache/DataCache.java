package com.telegram.dost.bot.cache;

import com.telegram.dost.bot.bot.BotState;

public interface DataCache {

    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);
}

package com.telegram.dost.bot.service;

import com.telegram.dost.bot.model.Reminder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class ScheduleService {

    private AbsSender absSender;
    private ReminderService reminderService;


    @Autowired
    public ScheduleService(ReminderService reminderService, AbsSender absSender){
        this.absSender = absSender;
        this.reminderService = reminderService;
    }


    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    public void processSchedule() {

        LocalDateTime now = LocalDateTime.now();
        List<Reminder> reminders = reminderService.getReminders(now.truncatedTo(ChronoUnit.HOURS), now);
        log.info("Got {} reminders to process", reminders.size());

        try {
            for (Reminder reminder : reminders) {
                SendMessage message = new SendMessage();
                message.setChatId(reminder.getUserId().toString());
                message.setText("Your have a reminder: " + reminder.getDescription());
                absSender.execute(message);
                reminderService.makeFinished(reminder.getId());
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}

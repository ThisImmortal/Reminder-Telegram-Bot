package com.telegram.dost.bot.service.reply;

import com.telegram.dost.bot.model.Reminder;
import com.telegram.dost.bot.service.ReminderService;
import com.telegram.dost.bot.util.AESUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@PropertySource("classpath:messages.properties")
public class ShowReminderReplyService implements ReplyService {

    @Value("${reply.no.reminders}")
    private String replyText;

    private ReminderService reminderService;

    @Autowired
    public ShowReminderReplyService(ReminderService reminderService){
        this.reminderService = reminderService;
    }

    @SneakyThrows
    @Override
    public SendMessage getReplyMessage(Update update) {

        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        int userId = update.getMessage().getFrom().getId().intValue();

        LocalDateTime now = LocalDateTime.now();
        List<Reminder> myReminders = reminderService.getUserReminders(userId, now);
        log.info("Got {} myReminders showing to user, myReminders.size()");

        if(myReminders.isEmpty()){
            message.setText(replyText);
        }

        else {
            int count = 1;
            String replyReminderList = "";
            for (Reminder reminder : myReminders) {
                if(reminder.getUserId() == userId) {
                    LocalDate reminderDate = reminder.getDateTime().toLocalDate();
                    LocalTime reminderTime = reminder.getDateTime().toLocalTime();
                    replyReminderList += count + "." + " " + AESUtil.decrypt(reminder.getDescription()) + " - "
                            + reminderDate + " at " +reminderTime + "\n";
                    count++;
                }
            }
            message.setText(replyReminderList);

        }

        return message;
    }
}

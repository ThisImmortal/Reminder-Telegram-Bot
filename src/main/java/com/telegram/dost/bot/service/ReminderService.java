package com.telegram.dost.bot.service;

import com.telegram.dost.bot.model.Reminder;
import com.telegram.dost.bot.repository.ReminderRepository;
import com.telegram.dost.bot.util.AESUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ReminderService {

    private ReminderRepository reminderRepository;


    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }


    public void saveReminder(Reminder reminder) {

        log.info("Saving reminder {}", reminder);
        reminderRepository.save(reminder);
    }

    @SneakyThrows
    public List<Reminder> getReminders(LocalDateTime from, LocalDateTime to) {

        List<Reminder> reminders = reminderRepository.getReminders(from, to);
        log.info("Getting reminders from {} to {}: [{}]", from, to, reminders);

        if(!reminders.isEmpty()){
            for(Reminder reminder : reminders){
                String reminderDescription = null;
                reminderDescription = AESUtil.decrypt(reminder.getDescription());
                reminder.setDescription(reminderDescription);
            }
        }
        return reminders;
    }


    public void makeFinished(int reminderId){

        log.info("Marking reminder with id {} as finished ", reminderId);
        reminderRepository.makeFinished(reminderId);
    }

    public List<Reminder> getUserReminders(int userId, LocalDateTime now){

        log.info("Getting reminders with datetime after {} for userId {}", now, userId);
        return reminderRepository.findByUserIdAndIsFinished(userId, now);
    }
}

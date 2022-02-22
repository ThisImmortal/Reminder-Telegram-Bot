package com.telegram.dost.bot.service.reply;

import com.telegram.dost.bot.model.ParsingResult;
import com.telegram.dost.bot.model.Reminder;
import com.telegram.dost.bot.service.ParseService;
import com.telegram.dost.bot.service.ReminderService;
import com.telegram.dost.bot.util.AESUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@PropertySource("classpath:messages.properties")
public class ProcessingReminderReplyService implements ReplyService {

    @Value("${reply.accept.reminder}")
    private String replyTextAccept;

    @Value("${reply.error.past.time}")
    private String replyTextPastTime;

    @Value("${reply.error.invalid}")
    private String replyTextInvalidDateTime;

    private ReminderService reminderService;
    private ParseService parseService;

    @Autowired
    public ProcessingReminderReplyService(ReminderService reminderService,
                                          ParseService parseService){
        this.reminderService = reminderService;
        this.parseService = parseService;
    }

    @SneakyThrows
    @Override
    public SendMessage getReplyMessage(Update update) {

        String userMessage = update.getMessage().getText();
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        int userId = update.getMessage().getFrom().getId().intValue();

        ParsingResult parsingResult = parseService.parse(userMessage);

        if(parsingResult.getStatus().equals("valid")){

            String reminderDescription = parsingResult.getReminderDescription();

            reminderDescription = AESUtil.encrypt((reminderDescription.substring(0,1).toUpperCase() + reminderDescription.substring(1).toLowerCase()));

            Reminder reminder = new Reminder(parsingResult.getDateTime(),
                    reminderDescription,
                    userId);

            reminderService.saveReminder(reminder);
            message.setText(replyTextAccept);
        }
        else if(parsingResult.getStatus().equals("time in the past")){
            message.setText(replyTextPastTime);
        }
        else {
            message.setText(replyTextInvalidDateTime);
        }
        return message;
    }
}

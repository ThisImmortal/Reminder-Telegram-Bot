package com.telegram.dost.bot.service;

import com.telegram.dost.bot.model.ParsingResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ParseService {
//    (\d{4}-\d{1,2}-\d{1,2}) (at|on) (\d{1,2}:\d{1,2})(.*)

    private final Pattern pattern = Pattern.
            compile("((\\d{2}.\\d{2}.\\d{4})|(\\btomorrow\\b|\\btoday\\b)) (at|on) (\\d{1,2}:\\d{1,2}) ((?=\\S{0}).{1,50})", Pattern.CASE_INSENSITIVE);

    public ParsingResult parse(String message){
        LocalDateTime localDateTime;
        ParsingResult parsingResult = new ParsingResult();
        Matcher matcher = pattern.matcher(message);


        if (matcher.matches()){
            String matcherGroup = matcher.group(1);
            if (isShort(matcherGroup)){
                localDateTime = produceLocalDateTime(matcher);
                parsingResult.setDateTime(localDateTime);
                parsingResult.setReminderDescription(matcher.group(6).trim().replaceAll("\\s+", " "));
                parsingResult.setStatus("valid");
            }
            else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                localDateTime = LocalDateTime.parse(LocalDate.parse(matcher.group(1), formatter)+"T"+matcher.group(5));
                if(localDateTime.isAfter(LocalDateTime.now())){
                    parsingResult.setDateTime(localDateTime);
                    parsingResult.setReminderDescription(matcher.group(6).trim().replaceAll("\\s+", " "));
                    parsingResult.setStatus("valid");
                }
                else {
                    parsingResult.setStatus("time in the past");
                }
            }
        }
        else {
            parsingResult.setStatus("invalid");
        }
        return parsingResult;
    }


    public boolean isShort(String matcherGroup){
        boolean isShort = false;
        if (matcherGroup.equalsIgnoreCase("tomorrow") || matcherGroup.equalsIgnoreCase("today")){
            isShort = true;
        }
        return isShort;
    }

    public LocalDateTime produceLocalDateTime(Matcher matcher){

        LocalDateTime dateTime;
        LocalDate date = LocalDate.now();
        String gotTime = matcher.group(5);
        Character semiColon = gotTime.charAt(1);
        if (semiColon.equals(':')){
            gotTime = "0" + gotTime;
        }

        LocalTime futureTime = LocalTime.parse(gotTime);

        if(matcher.group(1).equalsIgnoreCase("tomorrow")){
            date = date.plusDays(1);
        }

        dateTime = LocalDateTime.of(date, futureTime);
        return dateTime;
    }

}
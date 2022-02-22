package com.telegram.dost.bot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ParsingResult {

    private LocalDateTime dateTime;
    private String reminderDescription;
    private String status;


}

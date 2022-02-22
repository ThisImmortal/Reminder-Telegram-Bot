package com.telegram.dost.bot.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "reminder")
@Data
@NoArgsConstructor
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reminder_id")
    private Integer id;

    @Column(name = "reminder_datetime")
    private LocalDateTime dateTime;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "reminder_description")
    private String description;

    @Column(name = "reminder_status")
    private boolean isFinished;


    public Reminder(LocalDateTime dateTime, String description, Integer userId) {

        this.dateTime = dateTime;
        this.description = description;
        this.userId = userId;
        this.isFinished = false;
    }

}

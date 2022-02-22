package com.telegram.dost.bot.repository;


import com.telegram.dost.bot.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Integer> {


    @Query("SELECT r FROM Reminder r WHERE r.dateTime >= :from AND r.dateTime <= :to AND r.isFinished=false")
    List<Reminder> getReminders(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);


    @Transactional
    @Modifying
    @Query("update Reminder r set r.isFinished=true where r.id=:reminderId")
    void makeFinished(@Param("reminderId") int reminderId);


    @Query("SELECT r FROM Reminder r WHERE r.dateTime >= :now AND r.userId=:userId")
    List<Reminder> findByUserIdAndIsFinished(@Param("userId") int userId, @Param("now") LocalDateTime now);


}

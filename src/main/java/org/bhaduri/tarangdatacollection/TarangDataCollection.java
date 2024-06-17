/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package org.bhaduri.tarangdatacollection;

import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JSpinner;
import org.bhaduri.tarangdatacollection.webcrawler.WebDataRetrieve;
import org.quartz.CronScheduleBuilder;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.CronTrigger;
import org.quartz.DailyTimeIntervalScheduleBuilder;
import org.quartz.DailyTimeIntervalTrigger;
import org.quartz.DateBuilder;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TimeOfDay;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.HolidayCalendar;

/**
 *
 * @author bhaduri
 */
public class TarangDataCollection {

    public static void main(String[] args) {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched;
        try {
            sched = sf.getScheduler();

            JobDetail job = newJob(WebDataRetrieve.class)
                    .withIdentity("WebDataRetrieve", "WebCrawler")
                    .build();

            TimeOfDay startTime = createStartTime();
            TimeOfDay endTime = createEndTime();
            HolidayCalendar cal = new HolidayCalendar();
            cal.addExcludedDate( new Date(2024, 6, 17));
            sched.addCalendar("Holidays", cal, false,false);
            
            
            
            //SimpleScheduleBuilder schedule = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever();
            DailyTimeIntervalScheduleBuilder schedule = DailyTimeIntervalScheduleBuilder
                    .dailyTimeIntervalSchedule()
                    .onMondayThroughFriday()
                    .startingDailyAt(startTime).endingDailyAt(endTime).withIntervalInSeconds(5);
            
            DailyTimeIntervalTrigger simpleTrigger 
               = TriggerBuilder
                .newTrigger()
                .withIdentity("Hello-trigger-name")
                .forJob(job)
                .withSchedule(schedule).modifiedByCalendar("Holidays")
                .build();
            

            sched.scheduleJob(job, simpleTrigger);
            sched.start();
        } catch (SchedulerException ex) {
            Logger.getLogger(TarangDataCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static TimeOfDay createStartTime() {
        TimeOfDay timeOfDay = TimeOfDay.hourAndMinuteAndSecondFromDate(DateBuilder.dateOf(21, 57, 0));
        return timeOfDay;
    }
    
    public static TimeOfDay createEndTime() {
        TimeOfDay timeOfDay = TimeOfDay.hourAndMinuteAndSecondFromDate(DateBuilder.dateOf(21, 58, 0));
        return timeOfDay;
                
    }
}

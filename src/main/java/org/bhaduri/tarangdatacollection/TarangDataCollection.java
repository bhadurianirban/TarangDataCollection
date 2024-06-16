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
import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.DateBuilder;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

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

            //Date startTime = tomorrowAt(17, 50, 0);
            
            SimpleTrigger simpleTrigger 
               = TriggerBuilder
                .newTrigger()
                .withIdentity("Hello-trigger-name")
                .forJob(job)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
                .build();

            sched.scheduleJob(job, simpleTrigger);
            sched.start();
        } catch (SchedulerException ex) {
            Logger.getLogger(TarangDataCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Date tomorrowAt(int hour, int minute, int second) {
        return DateBuilder.todayAt(hour, minute, second);
                
    }

}

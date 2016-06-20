package com.lge.sureparksystem.parkserver.manager.statisticsmanager;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.topic.StatisticsManagerTopic;
import com.lge.sureparksystem.parkserver.util.Logger;

public class StatisticsManager extends ManagerTask {
    public class StatisticsManagerListener {
        @Subscribe
        public void onSubscribe(StatisticsManagerTopic topic) {
            System.out.println("StatisticsManagerListener: " + topic);

            process(topic.getJsonObject());
        }
    }

    @Override
    public void init() {
        getEventBus().register(new StatisticsManagerListener());
    }

    @Override
    public void run() {
        ScheduledJob job = new ScheduledJob();
        Timer jobScheduler = new Timer();
//        jobScheduler.schedule(job, getFirstScheduleTime(), javax.management.timer.Timer.ONE_DAY);
        jobScheduler.schedule(job, getFirstScheduleTime(), javax.management.timer.Timer.ONE_SECOND);
        while (loop) {
            try {
                //                System.out.println("StatisticsManager run(): ");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void process(JSONObject jsonObject) {

    }

    private Date getFirstScheduleTime() {
        Calendar cal = Calendar.getInstance();
        Logger.log("curtime " + cal.getTime());
//        long timeAfterOneHoure = cal.getTimeInMillis() + javax.management.timer.Timer.ONE_DAY;
        long timeAfterOneHour = cal.getTimeInMillis() + javax.management.timer.Timer.ONE_MINUTE;
        
        cal.setTimeInMillis(timeAfterOneHour);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONDAY);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

//        cal.set(year, month, day, 0, 0, 0);
        cal.set(year, month, day, hour, min, 0);
        Logger.log("day " + cal.getTime());
        return cal.getTime();
    }
    
    private void updateStatisticsInfo() {
        System.out.println(new Date());
        
    }

    class ScheduledJob extends TimerTask {

        public void run() {
            updateStatisticsInfo();
        }
    }
}

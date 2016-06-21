package com.lge.sureparksystem.parkserver.manager.statisticsmanager;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseProvider;
import com.lge.sureparksystem.parkserver.topic.StatisticsManagerTopic;
import com.lge.sureparksystem.parkserver.util.Logger;

public class StatisticsManager extends ManagerTask {
    DatabaseProvider dbProvider = null;

    public class StatisticsManagerListener {
        @Subscribe
        public void onSubscribe(StatisticsManagerTopic topic) {
            System.out.println("StatisticsManagerListener: " + topic);

            processMessage(topic.getJsonObject());
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
        jobScheduler.schedule(job, getFirstScheduleTime(), javax.management.timer.Timer.ONE_DAY);
        while (loop) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void processMessage(JSONObject jsonObject) {

    }

    private Date getFirstScheduleTime() {
        Calendar cal = Calendar.getInstance();
        Logger.log("curtime " + cal.getTime());
        long timeAfterOneDay = cal.getTimeInMillis() + javax.management.timer.Timer.ONE_DAY;

        cal.setTimeInMillis(timeAfterOneDay);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        cal.set(year, month, day, 0, 0, 0);
        Logger.log("day " + cal.getTime());
        return cal.getTime();
    }

    private void updateStatisticsInfo() {
        Calendar cal = Calendar.getInstance();
        Logger.log("curtime " + cal.getTime());
        long timeBeforeOneDay = cal.getTimeInMillis() - javax.management.timer.Timer.ONE_DAY;

        cal.setTimeInMillis(timeBeforeOneDay);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        cal.set(year, month, day, 0, 0, 0);
        Date updatingDate = cal.getTime();
        Logger.log("updating day = " + updatingDate);

        if (dbProvider == null) {
            dbProvider = DatabaseProvider.getInstance();
        }
        dbProvider.updateDailyStatisticsInfo(updatingDate);
    }

    class ScheduledJob extends TimerTask {

        public void run() {
            updateStatisticsInfo();
        }
    }
}

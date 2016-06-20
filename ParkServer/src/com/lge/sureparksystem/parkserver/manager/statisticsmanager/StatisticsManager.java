package com.lge.sureparksystem.parkserver.manager.statisticsmanager;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.topic.ManagerTopic;
import com.lge.sureparksystem.parkserver.topic.StatisticsManagerTopic;

public class StatisticsManager extends ManagerTask {
    public class StatisticsManagerListener {
        @Subscribe
        public void onSubscribe(StatisticsManagerTopic topic) {
            System.out.println("StatisticsManagerListener: " + topic);

            process(topic);
        }
    }

    @Override
    public void init() {
        getEventBus().register(new StatisticsManagerListener());
    }

    @Override
    public void run() {
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
    protected void process(ManagerTopic topic) {

    }
}

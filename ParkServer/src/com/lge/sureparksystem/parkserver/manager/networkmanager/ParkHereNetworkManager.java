package com.lge.sureparksystem.parkserver.manager.networkmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.AuthenticationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkHereNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ReservationManagerTopic;
import com.lge.sureparksystem.parkserver.util.Logger;

public class ParkHereNetworkManager extends NetworkManager {
    public class ParkHereNetworkManagerListener {
        @Subscribe
        public void onSubscribe(ParkHereNetworkManagerTopic topic) {
            System.out.println("ParkHereNetworkManagerListener: " + topic);

            processMessage(topic.getJsonObject());
        }
    }

    @Override
    public void init() {
        getEventBus().register(new ParkHereNetworkManagerListener());
    }

    public ParkHereNetworkManager(int serverPort) {
        super(serverPort);
    }

    public void send(JSONObject jsonObject) {
        for (SocketForServer socketForServer : socketList) {
            socketForServer.send(jsonObject);
        }
    }

    @Override
    public void receive(JSONObject jsonObject) {
        processMessage(jsonObject);
    }

    public void run() {
        super.run();
    }

    protected void processMessage(JSONObject jsonObject) {
        super.processMessage(jsonObject);

        MessageType messageType = MessageParser.getMessageType(jsonObject);

        switch (messageType) {
        case CREATE_DRIVER:
            getEventBus().post(new AuthenticationManagerTopic(jsonObject));
            break;
        case PARKING_LOT_INFO_REQUEST:
            getEventBus().post(new ReservationManagerTopic(jsonObject));
            break;
        case RESERVATION_REQUEST: //FALL THROUGH
        case RESERVATION_INFO_REQUEST: //FALL THROUGH
            getEventBus().post(new ReservationManagerTopic(jsonObject));
            break;
        case RESERVATION_INFORMATION:
            Logger.log("Reservation Information");
            send(jsonObject);
            break;
        case RESPONSE:
            send(jsonObject);
            break;
        case PARKING_LOT_LIST:
            send(jsonObject);
            break;
        default:
            break;
        }

        return;
    }
}
package com.lge.sureparksystem;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.lge.sureparksystem.control.NetworkManager;
import com.lge.sureparksystem.control.NetworkToActivity;
import com.lge.sureparksystem.model.AttendantModel;
import com.lge.sureparksystem.model.DriverModel;
import com.lge.sureparksystem.model.LoginModel;
import com.lge.sureparksystem.model.OwnerModel;
import com.lge.sureparksystem.parkclient.R;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.util.Utils;
import com.lge.sureparksystem.view.BaseView;
import com.lge.sureparksystem.view.RequestData;
import com.lge.sureparksystem.view.TTSWrapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends Activity implements OnClickListener, NetworkToActivity, BaseView {

    private static String TAG = "MainActivity";
    private View mLoadingView;
    private NetworkManager mNetworkManager;
    private AbstractFactory mLoginFactory;
    private AbstractFactory mOwnerFactory;
    private AbstractFactory mAttendantFactory;
    private AbstractFactory mDriverFactory;
    private int mTempNum = 1;
    private String mCurrentId;
    private int mCurrentAutority;
    final static int ATTENDANT_WATCH_HANDLER = 1;
    private TTSWrapper mTTSWrapper;

    public void viewLoadingView() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    public void goneLoadingView() {
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mLoadingView = findViewById(R.id.loading);
        mLoadingView.setVisibility(View.VISIBLE);
        mTTSWrapper = new TTSWrapper(this);
        InetAddress serverAddr;
        try {
            serverAddr = InetAddress.getByName(Utils.IP_ADDRESS);
            mNetworkManager = new NetworkManager(serverAddr, Utils.PORT, this);
            mNetworkManager.connect();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        findViewById(R.id.btn_temp).setOnClickListener(this);
        initFactory();
    }

    private void initFactory() {
        mLoginFactory = new LoginFactory();
        mLoginFactory.createModel();
        mLoginFactory.createView();

        mOwnerFactory = new OwnerFactory();
        mOwnerFactory.createModel();
        mOwnerFactory.createView();

        mAttendantFactory = new AttendantFactory();
        mAttendantFactory.createModel();
        mAttendantFactory.createView();

        mDriverFactory = new DriverFactory();
        mDriverFactory.createModel();
        mDriverFactory.createView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_area, (Fragment)mLoginFactory.mBaseFragment, Utils.TAG_LOGIN_FRAGMENT);
        ft.commitAllowingStateLoss();
        findViewById(R.id.fragment_area).setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mNetworkManager.disconnect();
        mTTSWrapper.dismiss();
        mLoadingView.setVisibility(View.GONE);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchFagment(AbstractFactory factory, String fragment_tag, String viewName, Bundle bundle) {
        if (bundle != null) {
            Log.d(TAG, "Add bundle");
            factory.mBaseFragment.setArguments(bundle);
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_area, (Fragment)factory.mBaseFragment, fragment_tag);
        ft.commitAllowingStateLoss();
        getActionBar().setTitle(viewName);
    }

    @Override
    public void onClick(View v) {
        int num = mTempNum % 4;
        decideFragment(num, null);
        mTempNum++;
    }

    private void decideFragment(int num, Bundle bundle) {
        String tag = null;
        String viewName = null;
        AbstractFactory af = null;
        if (num >= 4) {
            num = 0;
        }
        if (num == Utils.LOGINVIEW_FRAGMENT) {
            af = mLoginFactory;
            tag = Utils.TAG_LOGIN_FRAGMENT;
            viewName = this.getResources().getString(R.string.login);
        } else if (num == Utils.OWNERVIEWFRAGMENT) {
            af = mOwnerFactory;
            tag = Utils.TAG_OWNER_FRAGMENT;
            viewName = this.getResources().getString(R.string.owner);
        } else if (num == Utils.ATTENDANTVIEW_FRAGMENT) {
            af = mAttendantFactory;
            tag = Utils.TAG_ATTENDANT_FRAGMENT;
            viewName = this.getResources().getString(R.string.attendant);
        } else if (num == Utils.DRIVERVIEW_FRAGMENT) {
            af = mDriverFactory;
            tag = Utils.TAG_DRIVER_FRAGMENT;
            viewName = this.getResources().getString(R.string.driver);
        }
        switchFagment(af, tag, viewName, bundle);
    }

    public void sendMessageToServer() {
        Log.d(TAG, "");
        // JSONObject jsonObject = MessageParser.makeJSONObject(new
        // SocketMessage(MessageType.RESERVATION_NUMBER));
        // To do divide message
        // mNetworkManager.sendMessage(jsonObject);
    }

    @Override
    public void parseJSONMessage(String jsonMessage) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject)jsonParser.parse(jsonMessage);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MessageType messageType = MessageParser.getMessageType(jsonObject);
        Log.d(TAG, "parseJSONMessage : " + messageType);
        switch (messageType) {
        // Login
        case CREATE_DRVIER_RESOPNSE:
            String type = MessageParser.getString(jsonObject, "type");
            Log.d(TAG, "type : " + type);
            if ("create_driver".equals(type)) {
                LoginModel loginModel1 = (LoginModel)mLoginFactory.mBaseModel;
                LoginModel.Response r = loginModel1.new Response(jsonObject);
                if ("ok".equals(r.result)) {
                    Utils.showToast(this, "Create success");
                } else {
                    Utils.showToast(this, "Create fail");
                }
            } else if ("cancel_reservation".equals(type)) {
                DriverModel cancel_response = (DriverModel)mDriverFactory.mBaseModel;
                cancel_response.mResponse = cancel_response.new Response(jsonObject);
                mDriverFactory.mBaseFragment.setBaseModel(mDriverFactory.mBaseModel);
                if ("ok".equals(cancel_response.mResponse.result)) {
                    mDriverFactory.mBaseFragment.updateFlag(false);
                    Utils.showToast(this, "Reservation canceled");
                } else {
                    Utils.showToast(this, "Error check information");
                }
                refreshFragemnt(mDriverFactory);
            }
            break;
        case AUTHENTICATION_RESPONSE:
            LoginModel loginModel = (LoginModel)mLoginFactory.mBaseModel;
            LoginModel.Authentication_Response ar = loginModel.new Authentication_Response(jsonObject);
            if ("ok".equals(ar.getResult())) {
                mCurrentAutority = ar.geAuthority();
                if (Utils.DRIVERVIEW_FRAGMENT == mCurrentAutority) {
                    requsetServer(RequestData.RESERVATION_INFO_REQUEST, null);
                } else if (Utils.ATTENDANTVIEW_FRAGMENT == mCurrentAutority) {
                    requsetServer(RequestData.PARKING_LOT_STATUS_REQUEST, null);
                    Message msg = mServerWatchHandler.obtainMessage(ATTENDANT_WATCH_HANDLER);
                    mServerWatchHandler.sendMessageDelayed(msg, 15000);
                    Log.d(TAG, "mServerWatchHandler send 15000");
                } else if (Utils.OWNERVIEWFRAGMENT == mCurrentAutority) {
                    requsetServer(RequestData.PARKINGLOT_INFO_REQUEST, null);
                }
                decideFragment(ar.geAuthority(), null);
            } else {
                mCurrentId = null;
                mCurrentAutority = 0;
            }
            goneLoadingView();
            break;
        // Driver
        case PARKINGLOT_LIST_RESPONSE:
            Log.d(TAG, "mCurrentAutority : " + mCurrentAutority);
            if (Utils.DRIVERVIEW_FRAGMENT == mCurrentAutority) {
                Log.d(TAG, "PARKINGLOT_LIST_RESPONSE ATTENDANTVIEW_FRAGMENT");
                DriverModel parkinglot_list = (DriverModel)mDriverFactory.mBaseModel;
                parkinglot_list.mParkinglot_List = parkinglot_list.new Parkinglot_List(jsonObject);
                mDriverFactory.mBaseFragment.setBaseModel(mDriverFactory.mBaseModel);
                refreshFragemnt(mDriverFactory);
            } else if (Utils.OWNERVIEWFRAGMENT == mCurrentAutority) {
                Log.d(TAG, "PARKINGLOT_LIST_RESPONSE OWNERVIEWFRAGMENT");
                OwnerModel ownerModel = (OwnerModel)mOwnerFactory.mBaseModel;
                ownerModel.mParkinglot_List = ownerModel.new Parkinglot_List(jsonObject);
                mOwnerFactory.mBaseFragment.setBaseModel(mOwnerFactory.mBaseModel);
                refreshFragemnt(mOwnerFactory);
            }
            break;
        case RESERVATION_INFORMATION_RESPONSE:
            DriverModel information_response = (DriverModel)mDriverFactory.mBaseModel;
            information_response.mReservation_Information = information_response.new Reservation_Information(jsonObject);
            goneLoadingView();
            if ("ok".equals(information_response.mReservation_Information.getResult())) {
                // Instance update in Driver view
                mDriverFactory.mBaseFragment.updateFlag(true);
                mDriverFactory.mBaseFragment.setBaseModel(mDriverFactory.mBaseModel);
            } else {
                // reserved flag update in Driver view
                mDriverFactory.mBaseFragment.updateFlag(false);
                requsetServer(RequestData.PARKINGLOT_INFO_REQUEST, null);
            }
            // Driver fragment update
            refreshFragemnt(mDriverFactory);
            break;
        // Attendant
        case PARKING_LOT_STATUS:
            AttendantModel parking_lot_status_response = (AttendantModel)mAttendantFactory.mBaseModel;
            parking_lot_status_response.mParkinglotStatus = parking_lot_status_response.new ParkinglotStatus(jsonObject);
            mAttendantFactory.mBaseFragment.setBaseModel(mAttendantFactory.mBaseModel);
            refreshFragemnt(mAttendantFactory);
            mServerWatchHandler.removeMessages(ATTENDANT_WATCH_HANDLER);
            Message msg = mServerWatchHandler.obtainMessage(ATTENDANT_WATCH_HANDLER);
            mServerWatchHandler.sendMessageDelayed(msg, 15000);
            Log.d(TAG, "mServerWatchHandler update");
            break;
        case NOTIFICATION:
            AttendantModel notification_response = (AttendantModel)mAttendantFactory.mBaseModel;
            notification_response.mNotification = notification_response.new Notification(jsonObject);
            mAttendantFactory.mBaseFragment.setBaseModel(mAttendantFactory.mBaseModel);
            mAttendantFactory.mBaseFragment.updateFlag(true);
            mTTSWrapper.speak(notification_response.mNotification.type);
            refreshFragemnt(mAttendantFactory);
            break;

        // Owner
        case PARKINGLOT_STATISTICS:
            OwnerModel ownerMode2 = (OwnerModel)mOwnerFactory.mBaseModel;
            ownerMode2.mParkinglot_Statistics = ownerMode2.new Parkinglot_Statistics(jsonObject);
            mOwnerFactory.mBaseFragment.setBaseModel(mOwnerFactory.mBaseModel);
            refreshFragemnt(mOwnerFactory);
            break;
        case CHANGE_RESPONSE:
            OwnerModel ownerModel3 = (OwnerModel)mOwnerFactory.mBaseModel;
            ownerModel3.mChange_Response = ownerModel3.new Change_Response(jsonObject);
            mOwnerFactory.mBaseFragment.setBaseModel(mOwnerFactory.mBaseModel);
            refreshFragemnt(mOwnerFactory);
            if ("ok".equals(ownerModel3.mChange_Response.result)) {
                Utils.showToast(getApplication(), ownerModel3.mChange_Response.type + " changed to "
                        + ownerModel3.mChange_Response.value);
            } else {
                Utils.showToast(getApplication(), "Error");
            }
            break;
        default:
            break;
        }

    }

    private Handler mServerWatchHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case ATTENDANT_WATCH_HANDLER:
                Log.e(TAG, "mServerWatchHandler working. Check server");
//                AttendantModel notification_response = (AttendantModel)mAttendantFactory.mBaseModel;
//                notification_response.mNotification = notification_response.new Notification(
//                        MessageType.NOTIFICATION.getText(), "Server has problem. Check Please");
//                mAttendantFactory.mBaseFragment.setBaseModel(mAttendantFactory.mBaseModel);
//                mAttendantFactory.mBaseFragment.updateFlag(true);
//                mTTSWrapper.speak(notification_response.mNotification.type);
//                refreshFragemnt(mAttendantFactory);
                break;

            default:
                break;
            }

        }

    };

    private void refreshFragemnt(AbstractFactory af) {
        if (af == null || af.mBaseFragment == null) {
            Log.e(TAG, "check factory method");
            return;
        }
        af.mBaseFragment.onPause();
        af.mBaseFragment.onResume();
    }

    @Override
    public void requsetServer(RequestData rd, Bundle bundle) {
        Log.d(TAG, "requsetServer : " + rd);
        JSONObject jsonObject = new JSONObject();
        String messagetype;
        switch (rd) {
        case CREATE_NEW_DRIVER:
            LoginModel loginModel = (LoginModel)mLoginFactory.mBaseModel;
            messagetype = MessageType.CREATE_DRVIER_REQUEST.getText();
            String name = bundle.getString("name");
            String email = bundle.getString("email");
            String pwd = bundle.getString("pwd");
            LoginModel.Create_Driver cd = loginModel.new Create_Driver(messagetype, email, pwd, name);
            cd.putJSONObject(jsonObject);
            break;
        case LOGIN_USER:
            viewLoadingView();
            LoginModel loginModel1 = (LoginModel)mLoginFactory.mBaseModel;
            messagetype = MessageType.AUTHENTICATION_REQUEST.getText();
            String email1 = bundle.getString("email");
            String pwd1 = bundle.getString("pwd");
            mCurrentId = email1;
            LoginModel.Authentication_Request ar = loginModel1.new Authentication_Request(messagetype, email1, pwd1);
            ar.putJSONObject(jsonObject);
            break;

        case PARKINGLOT_INFO_REQUEST:
            DriverModel driverModel = (DriverModel)mDriverFactory.mBaseModel;
            messagetype = MessageType.PARKINGLOTINFO_REQUEST.getText();
            DriverModel.ParkinglotInfoRequest pi = driverModel.new ParkinglotInfoRequest(messagetype);
            pi.putJSONObject(jsonObject);
            break;

        case RESERVATION_INFO_REQUEST:
            DriverModel driverModel1 = (DriverModel)mDriverFactory.mBaseModel;
            messagetype = MessageType.RESERVATION_INFO_REQUEST.getText();
            DriverModel.ReservationInfo_Request rr = driverModel1.new ReservationInfo_Request(messagetype, mCurrentId);
            rr.putJSONObject(jsonObject);
            break;
        case RESERVATION_REQUEST:
            DriverModel driverModel2 = (DriverModel)mDriverFactory.mBaseModel;
            messagetype = MessageType.RESERVATION_REQUEST.getText();
            String parkinglot_id = bundle.getString("parkinglot_id");
            String reservation_time = bundle.getString("reservation_time");
            String paymentinfo = bundle.getString("paymentinfo");
            DriverModel.Reservation_Request rr1 = driverModel2.new Reservation_Request(messagetype, mCurrentId,
                    parkinglot_id, reservation_time, paymentinfo);
            rr1.putJSONObject(jsonObject);
            break;
        case CANCEL_REQUEST:
            DriverModel driverModel3 = (DriverModel)mDriverFactory.mBaseModel;
            messagetype = MessageType.CANCEL_REQUEST.getText();
            String reservation_id = bundle.getString("reservation_id");
            DriverModel.CancelRequest cr = driverModel3.new CancelRequest(messagetype, mCurrentId, reservation_id);
            cr.putJSONObject(jsonObject);
            break;

        case PARKING_LOT_STATUS_REQUEST:
            AttendantModel attendantModel = (AttendantModel)mAttendantFactory.mBaseModel;
            messagetype = MessageType.PARKING_LOT_STATUS_REQUEST.getText();
            AttendantModel.ParkinglotStatus_Request pr = attendantModel.new ParkinglotStatus_Request(messagetype);
            pr.putJSONObject(jsonObject);
            break;
        // Owner
        case CHANGE_PARKINGFEE:
            OwnerModel ownerModel = (OwnerModel)mOwnerFactory.mBaseModel;
            messagetype = MessageType.CHANGE_PARKINGFEE.getText();
            String parkinglotid = bundle.getString("parkinglot_id");
            String parking_fee = bundle.getString("fee");
            OwnerModel.Change_parkingfee cp = ownerModel.new Change_parkingfee(messagetype, parkinglotid, parking_fee);
            cp.putJSONObject(jsonObject);
            break;
        case CHANGE_GRACEPERIOD:
            OwnerModel ownerModel1 = (OwnerModel)mOwnerFactory.mBaseModel;
            messagetype = MessageType.CHANGE_PARKINGFEE.getText();
            String parkinglot_id1 = bundle.getString("parkinglot_id");
            String graceperiod = bundle.getString("graceperiod");
            OwnerModel.Change_graceperiod cg = ownerModel1.new Change_graceperiod(messagetype, parkinglot_id1,
                    graceperiod);
            cg.putJSONObject(jsonObject);
            break;
        case REMOVE_PARKINGLOT:
            OwnerModel ownerModel2 = (OwnerModel)mOwnerFactory.mBaseModel;
            messagetype = MessageType.REMOVE_PARKINGLOT.getText();
            String parkinglot_id2 = bundle.getString("parkinglot_id");
            OwnerModel.Remove_Parkinglot rp = ownerModel2.new Remove_Parkinglot(messagetype, parkinglot_id2);
            rp.putJSONObject(jsonObject);
            break;
        case CREATE_ATTENDANT:
            OwnerModel ownerModel3 = (OwnerModel)mOwnerFactory.mBaseModel;
            messagetype = MessageType.CREATE_ATTENDANT.getText();
            String id3 = bundle.getString("id");
            String pwd3 = bundle.getString("pwd");
            String name3 = bundle.getString("name");
            String parkinglot_id3 = bundle.getString("parkinglot_id");
            OwnerModel.Create_Attendant ca = ownerModel3.new Create_Attendant(messagetype, id3, pwd3, name3,
                    parkinglot_id3);
            ca.putJSONObject(jsonObject);
            break;
        case ADD_PARKINGLOT:
            OwnerModel ownerModel4 = (OwnerModel)mOwnerFactory.mBaseModel;
            messagetype = MessageType.ADD_PARKINGLOT.getText();
            String id4 = bundle.getString("id");
            String pwd4 = bundle.getString("pwd");
            String address = bundle.getString("address");
            String parkingfee = bundle.getString("parkingfee");
            String graceperiod4 = bundle.getString("graceperiod");
            OwnerModel.Add_Parkinglot ap = ownerModel4.new Add_Parkinglot(messagetype, id4, pwd4, address, parkingfee,
                    graceperiod4);
            ap.putJSONObject(jsonObject);
            break;
        case PARKINGLOT_STATS_REQUEST:
            OwnerModel ownerModel5 = (OwnerModel)mOwnerFactory.mBaseModel;
            messagetype = MessageType.PARKINGLOT_STATS_REQUEST.getText();
            String parkinglot_id5 = bundle.getString("parkinglot_id");
            String period5 = bundle.getString("period");
            OwnerModel.Parkinglot_stats_Request psr = ownerModel5.new Parkinglot_stats_Request(messagetype,
                    parkinglot_id5, period5);
            psr.putJSONObject(jsonObject);
            break;
        default:
            break;
        }
        boolean success = mNetworkManager.sendMessage(jsonObject);
        if (!success) {
            Utils.showToast(this, "Server was not connected");
        }
    }
}

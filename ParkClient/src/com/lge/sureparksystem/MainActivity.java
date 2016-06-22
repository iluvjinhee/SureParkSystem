package com.lge.sureparksystem;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.lge.sureparksystem.control.NetworkManager;
import com.lge.sureparksystem.control.NetworkToActivity;
import com.lge.sureparksystem.model.DriverModel;
import com.lge.sureparksystem.model.LoginModel;
import com.lge.sureparksystem.parkclient.R;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.util.Utils;
import com.lge.sureparksystem.view.BaseView;
import com.lge.sureparksystem.view.RequestData;

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
        mLoadingView.setVisibility(View.GONE);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
            LoginModel loginModel1 = (LoginModel)mLoginFactory.mBaseModel;
            LoginModel.Response r = loginModel1.new Response(jsonObject);
            break;
        case AUTHENTICATION_RESPONSE:
            LoginModel loginModel = (LoginModel)mLoginFactory.mBaseModel;
            LoginModel.Authentication_Response ar = loginModel.new Authentication_Response(jsonObject);
            if ("ok".equals(ar.getResult())) {
                mCurrentAutority = ar.geAuthority();
                if (Utils.DRIVERVIEW_FRAGMENT == mCurrentAutority) {
                    viewLoadingView();
                    decideFragment(Utils.DRIVERVIEW_FRAGMENT, null);
                    requsetServer(RequestData.RESERVATION_INFO_REQUEST, null);
                } else {
                    decideFragment(ar.geAuthority(), null);
                }
            } else {
                mCurrentId = null;
                mCurrentAutority = 0;
            }
            break;
        // Driver
        case PARKINGLOT_LIST_RESPONSE:
            DriverModel parkinglot_list = (DriverModel)mDriverFactory.mBaseModel;
            parkinglot_list.mParkinglot_List = parkinglot_list.new Parkinglot_List(jsonObject);
            mDriverFactory.mBaseFragment.setBaseModel(mDriverFactory.mBaseModel);
            break;
        case RESERVATION_INFORMATION_RESPONSE:
            DriverModel information_response = (DriverModel)mDriverFactory.mBaseModel;
            information_response.mReservation_Information = information_response.new Reservation_Information(jsonObject);
            goneLoadingView();
            if ("ok".equals(information_response.mReservation_Information.getResult())) {
                // Instance update in Driver view
                mDriverFactory.mBaseFragment.setBaseModel(mDriverFactory.mBaseModel);
            } else {
                // reserved flag update in Driver view
                requsetServer(RequestData.PARKINGLOT_INFO_REQUEST, null);
            }
            // Driver fragment update
            break;
        case CANCEL_RESPONSE:
            DriverModel cancel_response = (DriverModel)mDriverFactory.mBaseModel;
            cancel_response.mResponse = cancel_response.new Response(jsonObject);
            mDriverFactory.mBaseFragment.setBaseModel(mDriverFactory.mBaseModel);
            if ("ok".equals(cancel_response.mResponse.result)) {
                Utils.showToast(this, "Reservation canceled");
            } else {
                Utils.showToast(this, "Error check information");
            }
            break;
        // Attendant
        case PARKING_LOT_STATUS:
            break;
        case NOTIFICATION:
            break;
        default:
            break;
        }

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
        default:
            break;
        }
        mNetworkManager.sendMessage(jsonObject);
    }
}

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
import com.lge.sureparksystem.parkclient.R;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.message.SocketMessage;
import com.lge.sureparksystem.util.Utils;

import org.json.simple.JSONObject;

public class MainActivity extends Activity implements OnClickListener,
        NetworkToActivity {

    private static String TAG = "ParkClientActivity";
    private View mLoadingView;
    private NetworkManager mNetworkManager;
    private AbstractFactory mLoginFactory;
    private AbstractFactory mOwnerFactory;
    private AbstractFactory mAttendantFactory;
    private AbstractFactory mDriverFactory;
    private int mTempNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mLoadingView = findViewById(R.id.loading);
        mLoadingView.setVisibility(View.VISIBLE);
        mNetworkManager = new NetworkManager(Utils.IP_ADDRESS, Utils.PORT, this);
        mNetworkManager.connect();
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
        ft.replace(R.id.fragment_area, (Fragment)mLoginFactory.mBaseView,
                Utils.TAG_LOGIN_FRAGMENT);
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

    private void switchFagment(AbstractFactory factory, String fragment_tag, String viewName) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_area, (Fragment)factory.mBaseView,
                fragment_tag);
        ft.commitAllowingStateLoss();
        getActionBar().setTitle(viewName);
    }

    @Override
    public void onClick(View v) {
        int num = mTempNum % 4;
        String tag = null;
        String viewName = null;
        AbstractFactory af = null;
        if (num >= 4) {
            num = 0;
        }
        if (num == 0) {
            af = mLoginFactory;
            tag = Utils.TAG_LOGIN_FRAGMENT;
            viewName = this.getResources().getString(R.string.login);
        } else if (num == 1) {
            af = mAttendantFactory;
            tag = Utils.TAG_ATTENDANT_FRAGMENT;
            viewName = this.getResources().getString(R.string.attendant);
        } else if (num == 2) {
            af = mOwnerFactory;
            tag = Utils.TAG_OWNER_FRAGMENT;
            viewName = this.getResources().getString(R.string.owner);
        } else {
            af = mDriverFactory;
            tag = Utils.TAG_DRIVER_FRAGMENT;
            viewName = this.getResources().getString(R.string.driver);
        }
        switchFagment(af, tag, viewName);
        mTempNum++;
    }

    public void sendMessageToServer() {
        Log.d(TAG, "");
        JSONObject jsonObject = MessageParser.makeJSONObject(new SocketMessage(MessageType.RESERVATION_NUMBER));
        // To do divide message
        mNetworkManager.sendMessage(jsonObject);
    }
    @Override
    public void parseJSONMessage(String jsonMessage) {
        Log.d(TAG, "parseJSONMessage");
        SocketMessage socketMessage = MessageParser
                .parseJSONMessage(jsonMessage);
        MessageType messageType = socketMessage.getMessageType();
        Log.d(TAG, "messageType : " + messageType.getValue());
        switch (messageType) {
        case UNDEFINED:
            break;
        default:
            break;
        }

    }
}

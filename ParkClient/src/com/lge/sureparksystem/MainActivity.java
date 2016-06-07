package com.lge.sureparksystem;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.lge.sureparksystem.control.NetworkManager;
import com.lge.sureparksystem.parkclient.R;
import com.lge.sureparksystem.util.Utils;

public class MainActivity extends Activity implements OnClickListener{

    private static String TAG = "ParkClientActivity";
    private View mLoadingView;
    private NetworkManager mNetworkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mLoadingView = findViewById(R.id.loading);
        mLoadingView.setVisibility(View.VISIBLE);
        // mNetworkManager = new NetworkManager();
        findViewById(R.id.btn_temp).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        AbstractFactory loginFactory = new AttendantFactory();
        loginFactory.createModel();
        loginFactory.createView();
        ft.replace(R.id.fragment_area, (Fragment)loginFactory.mBaseView, Utils.TAG_LOGIN_FRAGMENT);
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

    @Override
    public void onClick(View v) {
//        getFragmentManager()
    }
}

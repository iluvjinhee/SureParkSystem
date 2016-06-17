package com.lge.sureparksystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.lge.sureparksystem.parkclient.R;

public class MainDialog extends DialogFragment {
    private int mLayoutId = R.layout.alert_dialog_text_entry;
    private int mTitle = R.string.ok;

    public MainDialog(int layoutId, int title) {
        super();
        this.mLayoutId = layoutId;
        this.mTitle = title;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder mCreateDialog = new AlertDialog.Builder(
                getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        mCreateDialog.setView(layoutInflater.inflate(mLayoutId, null));
        return mCreateDialog
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(mTitle)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {

                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {

                            }
                        }).create();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    
}

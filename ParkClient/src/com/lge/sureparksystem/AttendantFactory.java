package com.lge.sureparksystem;

import com.lge.sureparksystem.model.AttendantModel;
import com.lge.sureparksystem.model.BaseModel;
import com.lge.sureparksystem.view.AttendantView;
import com.lge.sureparksystem.view.BaseFragment;

public class AttendantFactory extends AbstractFactory{

    @Override
    BaseModel createModel() {
        mBaseModel = new AttendantModel();
        return mBaseModel;
    }

    @Override
    BaseFragment createView() {
        mBaseFragment = new AttendantView();
        return mBaseFragment;
    }

}

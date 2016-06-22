package com.lge.sureparksystem;

import com.lge.sureparksystem.model.BaseModel;
import com.lge.sureparksystem.model.DriverModel;
import com.lge.sureparksystem.view.BaseFragment;
import com.lge.sureparksystem.view.DriverView;

public class DriverFactory extends AbstractFactory {

    @Override
    BaseModel createModel() {
        mBaseModel = new DriverModel();
        return mBaseModel;
    }

    @Override
    BaseFragment createView() {
        mBaseFragment = new DriverView();
        return mBaseFragment;
    }

}

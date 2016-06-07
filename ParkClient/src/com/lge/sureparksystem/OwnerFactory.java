package com.lge.sureparksystem;

import com.lge.sureparksystem.model.BaseModel;
import com.lge.sureparksystem.model.OwnerModel;
import com.lge.sureparksystem.view.BaseView;
import com.lge.sureparksystem.view.OwnerView;

public class OwnerFactory extends AbstractFactory {

    @Override
    BaseModel createModel() {
        mBaseModel = new OwnerModel();
        return mBaseModel;
    }

    @Override
    BaseView createView() {
        mBaseView = new OwnerView();
        return mBaseView;
    }

}

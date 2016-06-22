package com.lge.sureparksystem;

import com.lge.sureparksystem.model.BaseModel;
import com.lge.sureparksystem.model.OwnerModel;
import com.lge.sureparksystem.view.BaseFragment;
import com.lge.sureparksystem.view.OwnerView;

public class OwnerFactory extends AbstractFactory {

    @Override
    BaseModel createModel() {
        mBaseModel = new OwnerModel();
        return mBaseModel;
    }

    @Override
    BaseFragment createView() {
        mBaseFragment = new OwnerView();
        return mBaseFragment;
    }

}

package com.lge.sureparksystem;

import com.lge.sureparksystem.model.BaseModel;
import com.lge.sureparksystem.model.LoginModel;
import com.lge.sureparksystem.view.BaseFragment;
import com.lge.sureparksystem.view.LoginView;

public class LoginFactory extends AbstractFactory {

    @Override
    BaseModel createModel() {
        mBaseModel = new LoginModel();
        return mBaseModel;
    }

    @Override
    BaseFragment createView() {
        mBaseFragment = new LoginView();
        return mBaseFragment;
    }

}

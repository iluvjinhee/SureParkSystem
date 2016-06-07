package com.lge.sureparksystem;

import com.lge.sureparksystem.model.BaseModel;
import com.lge.sureparksystem.view.BaseView;

public abstract class AbstractFactory {
    public BaseModel mBaseModel;
    public BaseView mBaseView;

    abstract BaseModel createModel();

    abstract BaseView createView();
}

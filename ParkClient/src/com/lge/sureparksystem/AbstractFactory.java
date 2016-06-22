package com.lge.sureparksystem;

import com.lge.sureparksystem.model.BaseModel;
import com.lge.sureparksystem.view.BaseFragment;

public abstract class AbstractFactory {
    public BaseModel mBaseModel;
    public BaseFragment mBaseFragment;

    abstract BaseModel createModel();

    abstract BaseFragment createView();
}

package com.zifang.util.ioc;

import com.google.inject.Binder;
import com.google.inject.Module;

public class TestModule implements Module {

    /* (non-Javadoc)
     * @see com.google.inject.Module#configure(com.google.inject.Binder)
     */
    public void configure(Binder binder) {
        binder.bind(Service.class).to(ServiceImp.class);
    }

}
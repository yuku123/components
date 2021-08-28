package com.zifang.util.core.pattern.ioc;

import javax.inject.Inject;
import javax.inject.Named;

@Named(value = "defaultBean")
public class Bean {
    @Inject
    Bean bean;
}

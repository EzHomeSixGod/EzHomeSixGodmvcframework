package com.ezhomesixgod.service.impl;

import com.ezhomesixgod.EzInterface.EzService;
import com.ezhomesixgod.controller.EzDemoController;
import com.ezhomesixgod.service.EzDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author renyang
 * @description
 * @createTime 2018-09-25 10:59
 */
@EzService("ezDemoService")
public class EzDemoServiceImpl implements EzDemoService {

    private static final Logger logger =LoggerFactory.getLogger(EzDemoServiceImpl.class);

    @Override
    public Object findList() {
        logger.info("findList>>>>");
        List<Object> list =new ArrayList<>();
        list.add("张三");
        list.add("李四");
        list.add("王五");
        return list;
    }
}

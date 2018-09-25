package com.ezhomesixgod.controller;

import com.ezhomesixgod.EzInterface.EzAutoWired;
import com.ezhomesixgod.EzInterface.EzController;
import com.ezhomesixgod.EzInterface.EzRequestMapping;
import com.ezhomesixgod.service.EzDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author renyang
 * @description
 * @createTime 2018-09-25 10:59
 */

@EzController
@EzRequestMapping(value = "EzDemoController")
public class EzDemoController {

    private static final Logger logger =LoggerFactory.getLogger(EzDemoController.class);

    @EzAutoWired
    private EzDemoService demoService;

    @EzRequestMapping("findList")
    public void findList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("EzDemoController findList>>>>");
       Object object =  demoService.findList();
        List<Object> list =(List<Object>)object;
        response.getWriter().write(list.toString());
    }
}

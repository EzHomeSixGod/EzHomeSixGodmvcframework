package com.ezhomesixgod.controller;

import com.ezhomesixgod.EzInterface.EzController;
import com.ezhomesixgod.EzInterface.EzRequestMapping;
import sun.misc.Contended;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author renyang
 * @description
 * @createTime 2018-09-25 18:30
 */
@EzController
@EzRequestMapping("login")
public class LoginController {
    @EzRequestMapping("login")
    public String login(HttpServletRequest request, HttpServletResponse response){
        String name = request.getParameter("userName");
        String passWord = request.getParameter("passWord");

        if("111".equals(name) && "111".equals(passWord)){
            return "success";
        }

        return "error";
    }
}

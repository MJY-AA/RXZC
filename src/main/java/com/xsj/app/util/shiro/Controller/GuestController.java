package com.xsj.app.util.shiro.Controller;

import org.apache.ibatis.mapping.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @PackageName:com.xsj.app.util.shiro.Controller
 * @Description:
 * @author:Xsj
 * @date 2020/4/22 0022 15:00
 */
@RestController
@RequestMapping("/guest")
public class GuestController{
   /* @Autowired
    private final ResultMap resultMap;

    public GuestController(ResultMap resultMap) {
        this.resultMap = resultMap;
    }

    @RequestMapping(value = "/enter", method = RequestMethod.GET)
    public ResultMap login() {
        return resultMap.success().message("欢迎进入，您的身份是游客");
    }

    @RequestMapping(value = "/getMessage", method = RequestMethod.GET)
    public ResultMap submitLogin() {
        return resultMap.success().message("您拥有获得该接口的信息的权限！");
    }*/
}
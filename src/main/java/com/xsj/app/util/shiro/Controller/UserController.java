package com.xsj.app.util.shiro.Controller;

import org.apache.ibatis.annotations.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @PackageName:com.xsj.app.util.shiro.Controller
 * @Description:
 * @author:Xsj
 * @date 2020/4/22 0022 15:04
 */
@RestController
@RequestMapping("/user")
public class UserController{
/*    @Autowired
    private final ResultMap resultMap;

    public UserController(ResultMap resultMap) {
        this.resultMap = resultMap;
    }

    @RequestMapping(value = "/getMessage", method = RequestMethod.GET)
    public ResultMap getMessage() {
        return resultMap.success().message("您拥有用户权限，可以获得该接口的信息！");
    }*/
}
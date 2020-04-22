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
 * @date 2020/4/22 0022 15:05
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
   /* @Autowired
    private final ResultMap resultMap;

    @RequestMapping(value = "/getMessage", method = RequestMethod.GET)
    public ResultMap getMessage() {
        return resultMap.success().message("您拥有管理员权限，可以获得该接口的信息！");
    }*/
}
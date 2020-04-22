package com.xsj.app.util.shiro.Controller;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.shiro.authc.AccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @PackageName:com.xsj.app.util.shiro.Controller
 * @Description:
 * @author:Xsj
 * @date 2020/4/22 0022 15:05
 */
@RestControllerAdvice
public class ExceptionController {
    /*private final ResultMap resultMap;

    @Autowired
    public ExceptionController(ResultMap resultMap) {
        this.resultMap = resultMap;
    }

    // 捕捉 CustomRealm 抛出的异常
    @ExceptionHandler(AccountException.class)
    public ResultMap handleShiroException(Exception ex) {
        return resultMap.fail().message(ex.getMessage());
    }*/
}
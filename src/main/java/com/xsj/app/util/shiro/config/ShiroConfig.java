package com.xsj.app.util.shiro.config;

import com.xsj.app.util.shiro.realm.CustomRealm;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @PackageName:com.xsj.app.util.shiro
 * @Description: Shiro 配置类
 * @author:Xsj
 * @date 2020/4/22 0022 11:43
 */
@Configuration
public class ShiroConfig {
    /**
     * 拦截器
     * @param securityManager
     * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
         // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // setLoginUrl 如果不设置值，默认会自动寻找Web工程根目录下的"/login.jsp"页面 或 "/login" 映射
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 设置无权限时跳转的 url;
        shiroFilterFactoryBean.setUnauthorizedUrl("/notRole");
        // 设置拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        /**
         * anon--无参，开放权限，可以理解为匿名用户或游客
         * authc--无参，需要认证
         * logout--无参，注销，执行后会直接跳转到shiroFilterFactoryBean.setLoginUrl(); 设置的 url
         * authcBasic--无参，表示 httpBasic 认证
         * user--无参，表示必须存在用户，当登入操作时不做检查
         * ssl--无参，表示安全的URL请求，协议为 https
         * perms[user]--参数可写多个，表示需要某个或某些权限才能通过，多个参数时写 perms["user, admin"]，当有多个参数时必须每个参数都通过才算通过
         * roles[admin]	--参数可写多个，表示是某个或某些角色才能通过，多个参数时写 roles["admin，user"]，当有多个参数时必须每个参数都通过才算通过
         * rest[user]--根据请求的方法，相当于 perms[user:method]，其中 method 为 post，get，delete 等
         * port[8081]--当请求的URL端口不是8081时，跳转到schemal://serverName:8081?queryString 其中 schmal 是协议 http 或 https 等等，serverName 是你访问的 Host，8081 是 Port 端口，queryString 是你访问的 URL 里的 ? 后面的参数
         * 说明
         * 1.anon, authc, authcBasic, user 是第一组认证过滤器
         * 2.perms, port, rest, roles, ssl 是第二组授权过滤器，要通过授权过滤器，就先要完成登陆认证操作才能走第二组授权器
         * 例如访问需要 roles 权限的 url，如果还没有登陆的话，会直接跳转到 shiroFilterFactoryBean.setLoginUrl(); 设置的 url
         */
        // anon游客--开发权限:所有url都都可以匿名访问
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/front/**", "anon");
        filterChainDefinitionMap.put("/api/**", "anon");
         //roles[user]用户，需要角色权限 “user”
        filterChainDefinitionMap.put("/user/**", "roles[user]");
        //管理员，需要角色权限 “admin”
        filterChainDefinitionMap.put("/admin/**", "roles[admin]");
        //authc:开放登陆接口 所有权限都可以访问
        filterChainDefinitionMap.put("/admin/**", "authc");
        filterChainDefinitionMap.put("/user/**", "authc");
        //其余接口一律拦截
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        System.out.println("--------------------Shiro拦截器工厂类注入成功-------------------");
        return shiroFilterFactoryBean;

    }

    /**
     *  注入 securityManager
     * @return
     */
    @Bean
    //此处注意要导入的import org.apache.shiro.mgt.SecurityManager;不是java.lang.SecurityManager这个包
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        // 设置realm
        defaultSecurityManager.setRealm(customRealm());
        return defaultSecurityManager;
    }

    /**
     * 自定义身份认证
     * 必须写这个并且加上@bean注解目的是为了注入CustomRealm
     * 否则会影响 CustomRealm类 中其他类的依赖注入
     * @return
     */
    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        return customRealm;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * *
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * *
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * * @return
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 示例
     */
    @RequestMapping("/user")
    @Controller
    public class UserController {
        @RequiresPermissions("user:list")
        @ResponseBody
        @RequestMapping("/show")
        public String showUser() {
            return "这是学生信息";
        }
    }
}

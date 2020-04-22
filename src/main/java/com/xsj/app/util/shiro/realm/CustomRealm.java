package com.xsj.app.util.shiro.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @PackageName:com.xsj.app.util.shiro.realm
 * @Description: 自定义 realm 类
 * @author:Xsj
 * @date 2020/4/22 0022 11:45
 */
/*@Component*/
public class CustomRealm extends AuthorizingRealm {

    /**
     * 注意事项
     * 有很多人会发现，UserMapper 等类，接口无法通过 @Autowired 注入进来，跑程序的时候会报 NullPointerException，
     * 网上说了很多诸如是 Spring 加载顺序等原因，但其实有一个很重要的地方要大家注意，
     * CustomRealm 这个类是在 shiro 配置类的 securityManager.setRealm() 方法中设置进去的
     * 而很多人直接写securityManager.setRealm(new CustomRealm()); ,这样是不行的，必须要使用 @Bean 注入 MyRealm，不能直接 new 对象
     */


     /**
     * 身份认证:即登录通过账号和密码验证登陆人的身份信息。
      * 获取身份验证信息
     * Shiro中，最终是通过 Realm 来获取应用程序中的用户、角色及权限信息的。
     * @param --uthenticationToken 用户身份信息 token
     * @return 返回封装了用户信息的 AuthenticationInfo 实例
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("------------------身份认证方法------------");
        //获取token
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 从数据库获取对应用户名密码的用户
        /**
         * 这两种方法用哪一种都可以 主要就是返回的一个是string类型的 一个是char类型的
         * String userName = (String) token.getPrincipal()
         * token的用法
         * token.getUsername()  //获得用户名 String
         * token.getPrincipal() //获得用户名 Object
         * token.getPassword()  //获得密码 char[]
         * token.getCredentials() //获得密码 Object
         */
        String userName = token.getUsername();
        String userPwd = new String((char[]) token.getCredentials());
        //此处应该是从数据库查询出来密码,时间问题我把密码写死了
        String password = "2415b95d3203ac901e287b76fcef640b";
        if (userName == null) {
            throw new AccountException("用户名不正确");
        } else if (!userPwd.equals(password )) {
            throw new AccountException("密码不正确");
        }
        String realmName = new String();
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, password,
                ByteSource.Util.bytes(userName + "salt"),realmName);

        return authenticationInfo;
    }
    /**
     * 权限相关:即登录过后，每个身份不一定，对应的所能看的页面也不一样。
     * @param
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("-----------------权限认证方法-------------");
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        /**
         * 此处应该去数据库 我直接死了 后期自行修改
         * String role = userMapper.getRole(username);
         * Set<String> stringSet = new HashSet<>();
         * stringSet.add("role");
         */
        //获得该用户角色
        String role ="user:show";
        Set<String> stringSet = new HashSet<>();
        //需要将 role 封装到 Set 作为 info.setRoles() 的参数
        stringSet.add(role);
        stringSet.add("user:admin");
        //设置该用户拥有的角色
        info.setStringPermissions(stringSet);
        return info;
    }



     /**
     * md5加密
     * @return
     */
    @Bean(name = "credentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setHashIterations(2);
        // storedCredentialsHexEncoded默认是true，此时用的是密码加密用的是Hex编码；false时用Base64编码
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        // 告诉realm,使用credentialsMatcher加密算法类来验证密文
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        customRealm.setCachingEnabled(false);
        return customRealm;
    }

    /**
     * 密码加密工具类
     * @param username
     * @param pwd
     * @return
     */
    public static String MD5Pwd(String username, String pwd) {
        // 加密算法MD5
        // salt盐 username + salt
        // 迭代次数
        String md5Pwd = new SimpleHash("MD5", pwd,
                ByteSource.Util.bytes(username + "salt"), 2).toHex();
        return md5Pwd;
    }
}

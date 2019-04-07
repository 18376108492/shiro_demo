package com.itdan.shiro.demo01;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Helloworld {

    private static Logger logger=LoggerFactory.getLogger(Helloworld.class);

    public static void main(String[] args) {

        logger.info("开始测试");
        /**
         * 1.获取安全管理器
         * 2.获取用户
         * 3.用户登入验证
         * 4.权限管理
         * 5.角色管理
         * 6.session
         */

        //1.获取安全管理器
        Factory<SecurityManager> factory=new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager= factory.getInstance();
        //2.需要设置安全管理器
        SecurityUtils.setSecurityManager(securityManager);
        //3.获取Subject对象(代表你即将登入的对象)
        Subject currentUser= SecurityUtils.getSubject();
        Session session=currentUser.getSession();

        session.setAttribute("name","小明");

        String name=(String) session.getAttribute("name");
        if (name!=null&&!name.equals(" ")) {
         logger.info("shiro中获取的数据为"+name);
        }

        //如果当前用户没有登入
        if(currentUser.isAuthenticated()==false){
            //UsernamePasswordToken
            UsernamePasswordToken token= new UsernamePasswordToken("lonestarr","vespa");
            token.setRememberMe(true);
            logger.info("无异常，登入成功");
            try {
                currentUser.login(token);
            }catch (UnknownAccountException e){
                logger.info("账号不存在");
            }catch (IncorrectCredentialsException e){
                logger.info("密码不正确");
            }catch (LockedAccountException e){
                logger.info("用户已锁死");
            }catch (AuthenticationException e){
                logger.info("认证异常");
            }
        }

        if(currentUser.hasRole("goodguy")){
            logger.info("拥有该指定的角色");
        }else {
            logger.info("不拥有该指定的角色");
        }

        if(currentUser.isPermitted("winnebago:drive:eagle5")){
            logger.info("拥有该指定权限");
        }else {
            logger.info("不拥有该指定权限");
        }
    }
}

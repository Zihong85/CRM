package com.osu.web.listener;

import com.osu.settings.entity.DicValue;
import com.osu.settings.service.DicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {

    @Autowired
    private DicService dicService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        // 在Listener监听器中无法使用Spring容器的@Resource或者@Autowired 注解的方法注入bean，因为，在web Server容器中，无论是Servlet，Filter，还是Listener都不是Spring容器管理的，因此我们都无法在这些类中直接使用Spring注解的方式来注入我们需要的对象。
        // 解决在监听器类中无法自动注入问题
        WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
        ServletContext application = sce.getServletContext();
        Map<String, List<DicValue>> map = dicService.getAll();
        Set<String> set = map.keySet();
        for (String key : set) {
            application.setAttribute(key, map.get(key));
        }

        Map<String,String> pMap = new HashMap<>();
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = rb.getKeys();
        while (e.hasMoreElements()){
            //阶段
            String key = e.nextElement();
            //可能性
            String value = rb.getString(key);
            pMap.put(key, value);
        }
        //将pMap保存到服务器缓存中
        application.setAttribute("pMap", pMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}

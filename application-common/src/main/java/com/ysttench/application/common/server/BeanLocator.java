package com.ysttench.application.common.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class BeanLocator {

    private static final String CONTEXT_LOCATION = "/applicationContext.xml";

    private static final String[] locations = { CONTEXT_LOCATION};

    private static ApplicationContext app;

    public static Object getBean(String name) {
        if (app == null) {
            app = new ClassPathXmlApplicationContext(locations);
        }
        return app.getBean(name);
    }
    
    public static void init() {
        if (app == null) {
            app = new ClassPathXmlApplicationContext(locations);
        }
    }
}

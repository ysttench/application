package com.ysttench.application.auth.settings.web.listener;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.ysttench.application.common.server.ConfigUtils;
import com.ysttench.application.database.ibatis.mapper.BaseMapper;


/**
 * 
 * @author Howard
 */
@Repository
public class InitMapperListener implements ServletContextListener {
    
    private static Log logger = LogFactory.getLog(InitMapperListener.class);
    
    @Inject
    private BaseMapper baseMapper;
    
    // 从 application.properties 中读取配置
    @Value("${spring.datasource.url}")
    private String url = "";
    
    @Value("${spring.datasource.username}")
    private String username = "";
    
    @Value("${spring.datasource.dialect}")
    private String dialect = "";
    
    @Value("${spring.datasource.mybatis.entity}")
    private String entity = "";

    public void contextInitialized(ServletContextEvent sce) {
        logger.info("配置信息初始化Start!!!");
        if ("mysql".equalsIgnoreCase(dialect)) {
            String db = url.substring(url.lastIndexOf("/") + 1);
            if (db.indexOf("?") > -1) {
                db = db.substring(0, db.indexOf("?"));
            }
            ConfigUtils.initTableFieldForMysql(baseMapper, entity, db);
            /*SysDepartmentController sysDepartmentController = new SysDepartmentController(sysDepartmentMapper, sysCodeConfigMapper);
            new Thread(sysDepartmentController).start();*/
        } else if ("oracle".equalsIgnoreCase(dialect)) {
            ConfigUtils.initTableFieldForOracle(baseMapper, entity, username);
        }
        logger.info("配置信息初始化End!!!");
    }

    public void contextDestroyed(ServletContextEvent arg0) {
        logger.info("配置信息清除!!!");
    }
}
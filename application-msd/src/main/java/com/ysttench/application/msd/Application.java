package com.ysttench.application.msd;

import java.util.HashSet;
import java.util.Set;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.ysttench.application.msd.core.service.AuthService;
import com.ysttench.application.msd.core.service.impl.AuthServiceImpl;
import com.ysttench.application.msd.settings.web.filter.CheckSessionOutFilter;



@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebEnvironment(true);
          
        Set<Object> set = new HashSet<Object>();
        set.add("classpath:spring-application.xml");
        set.add("classpath:spring-mvc.xml");
        app.setSources(set);
        app.run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
    
    /**
     * 使用代码注册Servlet（不需要@ServletComponentScan注解）
     *
     * @return
     * @author Howard
     * @create  2018年12月26日
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new CXFServlet(), "/services/*");// ServletName默认值为首字母小写，即myServlet
    }
    
    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }
    @Bean
    public AuthService authService() {
        return new AuthServiceImpl();
    }
    
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), authService());
        endpoint.publish("/AuthService");
        return endpoint;
    }
    /**
     * check session time的拦截器
     * 
     * @return
     */
    @Bean
    public FilterRegistrationBean checkSessionOutFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(checkSessionOutFilter());
        registration.addUrlPatterns("/*");
        // registration.addInitParameter("paramName", "paramValue");
        registration.setName("SessionTimeOutFilter");
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public CheckSessionOutFilter checkSessionOutFilter() {
        return new CheckSessionOutFilter();
    }

    /**
     * 编码拦截器
     * 
     * @return
     */
    @Bean
    public FilterRegistrationBean characterEncodingFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(checkSessionOutFilter());
        registration.addUrlPatterns("/*");
        registration.setName("forceEncodingFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }
}

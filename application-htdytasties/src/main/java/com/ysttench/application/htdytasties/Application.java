package com.ysttench.application.htdytasties;

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
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;
import com.ysttench.application.htdytasties.core.service.AuthService;
import com.ysttench.application.htdytasties.core.service.Impl.AuthServiceImpl;
import com.ysttench.application.htdytasties.settings.web.filter.CheckSessionOutFilter;
/**
 * 启动文件
 * @author Howard
 *
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
	SpringApplication.run(Application.class, args);
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
	// ServletName默认值为首字母小写，即myServlet
        return new ServletRegistrationBean(new CXFServlet(), "/services/*");
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

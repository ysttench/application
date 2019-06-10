package com.ysttench.application.equip.settings.web.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.ysttench.application.common.server.SessionUtil;



public class CheckSessionOutFilter implements Filter {
    protected FilterConfig filterConfig = null;

    /**
     * Take this filter out of service.
     */
    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * (对用户登陆做session check)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest hsrq = (HttpServletRequest) request;

        Object person = null;
        String reqPage = hsrq.getServletPath();
        
        if(reqPage.indexOf(hsrq.getContextPath())!=-1){
        	reqPage=reqPage.substring(reqPage.indexOf(hsrq.getContextPath()),reqPage.length());
        }
        // 后台管理的sessioncheck
        if (!reqPage.trim().equals("/") 
                && !reqPage.trim().equals("/login")
                && !(reqPage.trim().indexOf(".") != -1)
                && !(reqPage.trim().indexOf("/services") != -1)
                && !reqPage.trim().equals("/register/add")
                && !reqPage.trim().equals("/register/checkField")
                && !reqPage.trim().equals("/apiUser/addEntity")
                ) {
            person = SessionUtil.getUserSession();
            if (person == null) {
//                hsrp.sendRedirect(hsrq.getContextPath() + "/login");
                PrintWriter out = response.getWriter();
                out.println("<html>");
                out.println("<script type=\"text/JavaScript\">");
                out.println("   window.open('" + hsrq.getContextPath() + "/login','_top')");
                out.println("</script>");
                out.println("</html>");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
}

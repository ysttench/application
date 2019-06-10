package com.ysttench.application.htdytasties.settings.web.filter;

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


/**
 * 
 * @author Howard
 *
 */
public class CheckSessionOutFilter implements Filter {
    static String check = "/";
    protected FilterConfig filterConfig = null;

    /**
     * Take this filter out of service.
     */
    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * (对用户登陆做session check)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest hsrq = (HttpServletRequest) request;

        Object person = null;
        String reqPage = hsrq.getServletPath();
        
        if(reqPage.indexOf(hsrq.getContextPath())!=-1){
        	reqPage=reqPage.substring(reqPage.indexOf(hsrq.getContextPath()),reqPage.length());
        }
        // 后台管理的sessioncheck
        if (!check.equals(reqPage.trim()) 
                && !"/login".equals(reqPage.trim())
                && (reqPage.trim().indexOf(".") == -1)
                && (reqPage.trim().indexOf("/services") == -1)
                && !"/register/add".equals(reqPage.trim())
                && !"/register/checkField".equals(reqPage.trim())
                && !"/user/addEntity".equals(reqPage.trim())
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
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
}

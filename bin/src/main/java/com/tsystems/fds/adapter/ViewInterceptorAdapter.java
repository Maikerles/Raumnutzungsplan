package com.sae.fds.adapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sae.fds.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Configuration
public class ViewInterceptorAdapter extends HandlerInterceptorAdapter {
    @Autowired
    AccountService accountService;
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null && !modelAndView.getViewName().contains("error")) {
            modelAndView.addObject("g_account", accountService.getLoggedInAccount());
        }
    }
}

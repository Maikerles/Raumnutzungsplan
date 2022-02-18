package com.sae.fds.adapter;

import javax.servlet.http.HttpServletRequest;

import com.sae.fds.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.sae.fds.domain.Account;



@ControllerAdvice
class ExceptionAdapter {
    
    @Autowired
    AccountService accountService;
    
    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        Account account = accountService.getLoggedInAccount();
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.addObject("account", account);
        mav.setViewName(DEFAULT_ERROR_VIEW);
        
        e.printStackTrace();
        return mav;
    }
}
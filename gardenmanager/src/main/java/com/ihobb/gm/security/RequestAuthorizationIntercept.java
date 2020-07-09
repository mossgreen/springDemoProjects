package com.ihobb.gm.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

//@Aspect
//@Component
//public class RequestAuthorizationIntercept {
//
//    @Around("@annotation(com.ihobb.gm.security.RequestAuthorization.java)")
//    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
//        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//        if (null == userDetails) {
//            throw new RuntimeException("Access is Denied. Please again login or  contact service provider");
//        }
//        return joinPoint.proceed();
//
//        // todo user userService, find this user's orgCode,
//        /*
//        *         String tenantName = map.get(userDetails.getUsername());
//        if (tenantName != null && tenantName.equals(DBContextHolder.getCurrentDb())) {
//            return pjp.proceed();
//        }
//        throw new RuntimeException("Access is Denied. Please again login or contact service provider");
//        * */
//    }
//
//}

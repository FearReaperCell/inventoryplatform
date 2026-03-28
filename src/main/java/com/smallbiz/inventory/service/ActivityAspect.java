package com.smallbiz.inventory.service;

// 🔽 IMPORTS GO RIGHT HERE
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ActivityAspect {

    private final ActivityLogService activityLogService;

    public ActivityAspect(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @AfterReturning("execution(* com.smallbiz.inventory.web.*.*(..))")
    public void logControllerActions(JoinPoint joinPoint) {

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        String username = "SYSTEM";
        try {
            username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        } catch (Exception ignored) {}

        activityLogService.log(
            "ACTION",
            username,
            className + " -> " + methodName
        );
    }
}

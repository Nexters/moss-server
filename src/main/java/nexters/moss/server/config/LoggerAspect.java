package nexters.moss.server.config;

import nexters.moss.server.application.dto.Response;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Aspect
@Component
public class LoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    @Pointcut("execution(* nexters.moss.server.*.*.login(..)) || execution(* nexters.moss.server.*.*.join(..))")
    public void ignoredPointCut() {
    }

    @Pointcut("execution(* nexters.moss.server.web.*Controller.*(..))")
    public void loggingPointCut() {
    }

    @Pointcut("execution(* nexters.moss.server.web.*ExceptionHandler.*(..))")
    public void exceptionPointCut() {
    }

    @Pointcut("execution(* nexters.moss.server.application.*.*(..)) " +
            "&& execution(* nexters.moss.server.domain.*.*(..)) " +
            "&& execution(* nexters.moss.server.infra.*.*(..))")
    public void debuggingPointCut() {
    }

    @Around("loggingPointCut() && !ignoredPointCut()")
    public Object logInfo(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("====================================== START ======================================");
        logger.info("Controller: " + pjp.getSignature().getDeclaringTypeName());
        logger.info("Method: " + pjp.getSignature().getName());
        String params = Arrays.stream(pjp.getArgs())
                .map(Object::toString)
                .reduce((prev, current) -> prev + ", " + current)
                .orElse("");
        logger.info("Param: " + params);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        logger.info("HTTP Method: " + request.getMethod());
        logger.info("URI: " + request.getRequestURI());

        logger.info("Controller passes the request");
        Object result = pjp.proceed();
        logger.info("======================================= END =======================================");
        return result;
    }

    @Around("debuggingPointCut() && !ignoredPointCut()")
    public Object logDebug(ProceedingJoinPoint pjp) throws Throwable {
        logger.debug("Class: " + pjp.getSignature().getDeclaringTypeName());
        logger.debug("Method: " + pjp.getSignature().getName());
        String params = Arrays.stream(pjp.getArgs())
                .map(Object::toString)
                .reduce((prev, current) -> prev + ", " + current)
                .orElse("");
        logger.debug("Param: " + params);

        Object result = pjp.proceed();
        logger.debug(pjp.getSignature().getDeclaringTypeName() + " " + pjp.getSignature().getName() + " execution is finished");
        return result;
    }

    @Around("exceptionPointCut() && !ignoredPointCut()")
    public Object logException(ProceedingJoinPoint pjp) throws Throwable {
        logger.error("Class: " + pjp.getSignature().getDeclaringTypeName());
        logger.error("Method: " + pjp.getSignature().getName());
        String params = Arrays.stream(pjp.getArgs())
                .map(Object::toString)
                .reduce((prev, current) -> prev + ", " + current)
                .orElse("");
        logger.error("Param: " + params);

        Object result = pjp.proceed();
        logger.error(pjp.getSignature().getDeclaringTypeName() + " " + pjp.getSignature().getName() + " exception handling is finished");
        return result;
    }
}

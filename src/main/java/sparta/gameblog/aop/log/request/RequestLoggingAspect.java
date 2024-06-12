package sparta.gameblog.aop.log.request;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RequestLoggingAspect {

    private final HttpServletRequest request;

    @Pointcut("execution(* sparta.gameblog.web.controller..*.*(..))")
    public void methodsFromController() {}

    @Around("methodsFromController()")
    public Object requestLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            log.info("request URL: " + request.getRequestURI());
            log.info("HTTP Method: " + request.getMethod());
        }
    }
}

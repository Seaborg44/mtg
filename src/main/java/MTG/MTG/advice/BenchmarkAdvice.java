package MTG.MTG.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnExpression("${aspect.enable:true}")
public class BenchmarkAdvice {

    private final static Logger LOGGER = LoggerFactory.getLogger(BenchmarkAdvice.class);

    @Around("@annotation(MTG.MTG.config.Benchmark)")
    public Object measureTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = proceedingJoinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        LOGGER.info(", Time consumed: " + executionTime + "ms");
        return proceed;
    }
}

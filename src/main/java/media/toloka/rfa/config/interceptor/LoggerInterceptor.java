package media.toloka.rfa.config.interceptor;

// https://www.baeldung.com/spring-mvc-handlerinterceptor-vs-filter

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoggerInterceptor implements HandlerInterceptor {

    final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        String remoteAddr = request.getHeader("X-FORWARDED-FOR");
        if (remoteAddr == null || "".equals(remoteAddr)) {
            remoteAddr = request.getRemoteAddr();
        }
        String requestUrl = request.getRequestURL().toString();
        if (
                requestUrl.contains("rfa.toloka.media/assets")
                || requestUrl.contains("rfa.toloka.media/css/")
                || requestUrl.contains("rfa.toloka.media/js/")
                || requestUrl.contains("http://localhost:")
        ) {
            return true;
        } else {
            logger.info(remoteAddr + " => " +request.getRequestURL().toString());
        }
        // todo Логировать обращения к серверу.

//        System.out.println(remoteAddr);
        return true;
    }
}
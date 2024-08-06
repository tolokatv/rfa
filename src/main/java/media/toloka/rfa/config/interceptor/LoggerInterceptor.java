package media.toloka.rfa.config.interceptor;

// https://www.baeldung.com/spring-mvc-handlerinterceptor-vs-filter

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerInterceptor implements HandlerInterceptor {

    final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

//    @Autowired
//    public HttpSession httpSession;

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

            // Працюємо  із сессією та куками
            HttpSession httpSession = request.getSession();
//            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            logger.info("Session ID={} AND CreationTime={}",httpSession.getId(),formatter.format(httpSession.getCreationTime()));

            Cookie[] cookies =  request.getCookies();

//            logger.info("========= cookies not null = {} ",cookies != null);
//            if (cookies != null) {
//                logger.info("========= cookies length = {} ", cookies.length);
//            }

            if (    cookies != null
                    &&
                    cookies.length > 0
            ) {
//                logger.info("========= loop ==========");
                for (Cookie c : cookies) {
//                    logger.info("Cookies={} value={}", c.getName(), c.getValue());
                    if (c.getName().equals("LastVisit")) {
                        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        logger.info("=== Session ID={} LastVisit={}",httpSession.getId(),formatter.format(Long.parseLong(c.getValue())));
                    }
                }
//                logger.info("=========================");
            }
            // create a cookie
            Long ldate = new Date().getTime();
            Cookie cookie = new Cookie("LastVisit", ldate.toString());
            //add cookie to response
            response.addCookie(cookie);
//            logger.info("=========== Додали Куку ==============");

//            response.addCookie(
//                    new Cookie(
//                            "LastGet",
//                            formatter.format(new Date())
//                    )
//            );

//            Cookie[] cookie =  request.getCookies();
        }
        // todo Логировать обращения к серверу.

//        System.out.println(remoteAddr);
        return true;
    }
}
package media.toloka.rfa.config;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import media.toloka.rfa.radio.client.model.Clientdetail;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.message.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static java.lang.String.valueOf;

@Component
public class NavInterceptor implements HandlerInterceptor {

    @Autowired
    private MessageService messageService;
    @Autowired
    private ClientService clientService;

    final Logger logger = LoggerFactory.getLogger(NavInterceptor.class);

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) throws Exception {
        if (modelAndView == null) {
            return;
        }
        // your code
        Clientdetail cd;
        cd = clientService.getClientDetail(clientService.GetCurrentUser());
        if (cd == null) {
            logger.info("Handle Interseptor cd = null");
            return;
        }
        // додали кількість повідомлень для меню
        // TODO Подивитися чому ми викликаємо цю процедуру декілька разів.
//        ModelMap mm = modelAndView.getModelMap();
        modelAndView.getModel().put("quantitynewmessage",  messageService.GetQuantityNewMessage(cd));
        modelAndView.getModel().put("quantityallmessage",  messageService.GetQuantityAllMessage(cd));
        modelAndView.getModel().put("danger",  "У Вас нові повідомлення: "+ valueOf(messageService.GetQuantityNewMessage(cd)));
//        mm.put("quantitynewmessage",  messageService.GetQuantityNewMessage(cd));
//        mm.put("quantityallmessage",  messageService.GetQuantityAllMessage(cd));
//        mm.put("danger",  "У Вас нові повідомлення: "+ valueOf(messageService.GetQuantityNewMessage(cd)));
//        mm.addAttribute("quantityallmessage",  messageService.GetQuantityAllMessage(cd));
//        mm.addAttribute("quantitynewmessage",  messageService.GetQuantityNewMessage(cd));
//        logger.info("Handle Interseptor all={} new={} ",messageService.GetQuantityAllMessage(cd), messageService.GetQuantityNewMessage(cd) );
//        modelAndView.
    }
}

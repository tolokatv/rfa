package media.toloka.rfa.rpc;


//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class RPCController {
Logger logger = LoggerFactory.getLogger(RPCController.class);

    @Autowired
    RabbitTemplate template;

    @Value("${rabbitmq.queue}")
    private String queueName;

    @RequestMapping("/emit")
    @ResponseBody
    String home() {
        return "Empty mapping";
    }


    @RequestMapping("/process/{message}")
    @ResponseBody
    String error(@PathVariable("message") String message) {
        logger.info(String.format("Emit '%s'",message));
        String response = (String) template.convertSendAndReceive(queueName,message);
        // template.convertAndSend("rfajob",message);
        logger.info(String.format("Received on producer '%s'",response));
        return String.valueOf("returned from worker : " + response);
    }
}
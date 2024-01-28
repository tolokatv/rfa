package media.toloka.rfa.radio.root;

//import media.toloka.rfa.radio.model.MessageFromSite;
//import media.toloka.rfa.service.ServiceMessageFromSite;
import media.toloka.rfa.model.MessageFromSite;
import media.toloka.rfa.radio.root.service.ServiceMessageFromSite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Date;

@Controller
@EnableWebMvc
public class RootController {

    Logger logger = LoggerFactory.getLogger(RootController.class);

    @Autowired
    private ServiceMessageFromSite smfs; // = new ServiceMessageFromSite();

    @GetMapping(value = "/")
    public String index(Model model) {

        MessageFromSite QuestionForm = new MessageFromSite();
        model.addAttribute("question", QuestionForm);
        return "/root";
    }

    @GetMapping(value="/saveform/root")
    public String submitquestion (Model model, @ModelAttribute("question") MessageFromSite rootQuestionForm) {
        MessageFromSite mfs = new MessageFromSite();
        mfs.setName(rootQuestionForm.getName());
        mfs.setEmail(rootQuestionForm.getEmail());
        mfs.setPhone(rootQuestionForm.getPhone());
        mfs.setMessage(rootQuestionForm.getMessage());
        mfs.setContact_datetime(new Date());
        smfs.save(mfs);

        return "redirect:/#contact";
    }

}

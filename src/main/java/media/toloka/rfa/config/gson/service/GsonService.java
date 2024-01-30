package media.toloka.rfa.config.gson.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import media.toloka.rfa.config.gson.LocalDateDeserializer;
import media.toloka.rfa.config.gson.LocalDateSerializer;
import media.toloka.rfa.config.gson.LocalDateTimeDeserializer;
import media.toloka.rfa.config.gson.LocalDateTimeSerializer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class GsonService {
    public Gson CreateGson() {
        // https://www.javaguides.net/2019/11/gson-localdatetime-localdate.html
        GsonBuilder gbuilder = new GsonBuilder();
//        Gson gson = new GsonBuilder()
//                .excludeFieldsWithoutExposeAnnotation()
//                .create();
        gbuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gbuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        gbuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gbuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        return gbuilder.setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    }
}

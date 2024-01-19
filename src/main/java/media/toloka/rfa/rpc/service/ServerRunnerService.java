package media.toloka.rfa.rpc.service;

import com.google.gson.Gson;
import media.toloka.rfa.config.gson.service.GsonService;
import media.toloka.rfa.radio.client.service.ClientService;
import media.toloka.rfa.radio.station.model.Station;
import media.toloka.rfa.radio.station.service.StationService;
import media.toloka.rfa.rpc.model.RPCJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.UUID;

@Service
public class ServerRunnerService {
    // беремо з файлу конфігурації всю інформацію щодо параметрів стосовно файлової системи:
    // місце розташування, назви скриптів, файли шаблонів тощо.
    @Value("${rabbitmq.queue}")
            private String queueName;
    @Value("${media.toloka.rfa.server.client_dir}")
            private String clientdir;
    @Value("${media.toloka.rfa.server.run_dir}")
    private String server_rundir;
    @Value("${media.toloka.rfa.server.createStationCommand}")
    private String createStationCommand;
    @Value("${media.toloka.rfa.server.libretime.timezone}")
    private String libretime_timezone;
    @Value("${media.toloka.rfa.server.libretime.path}")
            private String libretime_path;
    @Value("${media.toloka.rfa.server.libretime.postgresql.host}")
    private String libretime_postgresql_host;
    @Value("${media.toloka.rfa.server.libretime.postgresql.port}")
    private String libretime_postgresql_port;

    @Value("${media.toloka.rfa.server.libretime.rabbitmq.host}")
    private String libretime_rabbitmq_host;
    @Value("${media.toloka.rfa.server.libretime.rabbitmq.port}")
    private String libretime_rabbitmq_port;
    @Value("${media.toloka.rfa.server.libretime.fromaddress}")
    private String libretime_from_address;
    @Value("${media.toloka.rfa.server.libretime.smtp.server}")
    private String libretime_smtp_server;
    @Value("${media.toloka.rfa.server.libretime.smtp.port}")
    private String libretime_smtp_port;
    @Value("${media.toloka.rfa.server.libretime.smtp.encryption}")
    private String libretime_smtp_encryption;
    @Value("${media.toloka.rfa.server.libretime.icecast.host}")
    private String libretime_icecast_host;
    @Value("${media.toloka.rfa.server.libretime.icecast.port}")
    private String libretime_icecast_port;
    @Value("${media.toloka.rfa.server.libretime.icecast.sourcepassword}")
    private String libretime_icecast_source_password;
    @Value("${media.toloka.rfa.server.libretime.icecast.adminpassword}")
    private String libretime_icecast_admin_password;

    @Value("${media.toloka.rfa.server.libretime.icecast.source.user}")
    private String libretime_icecast_sourceuser;
    @Value("${media.toloka.rfa.server.libretime.icecast.admin.user}")
    private String libretime_icecast_adminuser;

    @Value("${media.toloka.rfa.server.libretime.output.name}")
    private String libretime_output_name;
    @Value("${media.toloka.rfa.server.libretime.output.description}")
    private String libretime_output_description;
    @Value("${media.toloka.rfa.server.libretime.output.site}")
    private String libretime_output_site;
    @Value("${media.toloka.rfa.server.libretime.output.genre}")
    private String libretime_output_genre;
    @Value("${media.toloka.rfa.server.libretime.output.mobile}")
    private String libretime_output_mobile;






    @Autowired
            private StationService stationService;
    @Autowired
            private ClientService clientService;
    @Autowired
            private GsonService gsonService;



    Logger logger = LoggerFactory.getLogger(ServerRunnerService.class);

    public void AllocateStationOnServer(RPCJob rpcJob) {

        Gson gson = gsonService.CreateGson();
//        rpcJob.getRjobdata()
        Station station = gson.fromJson(rpcJob.getRjobdata(), Station.class);
        ProcessBuilder pb = new ProcessBuilder( createStationCommand);
        Map<String, String> env = pb.environment();
        // Призначаємо значення env
        env.put("CLIENT_DIR", clientdir);
//        env.remove("OTHERVAR");
        env.put("CLIENT_UUID", station.getClientdetail().getUuid());
        env.put("STATION_UUID", station.getUuid());
        env.put("STATION_WEB_PORT", station.getGuiport().toString());
        env.put("STATION_MASTER_PORT", station.getMain().toString());
        env.put("STATION_SHOW_PORT", station.getShow().toString());
        env.put("STATION_ID", station.getRadio_id().toString());
        env.put("LIBRETIME_VERSION", "4.0.0");
        env.put("RABBITMQ_DEFAULT_PASS", station.getUuid());
        env.put("RABBITMQ_QUEUE", station.getUuid());
        env.put("RABBITMQ_VROOT", station.getUuid());
        env.put("POSTGRES_PASSWORD", station.getUuid());

//        public_url
        env.put("PUBLIC_URL", station.getUuid());
        env.put("LIBRETIME_API_KEY", UUID.randomUUID().toString());
        env.put("LIBRETIME_SECRET_KEY", UUID.randomUUID().toString());
        env.put("LIBRETIME_TIMEZONE", libretime_timezone);
        env.put("LIBRETIME_PATH", libretime_path);
        env.put("LIBRETIME_POSTGRESQL_HOST", libretime_postgresql_host);
        env.put("LIBRETIME_POSTGRESQL_PORT", libretime_postgresql_port);
        env.put("LIBRETIME_POSTGRESQL_USERNAME", station.getDbname());
        env.put("LIBRETIME_POSTGRESQL_DBNAME", station.getDbname());
        env.put("LIBRETIME_POSTGRESQL_PASSWORD", station.getDbname());
        env.put("LIBRETIME_RABBITMQ_HOST", libretime_rabbitmq_host);
        env.put("LIBRETIME_RABBITMQ_PORT", libretime_rabbitmq_port);
        env.put("LIBRETIME_RABBITMQ_VHOST", "/" + station.getDbname());
        env.put("LIBRETIME_RABBITMQ_USERNAME", station.getDbname());
        env.put("LIBRETIME_RABBITMQ_PASSWORD", station.getDbname());
        env.put("LIBRETIME_FROM_ADDRESS", libretime_from_address);
        env.put("LIBRETIME_SMTP_SERVER", libretime_smtp_server);
        env.put("LIBRETIME_SMTP_PORT", libretime_smtp_port);
        env.put("LIBRETIME_SMTP_ENCRYPTION", libretime_smtp_encryption);
        env.put("LIBRETIME_ICECAST_HOST", libretime_icecast_host);
        env.put("LIBRETIME_ICECAST_PORT", libretime_icecast_port);
        env.put("LIBRETIME_ICECAST_SOURCE_USER", libretime_icecast_sourceuser);
        env.put("LIBRETIME_ICECAST_ADMIN_USER", libretime_icecast_adminuser);
        env.put("LIBRETIME_ICECAST_SOURCE_PASSWORD", libretime_icecast_source_password);
        env.put("LIBRETIME_ICECAST_ADMIN_PASSWORD", libretime_icecast_admin_password);
        // TODO При необхідності беремо з опису станції
        env.put("LIBRETIME_OUTPUT_NAME", libretime_output_name);
        env.put("LIBRETIME_OUTPUT_DESCRIPTION", libretime_output_description);
        env.put("LIBRETIME_OUTPUT_SITE", libretime_output_site);
        env.put("LIBRETIME_OUTPUT_GENRE", libretime_output_genre);
        env.put("LIBRETIME_OUTPUT_MOBILE", libretime_output_mobile);


        pb.directory(new File(server_rundir));
//        File log = new File("/home/ysv/log.txt");
        pb.redirectErrorStream(true);
//        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
        try {
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            // виводимо на консоль
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }
//            assert pb.redirectInput() == ProcessBuilder.Redirect.PIPE;
//            assert pb.redirectOutput().file() == log;
//            assert p.getInputStream().read() == -1;
        } catch (IOException e) {
            logger.warn(" Щось пішло не так при виконанні завдання в операційній системі");
            e.printStackTrace();
        }
    }
}

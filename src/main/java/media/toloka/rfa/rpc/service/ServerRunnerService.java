package media.toloka.rfa.rpc.service;

import media.toloka.rfa.rpc.model.RPCJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@Service
public class ServerRunnerService {

    Logger logger = LoggerFactory.getLogger(ServerRunnerService.class);

    public void AllocateStationOnServer(RPCJob rpcJob) {

        ProcessBuilder pb = new ProcessBuilder( "ssh", "rfa@s12"," ~/bin/createStation.sh");
        Map<String, String> env = pb.environment();
        // Призначаємо значення env
        env.put("VAR1", "myValue");
//        env.remove("OTHERVAR");
        env.put("VAR2", env.get("VAR1") + "suffix");
        pb.directory(new File("/home/ysv/"));
//        File log = new File("/home/ysv/log.txt");
        pb.redirectErrorStream(true);
//        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
        try {
            Process p = pb.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }
//            assert pb.redirectInput() == ProcessBuilder.Redirect.PIPE;
//            assert pb.redirectOutput().file() == log;
//            assert p.getInputStream().read() == -1;
        } catch (IOException e) {
            e.printStackTrace();
        }




    }



}

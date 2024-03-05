package media.toloka.rfa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig  implements WebSocketMessageBrokerConfigurer {




    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/topic");
//        sessionRepositoryInterceptor.setMatchingMessageTypes(EnumSet.of(SimpMessageType.CONNECT,
//                SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE,
//                SimpMessageType.UNSUBSCRIBE, SimpMessageType.HEARTBEAT));
//
//        config.setApplicationDestinationPrefixes(...);
//        config.enableSimpleBroker(...)
//             .setTaskScheduler(new DefaultManagedTaskScheduler()).setHeartbeatValue(new long[]{0,20000});

        config.enableSimpleBroker(
                "/user",
                "/topic",
                "/queue",
                "/hello",
                "/updatepublic",
                "/updateprivat",
                "/roomslist",
                "/userslist",
                "/private",
                "/public",
                "/heartbeats");
//                .setTaskScheduler(new DefaultManagedTaskScheduler())
//                .setHeartbeatValue(new long[]{0,8000});
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/rfa");
        registry.addEndpoint("/rfachat").setAllowedOrigins("https://rfa.toloka.media","https://localhost").withSockJS();
    }

//    @Bean
//    public TaskScheduler heartBeatScheduler() {
//        return new ThreadPoolTaskScheduler();
//    }
}

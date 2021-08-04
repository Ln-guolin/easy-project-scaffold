package @packageName@;

import group.bigplayers.common.converter.annotations.EnableBigplayerConverter;
import group.bigplayers.upms.security.annotation.EnableBigplayerFeignClients;
import group.bigplayers.upms.security.annotation.EnableBigplayerResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringCloudApplication
@EnableBigplayerConverter
@EnableBigplayerResourceServer
@ComponentScan(value = {"group.bigplayers.gameserv", "group.bigplayer.gameserv", "group.bigplayers.common.core"})
@EnableBigplayerFeignClients(basePackages = {"group.bigplayer.afrbtc", "com.codingapi.tx", "group.bigplayer.gameserv", "group.bigplayers.gameserv"})
public class @artifact4humpCase@AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(@artifact4humpCase@AdminApplication.class, args);
    }
}

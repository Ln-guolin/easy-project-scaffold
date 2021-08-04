package @packageName@;

import group.bigplayer.gameserv.common.security.annotation.EnableBigplayerFeignClients;
import group.bigplayer.gameserv.common.security.annotation.EnableBigplayerResourceServer;
import group.bigplayers.common.converter.annotations.EnableBigplayerFrontConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringCloudApplication
@EnableBigplayerFrontConverter
@EnableBigplayerResourceServer
@ComponentScan(value = {"group.bigplayers.gameserv", "group.bigplayer.gameserv"})
@EnableBigplayerFeignClients(basePackages = {"group.bigplayers.gameserv", "com.codingapi.tx", "group.bigplayer.gameserv", "group.bigplayer.afrbtc"})
public class @artifact4humpCase@Application {
    public static void main(String[] args) {
        SpringApplication.run(@artifact4humpCase@Application.class, args);
    }
}

package @packageName@;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

@Slf4j
@SpringCloudApplication
@ComponentScan(value = {"group.bigplayers.fmis"})
public class Fmis@artifact4humpCase@Application {

    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Fmis@artifact4humpCase@Application.class, args);
        Environment env = applicationContext.getEnvironment();

        log.info(
                "\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "External: \thttp://{}:{}\n\t" +
                        "Doc: \thttp://{}:{}/doc.html\n" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"), InetAddress
                        .getLocalHost()
                        .getHostAddress(), env.getProperty("server.port"), InetAddress
                        .getLocalHost()
                        .getHostAddress(), env.getProperty("server.port")
        );
    }
}

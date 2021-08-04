package @packageName@.client.config;

import @packageName@.client.feign.Fmis@artifact4humpCase@FeignClient;
import @packageName@.client.feign.fallback.Fmis@artifact4humpCase@FeignClientFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = {Fmis@artifact4humpCase@FeignClient.class})
@ComponentScan(basePackageClasses = {Fmis@artifact4humpCase@FeignClientFactory.class})
public class Fmis@artifact4humpCase@FeignClientAutoConfiguration {
}

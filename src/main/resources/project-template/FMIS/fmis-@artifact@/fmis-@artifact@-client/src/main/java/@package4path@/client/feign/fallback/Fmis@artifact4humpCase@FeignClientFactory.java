package @packageName@.client.feign.fallback;

import feign.hystrix.FallbackFactory;
import @packageName@.client.feign.Fmis@artifact4humpCase@FeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Fmis@artifact4humpCase@FeignClientFactory implements FallbackFactory<Fmis@artifact4humpCase@FeignClient> {
    @Override
    public Fmis@artifact4humpCase@FeignClient create(Throwable throwable) {
        return new Fmis@artifact4humpCase@FeignClient(){

        };
    }
}

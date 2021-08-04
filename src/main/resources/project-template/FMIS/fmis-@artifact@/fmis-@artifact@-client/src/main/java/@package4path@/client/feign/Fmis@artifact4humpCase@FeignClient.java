package @packageName@.client.feign;

import @packageName@.client.feign.fallback.Fmis@artifact4humpCase@FeignClientFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "fmis-{artifact}-application", fallbackFactory = Fmis@artifact4humpCase@FeignClientFactory.class)
public interface Fmis@artifact4humpCase@FeignClient {


}

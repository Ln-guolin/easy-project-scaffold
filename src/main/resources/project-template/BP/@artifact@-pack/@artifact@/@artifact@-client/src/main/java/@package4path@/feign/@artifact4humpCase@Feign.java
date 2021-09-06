package @packageName@.feign;

import group.bigplayers.common.core.constant.SecurityConstants;
import group.bigplayers.common.core.res.FeignResult;
import @packageName@.feign.fallback.@artifact4humpCase@FeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "{artifact}-application", fallbackFactory = @artifact4humpCase@FeignFallbackFactory.class)
public interface @artifact4humpCase@Feign {
    @PostMapping("/inner/{artifact4path}/queryTest")
    FeignResult queryTest(@RequestHeader(SecurityConstants.FROM) String from);
}

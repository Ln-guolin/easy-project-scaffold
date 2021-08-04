package @packageName@.feign;

import group.bigplayers.common.core.constant.SecurityConstants;
import group.bigplayers.common.core.res.FeignResult;
import @packageName@.feign.fallback.@artifact4humpCase@AdminFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "{artifact}-admin-application", fallbackFactory = @artifact4humpCase@AdminFallbackFactory.class)
public interface @artifact4humpCase@AdminFeign {
    @PostMapping("/inner/admin/{artifact}/queryTest")
    FeignResult queryTest(@RequestHeader(SecurityConstants.FROM) String from);
}

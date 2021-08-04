package @packageName@.feign.fallback;

import feign.hystrix.FallbackFactory;
import group.bigplayers.common.core.res.FeignResult;
import @packageName@.feign.@artifact4humpCase@AdminFeign;
import org.springframework.stereotype.Component;
@Component
public class @artifact4humpCase@AdminFallbackFactory implements FallbackFactory<@artifact4humpCase@AdminFeign> {
    @Override
    public @artifact4humpCase@AdminFeign create(Throwable throwable) {
        return new @artifact4humpCase@AdminFeign() {
            @Override
            public FeignResult queryTest(String from) {
                throw new RuntimeException("进入熔断，test error",throwable);
            }
        };
    }
}

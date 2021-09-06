package @packageName@.feign.fallback;

import feign.hystrix.FallbackFactory;
import group.bigplayers.common.core.res.FeignResult;
import @packageName@.feign.@artifact4humpCase@Feign;
import org.springframework.stereotype.Component;

@Component
public class @artifact4humpCase@FeignFallbackFactory implements FallbackFactory<@artifact4humpCase@Feign> {
    @Override
    public @artifact4humpCase@Feign create(Throwable throwable) {
        return new @artifact4humpCase@Feign(){
            @Override
            public FeignResult queryTest(String from) {
                throw new RuntimeException("进入熔断，test error",throwable);
            }
        };
    }
}

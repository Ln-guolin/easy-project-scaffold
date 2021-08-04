package @packageName@.feign.fallback;

import feign.hystrix.FallbackFactory;
import group.bigplayers.common.core.res.FeignResult;
import @packageName@.feign.@domainUpperCase@Feign;
import org.springframework.stereotype.Component;

@Component
public class @domainUpperCase@FeignFallbackFactory implements FallbackFactory<@domainUpperCase@Feign> {
    @Override
    public @domainUpperCase@Feign create(Throwable throwable) {
        return new @domainUpperCase@Feign(){
            @Override
            public FeignResult queryTest(String from) {
                throw new RuntimeException("进入熔断，test error",throwable);
            }
        };
    }
}

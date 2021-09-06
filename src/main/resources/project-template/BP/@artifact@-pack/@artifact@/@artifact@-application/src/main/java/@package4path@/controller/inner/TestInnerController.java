package @packageName@.controller.inner;

import groovy.util.logging.Slf4j;
import group.bigplayers.common.core.res.FeignResult;
import @packageName@.service.TestService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/inner/@artifact4path@/")
public class TestInnerController {
    @Autowired
    private TestService testService;

    @ApiOperation("query test")
    @PostMapping("queryTest")
    public FeignResult queryTest() {
        return FeignResult.success(testService.queryTest());
    }

}

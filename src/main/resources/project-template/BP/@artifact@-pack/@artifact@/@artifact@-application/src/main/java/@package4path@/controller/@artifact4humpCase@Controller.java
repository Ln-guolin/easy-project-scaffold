package @packageName@.controller;

import group.bigplayers.common.core.res.FeignResult;
import @packageName@.service.TestService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/@artifact4path@/")
public class @artifact4humpCase@Controller {
    @Autowired
    private TestService testService;

    @ApiOperation("query test")
    @GetMapping("queryTest")
    public FeignResult queryTest() {
        return FeignResult.success(testService.queryTest());
    }
}

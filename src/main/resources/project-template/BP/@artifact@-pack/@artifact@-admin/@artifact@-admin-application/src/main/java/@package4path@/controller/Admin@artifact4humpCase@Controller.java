package @packageName@.controller;

import group.bigplayers.common.core.res.FeignResult;
import @packageName@.service.TestService;
import group.bigplayers.upms.security.annotation.Inner;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/@artifact4path@/admin/")
public class Admin@artifact4humpCase@Controller {
    @Autowired
    private TestService testService;

    @ApiOperation("query test")
    @PostMapping("queryTest")
    public FeignResult queryTest() {
        return FeignResult.success(testService.queryTest());
    }
}

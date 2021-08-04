package @packageName@.service.impl;

import @packageName@.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public String queryTest() {
        return "query success";
    }
}

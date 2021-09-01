package cn.ex.project.scaffold.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口异常
 *
 * @author: Chen GuoLin
 * @create: 2020-02-02 15:20
 **/
@Data
@NoArgsConstructor
public class ApiException extends RuntimeException {

    private String msg;

    public ApiException(String msg){
        this.msg = msg;
    }
}

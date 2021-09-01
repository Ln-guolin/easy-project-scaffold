package cn.ex.project.scaffold.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回类
 *
 * @author: Chen GuoLin
 * @create: 2020-01-29 15:28
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {
    private int code;
    private String msg;
    private T data;

    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> R<T> success() {
        return new R<>(200,"success");
    }

    public static <T> R<T> success(T data) {
        return new R<>(200,"success",data);
    }

    public static <T> R<T> error(T data) {
        return new R<>(-1,"error",data);
    }
}

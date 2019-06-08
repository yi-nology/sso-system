package com.murphyyi.sso.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: Result
 * @description:
 * @author: zhangyi
 * @since: 2019-02-28 19:46
 */
@NoArgsConstructor
@Data
public class Result<T> {
    private T data = null;
    private String massage;
    private Integer status = 500;

    private Result(T data, Integer status, String massage) {
        this.data = data;
        this.status = status;
        this.massage = massage;
    }

    public static <T> Result<T> success(T data, String massage) {
        return new Result<T>(data, 200, massage);
    }

    public static <T> Result<T> fail(String massage) {
        return new Result<T>(null, 500, massage);
    }

    public static <T> Result<T> fail(T data, String massage) {
        return new Result<T>(data, 500, massage);
    }
}

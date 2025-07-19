package com.lxy.ai.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result {
    private Integer ok;
    private String msg;

    public Result(Integer ok, String msg) {
        this.ok = ok;
        this.msg = msg;
    }
    public static Result success(String msg) {
        return new Result(1, msg);
    }
    public static Result fail(String msg) {
        return new Result(0, msg);
    }
}

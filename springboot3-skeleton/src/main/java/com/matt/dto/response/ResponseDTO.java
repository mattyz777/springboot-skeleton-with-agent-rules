package com.matt.dto.response;

import com.matt.constant.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO<T> {
    private String code;
    private String message;
    private T data;

    public static <T> ResponseDTO<Void> success() {
        return success(null);
    }

    public static  <T> ResponseDTO<T> success(T data) {
        return new ResponseDTO<>(Constant.RESPONSE_SUCCESS, "", data);
    }

    public static ResponseDTO<Void> error() {
        return error(Constant.RESPONSE_ERROR, null);
    }

    public static ResponseDTO<Void> error(String message) {
        return new ResponseDTO<>(Constant.RESPONSE_ERROR, message, null);
    }

    public static ResponseDTO<Void> error(String code, String message) {
        return new ResponseDTO<>(code, message, null);
    }
}

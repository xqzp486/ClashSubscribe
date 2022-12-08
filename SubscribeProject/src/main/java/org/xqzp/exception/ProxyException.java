package org.xqzp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProxyException extends RuntimeException{
    private Integer code;
    private String msg;
}

package com.mars.fw.web.context;

import lombok.Data;

/**
 * @Author King
 * @create 2020/4/21 17:28
 */
@Data
public class GlobalEntry {

    private String token;

    private Long userId;

    private String userCode;

    private Object object;
}

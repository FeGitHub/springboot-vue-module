package com.test.demo.mapStruct;

import lombok.Data;

@Data
public class PersonTestDto {
    String describe;

    private Long id;

    private String personName;

    private String age;

    private String source;

    private String height;

    private String createTime;
}

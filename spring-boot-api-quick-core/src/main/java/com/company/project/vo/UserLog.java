package com.company.project.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserLog {
    private String username;
    private String userid;
    private String state;
}
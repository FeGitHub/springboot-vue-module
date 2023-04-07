package com.company.project.master.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoreWriteSheetVo {


    public MoreWriteSheetVo(Object list, Object otherData) {
        this.list = list;
        this.otherData = otherData;
    }

    private Object list;

    private Object otherData;

}

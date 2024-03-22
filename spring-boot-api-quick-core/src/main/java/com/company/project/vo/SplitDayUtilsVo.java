package com.company.project.vo;

import com.company.project.utils.SplitDayUtils;

import java.util.List;

public class SplitDayUtilsVo {
    public List<SplitDayUtils.ApplyOrChangeRangeDate> getApplyRangeDateList() {
        return applyRangeDateList;
    }

    public void setApplyRangeDateList(List<SplitDayUtils.ApplyOrChangeRangeDate> applyRangeDateList) {
        this.applyRangeDateList = applyRangeDateList;
    }

    public List<SplitDayUtils.ApplyOrChangeRangeDate> getChangeRangeDateList() {
        return changeRangeDateList;
    }

    public void setChangeRangeDateList(List<SplitDayUtils.ApplyOrChangeRangeDate> changeRangeDateList) {
        this.changeRangeDateList = changeRangeDateList;
    }

    public List<SplitDayUtils.MarkRangeDate> getMarkRangeDateList() {
        return markRangeDateList;
    }

    public void setMarkRangeDateList(List<SplitDayUtils.MarkRangeDate> markRangeDateList) {
        this.markRangeDateList = markRangeDateList;
    }

    private List<SplitDayUtils.ApplyOrChangeRangeDate> applyRangeDateList;

    private List<SplitDayUtils.ApplyOrChangeRangeDate> changeRangeDateList;

    private List<SplitDayUtils.MarkRangeDate> markRangeDateList;
}

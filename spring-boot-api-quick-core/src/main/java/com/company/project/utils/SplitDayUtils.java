package com.company.project.utils;

import com.alibaba.excel.util.CollectionUtils;
import com.company.project.vo.SplitDayUtilsVo;

import java.time.LocalDate;
import java.util.*;

/***
 * 日期切割服務
 */
public class SplitDayUtils {

    public static void main(String[] args) {

    }


    public static List<String> run(SplitDayUtilsVo vo) {
        List<String> result = new ArrayList<>();
        // 申請年假
        List<ApplyOrChangeRangeDate> applyRangeDateList = vo.getApplyRangeDateList();
        //更改年假
        applyRangeDateList.addAll(vo.getChangeRangeDateList());
        // 年假登記
        List<MarkRangeDate> markRangeDateList = vo.getMarkRangeDateList();
        Map<String, MarkRangeDate> markRangeDateListMap = getMapMarkRangeDate(markRangeDateList);
        //数据转换
        List<ApplyDate> appDateList = applyRangeListToApplyDateList(applyRangeDateList);
        List<MarkDate> markDateList = markRangeListToMarkDateList(markRangeDateList);
        //通过日期找到对应的登记日
        Map<String, MarkDate> mapMarkDateList = markDateToMap(markDateList);
        //找到同一段的登记日
        Map<String, List<MarkDate>> mapMarkDateListMap = markDateListToMap(markDateList);
        //数据处理
        if (!CollectionUtils.isEmpty(appDateList)) {
            for (ApplyDate applyDate : appDateList) {
                MarkDate markDate = mapMarkDateList.get(applyDate.getApplyDate());
                if (markDate != null) {
                    markDate.setConsumeStatus(Boolean.TRUE);
                }
            }
        }
        //数据分析
        Iterator<Map.Entry<String, List<MarkDate>>> iter = mapMarkDateListMap.entrySet().iterator();
        while (iter.hasNext()) {
            int consumeStatusTrue = 0;
            Map.Entry<String, List<MarkDate>> entry = iter.next();
            List<MarkDate> tempDetailList = entry.getValue();
            String key = entry.getKey();
            if (!CollectionUtils.isEmpty(tempDetailList)) {
                for (MarkDate markDate : tempDetailList) {
                    if (Boolean.TRUE.equals(markDate.getConsumeStatus())) {
                        consumeStatusTrue++;
                    }
                }
                if (consumeStatusTrue == tempDetailList.size()) {
                    MarkRangeDate markRangeDate = markRangeDateListMap.get(key);
                    String startDate = DateUtils.formatLocalDate(markRangeDate.getStartDate(), DateUtils.yyyy_MM_dd);
                    String endtDate = DateUtils.formatLocalDate(markRangeDate.getEndDate(), DateUtils.yyyy_MM_dd);
                    String msg = String.format("%s到%s年假登记已经全部消耗，直接修改此年假登记为已消耗", startDate, endtDate);
                    System.out.println(msg);
                    result.add(msg);
                } else {//要分割的情况
                    //按照日期从大到小
                    Comparator<MarkDate> byCreateArchiveDateDesc = Comparator.comparing(MarkDate::getMarkDate);
                    tempDetailList.sort(byCreateArchiveDateDesc);
                    MarkDate nowMaxEndMarkDate = tempDetailList.get(0);
                    MarkDate firstMarkDate = tempDetailList.get(0);
                    for (MarkDate markDate : tempDetailList) {
                        if (firstMarkDate.getMarkDate().isEqual(markDate.getMarkDate()) || markDate.getMarkDate().isAfter(nowMaxEndMarkDate.getMarkDate())) {
                            MarkDate endMarkDate = getEndMarkDate(markDate, tempDetailList);
                            nowMaxEndMarkDate = endMarkDate;
                            String startDate = DateUtils.formatLocalDate(markDate.getMarkDate(), DateUtils.yyyy_MM_dd);
                            String endtDate = DateUtils.formatLocalDate(endMarkDate.getMarkDate(), DateUtils.yyyy_MM_dd);
                            String msg = String.format("%s到%s为一段年假登记，消耗状态为" + (endMarkDate.getConsumeStatus() ? "已消耗" : "未消耗"), startDate, endtDate);
                            System.out.println(msg);
                            result.add(msg);
                        }
                    }
                }
            }
        }
        return result;
    }


    // **********************下面是工具方法*************************

    public static Map<String, MarkRangeDate> getMapMarkRangeDate(List<MarkRangeDate> markRangeDateList) {
        Map<String, MarkRangeDate> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(markRangeDateList)) {
            for (MarkRangeDate markRangeDate : markRangeDateList) {
                map.put(markRangeDate.getBizId(), markRangeDate);
            }
        }
        return map;
    }


    public static MarkDate getEndMarkDate(MarkDate startMarkDate, List<MarkDate> tempDetailList) {
        Boolean consumeStatus = startMarkDate.getConsumeStatus();
        MarkDate result = startMarkDate;
        for (MarkDate tempMarkDate : tempDetailList) {
            if (tempMarkDate.getMarkDate().isAfter(startMarkDate.getMarkDate())) {
                if (consumeStatus.equals(tempMarkDate.getConsumeStatus())) {
                    result = tempMarkDate;
                } else {
                    break;
                }
            }
        }
        return result;
    }


    public static Map<String, List<MarkDate>> markDateListToMap(List<MarkDate> markDateList) {
        Map<String, List<MarkDate>> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(markDateList)) {
            for (MarkDate markDate : markDateList) {
                List<MarkDate> tempList = map.get(markDate.getMarkRangeDateId());
                if (tempList == null) {
                    tempList = new ArrayList<>();
                }
                tempList.add(markDate);
                map.put(markDate.getMarkRangeDateId(), tempList);
            }
        }
        return map;
    }


    /***
     * 将登记范围转为登记日
     * @param applyRangeDateList
     * @return
     */
    public static List<ApplyDate> applyRangeListToApplyDateList(List<ApplyOrChangeRangeDate> applyRangeDateList) {
        List<ApplyDate> applyDateList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(applyRangeDateList)) {
            for (ApplyOrChangeRangeDate applyDate : applyRangeDateList) {
                applyDateList.addAll(applyRangeToApplyDateList(applyDate));
            }
        }
        return applyDateList;
    }

    public static List<ApplyDate> applyRangeToApplyDateList(ApplyOrChangeRangeDate rangeDate) {
        List<ApplyDate> applyDateList = new ArrayList<>();
        LocalDate startMove = rangeDate.getStartDate();
        while (true) {
            if (startMove.isAfter(rangeDate.getEndDate())) {
                break;
            }
            ApplyDate applyDate = new ApplyDate();
            applyDate.setApplyDate(DateUtils.formatLocalDate(startMove, DateUtils.yyyy_MM_dd));
            applyDateList.add(applyDate);
            startMove = startMove.plusDays(1);
        }
        return applyDateList;
    }


    public static Map<String, MarkDate> markDateToMap(List<MarkDate> markDateList) {
        Map<String, MarkDate> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(markDateList)) {
            for (MarkDate markDate : markDateList) {
                String key = DateUtils.formatLocalDate(markDate.getMarkDate(), DateUtils.yyyy_MM_dd);
                map.put(key, markDate);
            }
        }
        return map;
    }


    /***
     * 将登记范围转为登记日
     * @param markRangeDateList
     * @return
     */
    public static List<MarkDate> markRangeListToMarkDateList(List<MarkRangeDate> markRangeDateList) {
        List<MarkDate> markDateList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(markRangeDateList)) {
            for (MarkRangeDate rangeDate : markRangeDateList) {
                markDateList.addAll(markRangeToMarkDateList(rangeDate));
            }
        }
        return markDateList;
    }

    public static List<MarkDate> markRangeToMarkDateList(MarkRangeDate rangeDate) {
        List<MarkDate> markDateList = new ArrayList<>();
        LocalDate startMove = rangeDate.getStartDate();
        while (true) {
            if (startMove.isAfter(rangeDate.getEndDate())) {
                break;
            }
            MarkDate markDate = new MarkDate();
            markDate.setMarkRangeDateId(rangeDate.getBizId());
            markDate.setMarkDate(startMove);
            markDateList.add(markDate);
            startMove = startMove.plusDays(1);
        }
        return markDateList;
    }


    /***
     * 申請日期（按日）
     */
    public static class ApplyDate {
        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

        private String applyDate;

    }


    /***
     * 登記日期（按日）
     */
    public static class MarkDate {
        public LocalDate getMarkDate() {
            return markDate;
        }

        public void setMarkDate(LocalDate markDate) {
            this.markDate = markDate;
        }

        public Boolean getConsumeStatus() {
            return consumeStatus;
        }

        public void setConsumeStatus(Boolean consumeStatus) {
            this.consumeStatus = consumeStatus;
        }

        public String getMarkRangeDateId() {
            return markRangeDateId;
        }

        public void setMarkRangeDateId(String markRangeDateId) {
            this.markRangeDateId = markRangeDateId;
        }

        private LocalDate markDate;

        private Boolean consumeStatus = false;

        private String markRangeDateId;
    }


    /***
     * 登記日期（按範圍）
     */
    public static class MarkRangeDate {


        private String bizId;


        MarkRangeDate(LocalDate startDate, LocalDate endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        private LocalDate startDate;
        private LocalDate endDate;

        public String getBizId() {
            return bizId;
        }

        public void setBizId(String bizId) {
            this.bizId = bizId;
        }
    }

    /***
     * 申請或更改日期（按範圍）
     */
    public static class ApplyOrChangeRangeDate {


        ApplyOrChangeRangeDate(LocalDate startDate, LocalDate endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        private LocalDate startDate;
        private LocalDate endDate;
    }


    public static class ChangeRangeDate {

        ChangeRangeDate(LocalDate startDate, LocalDate endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        private LocalDate startDate;
        private LocalDate endDate;
    }

}

package com.test;

import com.alibaba.excel.util.CollectionUtils;
import com.company.project.utils.DateUtils;
import com.company.project.utils.StringConvertUtil;

import java.time.LocalDate;
import java.util.*;

public class SplitDayTest {

    public static void main(String[] args) {
        testMix();
    }


    /* ①申請年假：2023-08-04~2023-08-04
      ②年假登記：2023-08-04~2023-08-04
      ③無需拆分，直接更新年假登記“消耗狀態”為“1已消耗”；
    */
    public static void test1() {
        // 申請年假
        List<ApplyRangeDate> applyRangeDateList = new ArrayList<>();
        ApplyRangeDate applyRangeDate = new ApplyRangeDate(StringConvertUtil.convertLocalDate("2023-08-03", null), StringConvertUtil.convertLocalDate("2023-08-03", null));
        applyRangeDateList.add(applyRangeDate);
        // 年假登記
        List<MarkRangeDate> markRangeDateList = new ArrayList<>();
        MarkRangeDate markRangeDateA = new MarkRangeDate(StringConvertUtil.convertLocalDate("2023-08-03", null), StringConvertUtil.convertLocalDate("2023-08-03", null));
        markRangeDateA.setBizId("markRangeDateA");
        markRangeDateList.add(markRangeDateA);
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
            if (!CollectionUtils.isEmpty(tempDetailList)) {
                for (MarkDate markDate : tempDetailList) {
                    if (Boolean.TRUE.equals(markDate.getConsumeStatus())) {
                        consumeStatusTrue++;
                    }
                }
                if (consumeStatusTrue == tempDetailList.size()) {
                    System.out.println(entry.getKey() + "==>该年假登记已经全部消耗，直接修改此年假登记为已消耗");
                }
            }
        }
    }


    /* 申請年假“開始日期”與年假登記“開始日期”相等：
            ①申請年假：2023-08-03~2023-08-03
            ②年假登記：2023-08-03~2023-08-05
            ③拆分年假登記，
    A . 2023-08-03~2023-08-03已消耗
    B . 2023-08-04~2023-08-05可用 */
    public static void test2() {
        // 申請年假
        List<ApplyRangeDate> applyRangeDateList = new ArrayList<>();
        ApplyRangeDate applyRangeDate = new ApplyRangeDate(StringConvertUtil.convertLocalDate("2023-08-03", null), StringConvertUtil.convertLocalDate("2023-08-03", null));
        applyRangeDateList.add(applyRangeDate);
        // 年假登記
        List<MarkRangeDate> markRangeDateList = new ArrayList<>();
        MarkRangeDate markRangeDateA = new MarkRangeDate(StringConvertUtil.convertLocalDate("2023-08-03", null), StringConvertUtil.convertLocalDate("2023-08-06", null));
        markRangeDateA.setBizId("markRangeDateA");
        markRangeDateList.add(markRangeDateA);
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
            if (!CollectionUtils.isEmpty(tempDetailList)) {
                for (MarkDate markDate : tempDetailList) {
                    if (Boolean.TRUE.equals(markDate.getConsumeStatus())) {
                        consumeStatusTrue++;
                    }
                }
                if (consumeStatusTrue == tempDetailList.size()) {
                    System.out.println(entry.getKey() + "==>该年假登记已经全部消耗，直接修改此年假登记为已消耗");
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
                            System.out.println(String.format("%s到%s为一段记录，消耗状态为" + endMarkDate.getConsumeStatus(), startDate, endtDate));
                        }
                    }
                }
            }
        }
    }

    /*
（3）申請年假“結束日期”與年假登記“結束日期”相等：
            ① 申請年假：2023-08-03~2023-08-03
            ② 年假登記：2023-07-31~2023-08-03
            ③ 拆分年假登記，
    A. 2023-07-31~2023-08-02 可用
    B. 2023-08-03~2023-08-03 已消耗
  */
    public static void test3() {
        // 申請年假
        List<ApplyRangeDate> applyRangeDateList = new ArrayList<>();
        ApplyRangeDate applyRangeDate = new ApplyRangeDate(StringConvertUtil.convertLocalDate("2023-08-03", null), StringConvertUtil.convertLocalDate("2023-08-03", null));
        applyRangeDateList.add(applyRangeDate);
        // 年假登記
        List<MarkRangeDate> markRangeDateList = new ArrayList<>();
        MarkRangeDate markRangeDateA = new MarkRangeDate(StringConvertUtil.convertLocalDate("2023-07-31", null), StringConvertUtil.convertLocalDate("2023-08-03", null));
        markRangeDateA.setBizId("markRangeDateA");
        markRangeDateList.add(markRangeDateA);
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
            if (!CollectionUtils.isEmpty(tempDetailList)) {
                for (MarkDate markDate : tempDetailList) {
                    if (Boolean.TRUE.equals(markDate.getConsumeStatus())) {
                        consumeStatusTrue++;
                    }
                }
                if (consumeStatusTrue == tempDetailList.size()) {
                    System.out.println(entry.getKey() + "==>该年假登记已经全部消耗，直接修改此年假登记为已消耗");
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
                            System.out.println(String.format("%s到%s为一段记录，消耗状态为" + endMarkDate.getConsumeStatus(), startDate, endtDate));
                        }
                    }
                }
            }
        }
    }


    public static void test4() {
        // 申請年假
        List<ApplyRangeDate> applyRangeDateList = new ArrayList<>();
        ApplyRangeDate applyRangeDate = new ApplyRangeDate(StringConvertUtil.convertLocalDate("2023-08-03", null), StringConvertUtil.convertLocalDate("2023-08-03", null));
        applyRangeDateList.add(applyRangeDate);
        // 年假登記
        List<MarkRangeDate> markRangeDateList = new ArrayList<>();
        MarkRangeDate markRangeDateA = new MarkRangeDate(StringConvertUtil.convertLocalDate("2023-07-31", null), StringConvertUtil.convertLocalDate("2023-08-05", null));
        markRangeDateA.setBizId("markRangeDateA");
        markRangeDateList.add(markRangeDateA);
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
            if (!CollectionUtils.isEmpty(tempDetailList)) {
                for (MarkDate markDate : tempDetailList) {
                    if (Boolean.TRUE.equals(markDate.getConsumeStatus())) {
                        consumeStatusTrue++;
                    }
                }
                if (consumeStatusTrue == tempDetailList.size()) {
                    System.out.println(entry.getKey() + "==>该年假登记已经全部消耗，直接修改此年假登记为已消耗");
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
                            System.out.println(String.format("%s到%s为一段记录，消耗状态为" + endMarkDate.getConsumeStatus(), startDate, endtDate));
                        }
                    }
                }
            }
        }
    }

  /*（5）申請年假“開始日期、結束日期”部分時段與1段年假登記“開始日期、結束日期”重疊：
            ① 申請年假：2023-11-17~2023-11-20（符合年假登記表+更改年假）
            ② 年假登記：2023-11-09~2023-11-17
            ③ 拆分年假登記，
    A. 2023-11-09~2023-11-16 可用
    B. 2023-11-17~2023-11-17 已消耗 */

    public static void test5() {
        // 申請年假
        List<ApplyRangeDate> applyRangeDateList = new ArrayList<>();
        ApplyRangeDate applyRangeDate = new ApplyRangeDate(StringConvertUtil.convertLocalDate("2023-11-17", null), StringConvertUtil.convertLocalDate("2023-11-20", null));
        applyRangeDateList.add(applyRangeDate);
        // 年假登記
        List<MarkRangeDate> markRangeDateList = new ArrayList<>();
        MarkRangeDate markRangeDateA = new MarkRangeDate(StringConvertUtil.convertLocalDate("2023-11-09", null), StringConvertUtil.convertLocalDate("2023-11-17", null));
        markRangeDateA.setBizId("markRangeDateA");
        markRangeDateList.add(markRangeDateA);
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
            if (!CollectionUtils.isEmpty(tempDetailList)) {
                for (MarkDate markDate : tempDetailList) {
                    if (Boolean.TRUE.equals(markDate.getConsumeStatus())) {
                        consumeStatusTrue++;
                    }
                }
                if (consumeStatusTrue == tempDetailList.size()) {
                    System.out.println(entry.getKey() + "==>该年假登记已经全部消耗，直接修改此年假登记为已消耗");
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
                            System.out.println(String.format("%s到%s为一段记录，消耗状态为" + endMarkDate.getConsumeStatus(), startDate, endtDate));
                        }
                    }
                }
            }
        }
    }

    //申请年假  2023-11-17到 2023-11-18
    //更改年假  2023-11-19到 2023-11-19
    //年假登记  2023-11-18到2023-11-19
    public static void testMix() {
        // 申請年假
        List<ApplyRangeDate> applyRangeDateList = new ArrayList<>();
        ApplyRangeDate applyRangeDate = new ApplyRangeDate(StringConvertUtil.convertLocalDate("2023-11-17", null), StringConvertUtil.convertLocalDate("2023-11-18", null));
        applyRangeDateList.add(applyRangeDate);
        //更改年假
        //  List<ChangeRangeDate> changeRangeDateList = new ArrayList<>();
        //  ChangeRangeDate changeRangeDate = new ChangeRangeDate(StringConvertUtil.convertLocalDate("2023-11-19", null), StringConvertUtil.convertLocalDate("2023-11-19", null));
        //  changeRangeDateList.add(changeRangeDate);
        ApplyRangeDate changeRangeDateList = new ApplyRangeDate(StringConvertUtil.convertLocalDate("2023-11-19", null), StringConvertUtil.convertLocalDate("2023-11-19", null));
        applyRangeDateList.add(changeRangeDateList);
        // 年假登記
        List<MarkRangeDate> markRangeDateList = new ArrayList<>();
        MarkRangeDate markRangeDateA = new MarkRangeDate(StringConvertUtil.convertLocalDate("2023-11-18", null), StringConvertUtil.convertLocalDate("2023-11-19", null));
        markRangeDateA.setBizId("markRangeDateA");
        markRangeDateList.add(markRangeDateA);
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
            if (!CollectionUtils.isEmpty(tempDetailList)) {
                for (MarkDate markDate : tempDetailList) {
                    if (Boolean.TRUE.equals(markDate.getConsumeStatus())) {
                        consumeStatusTrue++;
                    }
                }
                if (consumeStatusTrue == tempDetailList.size()) {
                    System.out.println(entry.getKey() + "==>该年假登记已经全部消耗，直接修改此年假登记为已消耗");
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
                            System.out.println(String.format("%s到%s为一段记录，消耗状态为" + endMarkDate.getConsumeStatus(), startDate, endtDate));
                        }
                    }
                }
            }
        }
    }


    // **********************下面是工具方法*************************

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
    public static List<ApplyDate> applyRangeListToApplyDateList(List<ApplyRangeDate> applyRangeDateList) {
        List<ApplyDate> applyDateList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(applyRangeDateList)) {
            for (ApplyRangeDate applyDate : applyRangeDateList) {
                applyDateList.addAll(applyRangeToApplyDateList(applyDate));
            }
        }
        return applyDateList;
    }

    public static List<ApplyDate> applyRangeToApplyDateList(ApplyRangeDate rangeDate) {
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


    public static class ApplyDate {
        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

        private String applyDate;

    }


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

    public static class ApplyRangeDate {

        public String getBizType() {
            return bizType;
        }

        public void setBizType(String bizType) {
            this.bizType = bizType;
        }

        private String bizType;


        ApplyRangeDate(LocalDate startDate, LocalDate endDate) {
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

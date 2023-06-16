package com.company.project.service.easyExcel;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.*;


@Slf4j
public class EasyExcelMergeStrategy<T> extends AbstractMergeStrategy {


    // 主键值集合
    private List<String> primaryIdList = new ArrayList<>();
    // 需要合并的列index
    Set<Integer> colIndexSet = new HashSet<>();

    Map<Integer, List<String>> mapColumnIndex = new HashMap<>();

    private Integer excelKeyColumnIndex;

    /**
     * 从数据里获取合并的信息
     */
    public EasyExcelMergeStrategy(List<T> data) throws IllegalAccessException {
        if (data.size() == 0) {
            throw new RuntimeException("no data exception");
        }
        for (T row : data) {
            Class<?> type = row.getClass();
            Field[] fields = getAllDeclaredFields(type);
            Integer i = 0;
            for (Field field : fields) {
                ExcelKey key = field.getDeclaredAnnotation(ExcelKey.class);
                if (null != key) {
                    excelKeyColumnIndex = i;
                    field.setAccessible(true);
                    Object filedValue = field.get(row);
                    field.setAccessible(false);
                    if (null != filedValue) {
                        // 添加合并主键值
                        primaryIdList.add(String.valueOf(filedValue));
                    }
                    i++;
                    break;
                }
            }
        }
        // 获取需要合并的列
        T row = data.get(0);
        Class<?> aClass = row.getClass();
        Field[] fields = getAllDeclaredFields(aClass);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            ExcelMerge merge = field.getDeclaredAnnotation(ExcelMerge.class);
            if (null != merge && merge.isMerge()) {

                colIndexSet.add(i);
            }
        }
        setMapColumnIndex(data);
    }

    public void setMapColumnIndex(List<T> data) throws IllegalAccessException {
        for (T row : data) {
            Class<?> type = row.getClass();
            Field[] fields = getAllDeclaredFields(type);
            for (int columnIndex = 0; columnIndex < fields.length; columnIndex++) {
                Field field = fields[columnIndex];
                ExcelMerge merge = field.getDeclaredAnnotation(ExcelMerge.class);
                if (null != merge && merge.isMerge()) {//该列属于合并列
                    field.setAccessible(true);
                    Object filedValue = field.get(row);
                    field.setAccessible(false);
                    List<String> list = mapColumnIndex.get(columnIndex);
                    if (CollectionUtils.isEmpty(list)) {
                        list = new ArrayList<>();
                    }
                    list.add(String.valueOf(filedValue));
                    mapColumnIndex.put(columnIndex, list);
                }
            }
        }
    }


    /**
     * 获取类的属性和方法
     */
    public static Field[] getAllDeclaredFields(Class<?> clazz) {
        Class<?> superclass;
        List<Field> fieldList = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        while ((superclass = clazz.getSuperclass()) != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(superclass.getDeclaredFields())));
            clazz = superclass;
        }
        Field[] res = new Field[fieldList.size()];
        res = fieldList.toArray(res);
        return res;
    }


    /**
     * 合并单元格方法
     */
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {

        // 行坐标
        int rowIndex = cell.getRowIndex();
        // 列坐标
        int columnIndex = cell.getColumnIndex();
        Map<Integer, Integer> mergeRowMap = getMergeRowMap(columnIndex);
        // 下行行数
        Integer downRows = mergeRowMap.get(rowIndex - head.getHeadNameList().size());
        // 和并列坐标
        boolean mergeContains = colIndexSet.contains(columnIndex);
        int startRowIndex = rowIndex;
        int endRowIndex = startRowIndex + (downRows == null ? 0 : downRows);
        int startColumnIndex = columnIndex;
        int endColumnIndex = columnIndex;
        //合并单元格区域只有一个单元格时，不合并
        if (endRowIndex == startRowIndex && endColumnIndex == startColumnIndex) {
            return;
        }
        if (null != downRows && mergeContains) {
            // 创建单元格范围地址
            CellRangeAddress cellRangeAddress = new CellRangeAddress(startRowIndex, endRowIndex, startColumnIndex, endColumnIndex);
            sheet.addMergedRegionUnsafe(cellRangeAddress);
        }
    }

    public Map<Integer, Integer> getMergeRowMap(Integer columnIndex) {
        Map<Integer, Integer> mergeRowMap = new HashMap<>();
        List<String> computeList;
        if (columnIndex == excelKeyColumnIndex) {
            computeList = primaryIdList;
            for (int i = 0; i < computeList.size(); i++) {
                mergeRowMap.put(i, getStepsBeforeMeetNotSame(i, computeList, true));
            }
        } else {
            computeList = mapColumnIndex.get(columnIndex);
            for (int i = 0; i < computeList.size(); i++) {
                mergeRowMap.put(i, getStepsBeforeMeetNotSame(i, computeList, false));
            }
        }
        return mergeRowMap;
    }


    /***
     * 根据开始的目标，获取遇到下个不相同的目标前可以前进的步数
     * 如："A","A","A", "B", "B", "A" 如果 startIndex是 0，则结果是2（不考虑keyRepeatSet的情况）
     * @param startIndex
     * @param computeList
     * @return
     */
    public Integer getStepsBeforeMeetNotSame(Integer startIndex, List<String> computeList, boolean ignorePrimaryIdList) {
        int steps = 0;
        Set stepRepeatSet = new HashSet();
        Set keyRepeatSet = new HashSet();
        stepRepeatSet.add(computeList.get(startIndex));
        keyRepeatSet.add(primaryIdList.get(startIndex));
        for (int i = startIndex + 1; i < computeList.size(); i++) {
            // 可以添加成功说明遇到了不同的元素了
            if (ignorePrimaryIdList) {
                if (stepRepeatSet.add(computeList.get(i))) {
                    return steps;
                }
            } else {
                if (stepRepeatSet.add(computeList.get(i)) || keyRepeatSet.add(primaryIdList.get(i))) {
                    return steps;
                }
            }
            steps++;
        }
        return steps;
    }


    public Map<Integer, Integer> computeByMergeKey(List<String> computeList) {
        Map<Integer, Integer> mergeRowMap = new HashMap<>();
        // 主键索引
        int idIndex = 0;
        // 主键临时值
        String tempValue = null;
        // 主键不相同
        for (int i = 0; i < computeList.size(); i++) {
            if (null == tempValue) {
                tempValue = computeList.get(i);
            }
            String id = computeList.get(i);
            if (!id.equals(tempValue)) {
                mergeRowMap.put(idIndex, (i - 1) - idIndex);
                idIndex = i;
                tempValue = null;
            }
            if (computeList.size() - 1 == i) {
                mergeRowMap.put(idIndex, i - idIndex);
            }
        }
        return mergeRowMap;
    }


    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        // 如果字符类型的单元格值为空字符串，那就设置成“/”
        if (cellData.getType().equals(CellDataTypeEnum.STRING) && StringUtils.isEmpty(cellData.getStringValue())) {
            cellData.setStringValue("/");
        }
        // 如果数字类型的单元格值为空，那就设置成“/”
        if (cellData.getType().equals(CellDataTypeEnum.NUMBER) && null == cellData.getNumberValue()) {
            cellData.setStringValue("/");
        }
        super.afterCellDataConverted(writeSheetHolder, writeTableHolder, cellData, cell, head, relativeRowIndex, isHead);
    }

}

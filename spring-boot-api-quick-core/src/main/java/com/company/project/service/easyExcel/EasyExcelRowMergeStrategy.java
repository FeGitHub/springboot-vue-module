package com.company.project.service.easyExcel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.*;


/***
 * 行合并策略
 * 所有的ExcelRowMerge都会围绕 ExcelRowMergeKey的范围内进行数据的合并
 * @param <T>
 */
@Slf4j
public class EasyExcelRowMergeStrategy<T> extends AbstractMergeStrategy {
    // 主键值集合
    private List<String> primaryIdList = new ArrayList<>();
    // 标记了要合并的列号
    Set<Integer> colIndexSet = new HashSet<>();

    // key==>列号  value==>对应此列的所有数据
    Map<Integer, List<String>> mapColumnIndex = new HashMap<>();

    // 被标记的key对应的列序号
    private Integer excelKeyColumnIndex;

    /**
     * 从数据里获取合并的信息
     */
    public EasyExcelRowMergeStrategy(List<T> data) throws IllegalAccessException {
        if (data.size() == 0) {
            throw new RuntimeException("no data exception");
        }
        //收集主键集合信息
        setPrimaryIdList(data);
        //收集合并列信息
        setColIndexSet(data);
        //设置列号和列数据的映射
        setMapColumnIndex(data);
    }


    /****
     * 收集合并列信息
     * @param data
     */
    public void setColIndexSet(List<T> data) {
        // 获取需要合并的列
        T row = data.get(0);
        Class<?> aClass = row.getClass();
        Field[] fields = getAllDeclaredFields(aClass);
        for (int columnIndex = 0; columnIndex < fields.length; columnIndex++) {
            Field field = fields[columnIndex];
            ExcelRowMerge merge = field.getDeclaredAnnotation(ExcelRowMerge.class);
            if (null != merge && merge.isMerge()) {
                colIndexSet.add(columnIndex);
            }
        }
    }


    /***
     * 收集主键集合信息
     * @param data
     * @throws IllegalAccessException
     */
    public void setPrimaryIdList(List<T> data) throws IllegalAccessException {
        for (T row : data) {
            Class<?> type = row.getClass();
            Field[] fields = getAllDeclaredFields(type);
            for (int columnIndex = 0; columnIndex < fields.length; columnIndex++) {
                ExcelRowMergeKey key = fields[columnIndex].getDeclaredAnnotation(ExcelRowMergeKey.class);
                if (null != key) {
                    excelKeyColumnIndex = columnIndex;
                    fields[columnIndex].setAccessible(true);
                    Object filedValue = fields[columnIndex].get(row);
                    fields[columnIndex].setAccessible(false);
                    if (null != filedValue) {
                        // 添加合并主键值
                        primaryIdList.add(String.valueOf(filedValue));
                    }
                    break;
                }
            }
        }
    }

    /****
     * 设置列号和列数据的映射
     * @param data
     * @throws IllegalAccessException
     */
    public void setMapColumnIndex(List<T> data) throws IllegalAccessException {
        for (T row : data) {
            Class<?> type = row.getClass();
            Field[] fields = getAllDeclaredFields(type);
            for (int columnIndex = 0; columnIndex < fields.length; columnIndex++) {
                Field field = fields[columnIndex];
                ExcelRowMerge merge = field.getDeclaredAnnotation(ExcelRowMerge.class);
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
        //合并单元格区域只有一个单元格时，不合并
        if (endRowIndex == startRowIndex) {
            return;
        }
        if (null != downRows && mergeContains) {
            // 创建单元格范围地址
            CellRangeAddress cellRangeAddress = new CellRangeAddress(startRowIndex, endRowIndex, columnIndex, columnIndex);
            sheet.addMergedRegionUnsafe(cellRangeAddress);
        }
    }

    /***
     * 计算合并信息，key是合并开始的序号，value是可向前合并的步数
     * @param columnIndex
     * @return
     */
    public Map<Integer, Integer> getMergeRowMap(Integer columnIndex) {
        Map<Integer, Integer> mergeRowMap = new HashMap<>();
        List<String> computeList;
        if (excelKeyColumnIndex != null && columnIndex == excelKeyColumnIndex) {//获取可合并的主键序号
            computeList = primaryIdList;
            for (int i = 0; i < computeList.size(); i++) {
                mergeRowMap.put(i, getStepsBeforeMeetNotSame(i, computeList, true));
            }
        } else {
            if (colIndexSet.contains(columnIndex)) {//属于要合并的列
                computeList = mapColumnIndex.get(columnIndex);
                for (int i = 0; i < computeList.size(); i++) {
                    mergeRowMap.put(i, getStepsBeforeMeetNotSame(i, computeList, false));
                }
            } else {
                mergeRowMap.put(columnIndex, 0);//不要合并
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
}

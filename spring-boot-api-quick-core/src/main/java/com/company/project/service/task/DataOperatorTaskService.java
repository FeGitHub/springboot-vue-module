package com.company.project.service.task;

import com.company.project.constant.ApplicationProperties;
import com.company.project.service.impl.SystemLogServiceImpl;
import com.company.project.utils.MysqlBakDateBaseUtils;
import com.company.project.utils.StringConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;

/***
 * 数据处理服务作业服务
 */
@Service
public class DataOperatorTaskService {

    private Logger logger = LoggerFactory.getLogger(DataOperatorTaskService.class);


    @Autowired
    SystemLogServiceImpl systemLogServiceImpl;

    /***
     * 数据备份
     */
    public void backUpData() {
        MysqlBakDateBaseUtils.backUpData(null, null, null, null, null);
    }

    /***
     *  冗余数据删除
     */
    public void delData() {
        //把系统日志表7天前的数据删除掉
        systemLogServiceImpl.delLogBefore7Day();
        //把数据库备份文件7天之前的文件删除
        File file = new File(ApplicationProperties.backUpDataFilePath);
        LocalDate beforeNow7 = LocalDate.now().plusDays(-7);
        for (File fileItem : file.listFiles()) {
            if (isDataBaseBackUpFile(fileItem)) {
                LocalDate operatorTime = getLocalDateByFileName(fileItem);
                if (operatorTime != null && operatorTime.isBefore(beforeNow7)) {
                    logger.info("删除文件==>" + fileItem.getName());
                    fileItem.delete();
                }
            }
        }
    }

    /***
     * 是否是数据库备份文件
     * @param file
     * @return
     */
    public boolean isDataBaseBackUpFile(File file) {
        return file.getName().indexOf("数据备份") > -1;
    }

    /****
     *  获取数据库备份文件中的时间信息
     */
    public LocalDate getLocalDateByFileName(File file) {
        String[] nameArr = file.getName().split("_");
        return StringConvertUtil.convertLocalDate(nameArr[1], null);
    }
}

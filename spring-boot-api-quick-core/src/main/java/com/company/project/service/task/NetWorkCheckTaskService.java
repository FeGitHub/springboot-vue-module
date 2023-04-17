package com.company.project.service.task;

import com.company.project.utils.CmdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/***
 * 网络自动维护服务
 */
@Service
public class NetWorkCheckTaskService {


    private static String NET_CHECK_FAIL_MSG = "请求找不到主机";

    private static String NET_WORK_SUCCESS_MSG = "已成功完成连接请求";

    private static int CHECK_NETWOEK_COUNT = 6;//检查网络的次数

    private static int NETWOEK_LINK_FAIL_STAND = 5;//网络连接失败的次数标准（超过此标准就重启）

    private static int NETWOEK_LINK_FAIL_COUNT = 0;//记录网络连接失败的次数


    private static String ALEADY_LINK_WIFI_NAME = "wifi";//本机已经连接过的wifi连接（用于自动连接）

    private Logger logger = LoggerFactory.getLogger(NetWorkCheckTaskService.class);

    /***
     *  维护网络
     */
    public void netWorkCheckTask() {
        if (!isNetWork()) {//网络链接失败
            logger.info("正在尝试重新连接网络...");
            String netWorkLinkMsg = CmdUtil.excuteCmdCommand("netsh wlan connect  " + ALEADY_LINK_WIFI_NAME);//尝试连接网络
            if (netWorkLinkMsg.indexOf(NET_WORK_SUCCESS_MSG) == -1) {
                NETWOEK_LINK_FAIL_COUNT++;
                logger.info("网络连接失败:" + NETWOEK_LINK_FAIL_COUNT);
            } else {
                logger.info("已成功连接网络！");
            }
            if (NETWOEK_LINK_FAIL_COUNT == NETWOEK_LINK_FAIL_STAND) {//如果出现多次网络连接失败，直接重启
                logger.info("重启系统...");
                CmdUtil.excuteCmdCommand("shutdown -r -t 10");
            }
        } else {
            logger.info("网络正常");
        }
    }


    /****
     * 判断网络是否正常的依据
     * @return
     */
    public boolean isNetWork() {
        String netWorkCheckMsg = "";
        Integer failNum = 0;
        for (int i = 0; i < CHECK_NETWOEK_COUNT; i++) {
            netWorkCheckMsg = CmdUtil.excuteCmdCommand("ping www.baidu.com");
            if (netWorkCheckMsg.indexOf(NET_CHECK_FAIL_MSG) > -1) {
                failNum++;
            }
        }
        logger.info("检测网络失败次数:" + failNum.toString());
        return (failNum / CHECK_NETWOEK_COUNT) < 0.2;
    }

}
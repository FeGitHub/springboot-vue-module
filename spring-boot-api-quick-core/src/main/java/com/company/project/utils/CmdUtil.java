package com.company.project.utils;

import com.company.project.job.NetWorkCheckTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * <p>执行windows的cmd命令工具类</p>
 */
public class CmdUtil {

    private static Logger logger = LoggerFactory.getLogger(NetWorkCheckTask.class);


    public static void main(String[] args) {
        excuteCmdCommand("convert --version");
    }

    /**
     * 执行一个cmd命令
     *
     * @param cmdCommand cmd命令
     * @return 命令执行结果字符串，如出现异常返回null
     */
    public static String excuteCmdCommand(String cmdCommand) {
        logger.info("===>" + cmdCommand);
        StringBuilder stringBuilder = new StringBuilder();
        Process process;
        try {
            process = Runtime.getRuntime().exec(cmdCommand);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "GBK"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            String result = stringBuilder.toString();
            logger.info("<===" + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 执行bat文件，
     *
     * @param file          bat文件路径
     * @param isCloseWindow 执行完毕后是否关闭cmd窗口
     * @return bat文件输出log
     */
    public static String excuteBatFile(String file, boolean isCloseWindow) {
        String cmdCommand = null;
        if (isCloseWindow) {
            cmdCommand = "cmd.exe /c " + file;
        } else {
            cmdCommand = "cmd.exe /k " + file;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmdCommand);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "GBK"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 执行bat文件,新开窗口
     *
     * @param file          bat文件路径
     * @param isCloseWindow 执行完毕后是否关闭cmd窗口
     * @return bat文件输出log
     */
    public static String excuteBatFileWithNewWindow(String file, boolean isCloseWindow) {
        String cmdCommand = null;
        if (isCloseWindow) {
            cmdCommand = "cmd.exe /c start" + file;
        } else {
            cmdCommand = "cmd.exe /k start" + file;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmdCommand);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "GBK"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


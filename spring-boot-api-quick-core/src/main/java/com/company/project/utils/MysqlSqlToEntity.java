package com.company.project.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 根据sql表生成java实体类 (MySQL)
 */
public class MysqlSqlToEntity {

    //作者
    private static final String AUTHOR_NAME = "jeff";

    //输出实体类的文件夹路径[为空时根据`PACKAGE_PATH`输出]
    private static final String PATH = "";

    //输出实体类的包
    private static final String PACKAGE_PATH = "com.company.project.create";

    //表名[为空时输出`TAB_PRE`前缀的所有表]
    private static final String[] TAB_NAMES = {};

    //表前缀[为空时输出库中所有表] (生成实体类类名时会去除)
    private static final String[] TAB_PRE = {};

    //需要导入的包 (java.util.Date 会自动判断 不需要添加)
    private static final String[] IMPORTS = {
            "com.baomidou.mybatisplus.annotation.IdType"
            , "com.baomidou.mybatisplus.annotation.TableField"
            , "com.baomidou.mybatisplus.annotation.TableId"
            , "com.baomidou.mybatisplus.annotation.TableName"
            , "lombok.Data"};

    //类注解 占位符(表名):{}
    private static final String[] TAB_COMS = {
            "@Data"
            , "@TableName(\"{}\")"};

    //字段注解 占位符(字段名):{}
    private static final String[] COMMENTS = {
            "@TableField(\"{}\")"};

    //是否生成GetSet方法
    private static final boolean IN_GET_SET = true;

    //数据库连接
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://192.168.61.131:3306/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    private static final String NAME = "root";
    private static final String PASS = "root";

    //其他参数
    private static String[] colnames;
    private static String[] colTypes;
    private static String tableSchema;
    private static Connection con;
    private static Statement run;
    private static boolean importDate = false;

    public static void main(String[] args) {
        try {
            int i = 0;
            //创建数据库连接
            connect();
            //获取所有表名并遍历
            for (String tableName : getAllTables()) {
                boolean b = false;
                //指定表名
                if (TAB_NAMES != null && TAB_NAMES.length > 0) {
                    for (String tabName : TAB_NAMES) {
                        if (tableName.equals(tabName.toLowerCase())) {
                            b = true;
                            break;
                        }
                    }
                }
                //指定表名前缀
                else if (TAB_PRE != null && TAB_PRE.length > 0) {
                    for (String pre : TAB_PRE) {
                        if (tableName.startsWith(pre.toLowerCase())) {
                            b = true;
                            break;
                        }
                    }
                }
                //表名与表名前缀均未指定
                else {
                    b = true;
                }
                if (b) {
                    //处理表
                    toTableInfo(tableName);
                    i++;
                }
            }
            System.out.println("共生成 " + i + " 个实体类");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            colseConnect();
        }
    }

    private static void connect() throws Exception {
        Class.forName(DRIVER);
        con = DriverManager.getConnection(URL, NAME, PASS);
        run = con.createStatement();
        tableSchema = URL.split("\\?")[0].substring(URL.split("\\?")[0].lastIndexOf("/") + 1);
    }

    private static List<String> getAllTables() throws Exception {
        List<String> ls = new ArrayList<>();
        String sql = "SELECT TABLE_NAME FROM information_schema.tables WHERE table_schema='" + tableSchema + "'";
        ResultSet rs = run.executeQuery(sql);
        while (rs.next()) {
            ls.add(rs.getString("TABLE_NAME").toLowerCase());
        }
        return ls;
    }

    private static void toTableInfo(String tableName) throws Exception {
        String sql = "SELECT * FROM " + tableName + " LIMIT 1";
        ResultSet rs = run.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData();
        int size = rsmd.getColumnCount();
        colnames = new String[size];
        colTypes = new String[size];
        for (int i = 0; i < size; i++) {
            colnames[i] = rsmd.getColumnName(i + 1);
            colTypes[i] = rsmd.getColumnTypeName(i + 1);
        }
        //规范表名
        String normTableName = normTableName(tableName);
        //获取该表注释
        String tableComment = getTableComment(tableName);
        //获取该表字段信息
        StringBuffer tempSb = getColsInfo(tableName);
        //生成GetSet方法
        if (IN_GET_SET) {
            toGetSet(tempSb);
        }
        //生成Java文件
        toJavaFile(normTableName, getSb(tableName, normTableName, tableComment, tempSb));
    }

    //规范表名
    private static String normTableName(String tableName) {
        if (TAB_PRE != null && TAB_PRE.length > 0) {
            for (String pre : TAB_PRE) {
                if (tableName.startsWith(pre.toLowerCase()) && !pre.toLowerCase().equals(tableName)) {
                    tableName = tableName.substring(pre.length());
                    break;
                }
            }
        }
        return capitalLetters(underlineToHump(tableName));
    }

    //获取表注释
    private static String getTableComment(String tableName) throws Exception {
        String str = "";
        String sql = "SELECT table_comment FROM information_schema.tables WHERE table_schema='" + tableSchema + "' AND table_name = '" + tableName + "'";
        ResultSet rs = run.executeQuery(sql);
        while (rs.next()) {
            str = rs.getString("table_comment");
            if (str == null) {
                str = "";
            } else {
                str = str.replace("\r\n", " ")
                        .replace("\n", " ")
                        .replace("\r", " ")
                        .trim();
            }
        }
        return str;
    }

    //生成类内容
    private static StringBuffer getColsInfo(String tableName) throws Exception {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < colnames.length; i++) {
            sb.append("\r\n");
            //字段注释
            String colComment = getColComment(tableName, colnames[i]);
            if (null != colComment && !"".equals(colComment)) {
                sb.append("// ").append(colComment).append("\r\n");
            }
            //注解
            for (String s : COMMENTS) {
                sb.append(s.replace("{}", colnames[i])).append("\r\n");
            }
            //小驼峰
            String colsName = underlineToHump(colnames[i]);
            //类型
            String type = sqlToJavaType(colTypes[i]);
            sb.append("private ").append(type).append(" ").append(colsName).append(";\r\n");
        }
        return sb;
    }

    //生成GetSet方法
    private static void toGetSet(StringBuffer sb) {
        for (int i = 0; i < colnames.length; i++) {
            sb.append("\r\n");
            //小驼峰
            String colsName = underlineToHump(colnames[i]);
            //大驼峰
            String capitalName = capitalLetters(colsName);
            //类型
            String type = sqlToJavaType(colTypes[i]);
            sb.append("public ").append(type).append(" get").append(capitalName).append("() {\r\n");
            sb.append("return this.").append(colsName).append(";\r\n");
            sb.append("}\r\n");
            sb.append("\r\n");
            sb.append("public void set").append(capitalName).append("(").append(type).append(" ").append(colsName).append(") {\r\n");
            sb.append("this.").append(colsName).append(" = ").append(colsName).append(";\r\n");
            sb.append("}\r\n");
        }
    }

    //生成文本
    private static StringBuffer getSb(String tableName, String normTableName, String tableComment, StringBuffer colSb) {
        StringBuffer sb = new StringBuffer();
        if (PACKAGE_PATH != null && !PACKAGE_PATH.equals("")) {
            sb.append("package ").append(PACKAGE_PATH).append(";\r\n");
            sb.append("\r\n");
        }
        sb.append("import java.io.Serializable;\r\n");
        for (String s : IMPORTS) {
            sb.append("import ").append(s).append(";\r\n");
        }
        if (importDate) {
            sb.append("import java.util.Date;\r\n");
            importDate = false;
        }
        sb.append("\r\n");
        //注释
        sb.append("/**\r\n");
        sb.append(" * ").append(tableComment).append("\r\n");
        sb.append(" *\r\n");
        sb.append(" * @author ").append(AUTHOR_NAME).append("\r\n");
        sb.append(" * @date ").append(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date())).append("\r\n");
        sb.append(" */\r\n");
        //注解
        for (String s : TAB_COMS) {
            sb.append(s.replace("{}", tableName)).append("\r\n");
        }
        //实体类
        sb.append("public class ").append(normTableName).append(" implements Serializable").append(" {\r\n");
        sb.append("private static final long serialVersionUID = 1L;\r\n");
        sb.append(colSb);
        sb.append("}");
        return sb;
    }

    //生成java文件
    private static void toJavaFile(String tableName, StringBuffer content) throws Exception {
        String path = new File("").getAbsolutePath() + "/src/main/java/";
        if (PATH != null && !PATH.equals("")) {
            if (PATH.endsWith("/") || PATH.endsWith("\\")) {
                path = PATH;
            } else {
                path = PATH + "/";
            }
        } else if (PACKAGE_PATH != null && !PACKAGE_PATH.equals("")) {
            path += PACKAGE_PATH.replace(".", "/") + "/";
        } else {
            path += "entity/";
        }
        dirExists(path);
        String outputPath = path + tableName + ".java";
        FileWriter fw = new FileWriter(outputPath);
        PrintWriter pw = new PrintWriter(fw);
        for (String s : content.toString().split("\r\n")) {
            pw.println(s);
            pw.flush();
        }
        pw.close();
    }

    //数据类型处理
    private static String sqlToJavaType(String sqlType) {
        if (sqlType.equalsIgnoreCase("TINYINT")) {
            return "Byte";
        } else if (sqlType.equalsIgnoreCase("SMALLINT")
                || sqlType.equalsIgnoreCase("MEDIUMINT")) {
            return "Short";
        } else if (sqlType.equalsIgnoreCase("INT")
                || sqlType.equalsIgnoreCase("INTEGER")) {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("BIGINT")) {
            return "Long";
        } else if (sqlType.equalsIgnoreCase("FLOAT")
                || sqlType.equalsIgnoreCase("DOUBLE")) {
            return "Double";
        } else if (sqlType.equalsIgnoreCase("DATE")
                || sqlType.equalsIgnoreCase("TIME")
                || sqlType.equalsIgnoreCase("YEAR")
                || sqlType.equalsIgnoreCase("DATETIME")
                || sqlType.equalsIgnoreCase("TIMESTAMP")) {
            importDate = true;
            return "Date";
        }
        return "String";
    }

    //获取字段注释
    private static String getColComment(String tableName, String columnName) throws Exception {
        String str = "";
        String sql = "SELECT column_comment FROM information_schema.columns WHERE table_schema='" + tableSchema + "' AND TABLE_NAME='" + tableName + "' AND COLUMN_NAME='" + columnName + "'";

        ResultSet rs = run.executeQuery(sql);
        while (rs.next()) {
            str = rs.getString("column_comment");
            if (str == null) {
                str = "";
            } else {
                str = str.replace("\r\n", " ")
                        .replace("\n", " ")
                        .replace("\r", " ")
                        .trim();
            }
        }
        return str;
    }

    //下划线命名转为小驼峰命名
    public static String underlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String[] p = para.split("_");
        for (String s : p) {
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    //首字母大写
    public static String capitalLetters(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    //判断文件夹是否存在
    public static void dirExists(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    //清除链接
    private static void colseConnect() {
        try {
            if (con != null) {
                con.close();
                con = null;
            }
            if (run != null) {
                run.close();
                run = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

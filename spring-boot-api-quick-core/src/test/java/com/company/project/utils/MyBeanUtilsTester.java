package com.company.project.utils;

import com.company.project.Tester;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MyBeanUtilsTester extends Tester {

    @Test
    public void testCopy() throws Exception {
        Map<String, Object> testMap = new HashMap<String, Object>();
        testMap.put("test1", "字段1");
        testMap.put("test2", "字段2");
        testMap.put("test3", "字段3");
        TestVo vo = MyBeanUtils.copyProperties(testMap, TestVo.class);
        System.out.println(vo.getAllField());
    }

    public static class TestVo {
        private String test1;

        private String TEST2;

        private String TEST_3;

        public String getTest1() {
            return test1;
        }

        public void setTest1(String test1) {
            this.test1 = test1;
        }

        public String getTEST2() {
            return TEST2;
        }

        public void setTEST2(String TEST2) {
            this.TEST2 = TEST2;
        }

        public String getTEST_3() {
            return TEST_3;
        }

        public void setTEST_3(String TEST_3) {
            this.TEST_3 = TEST_3;
        }

        public String getAllField() {
            return "test1:" + test1 + ",TEST2:" + TEST2 + ",TEST_3:" + TEST_3;
        }
    }
}

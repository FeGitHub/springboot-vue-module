package com.company.project.service.test;

import com.company.project.master.vo.UserEntity1;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExcelService {


    public Workbook createWorkbook(InputStream inputStream) {
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (Exception e) {

        }
        return workbook;
    }


    /**
     * 构造假数据，实际上应该从数据库查出来
     *
     * @return List<UserEntity>
     */
    public List<UserEntity1> getData() {
        List<UserEntity1> users = new ArrayList<UserEntity1>();
        for (int i = 1; i <= 9; i++) {
            UserEntity1 user = new UserEntity1();
            user.setBirthday(new Date());
            user.setName("user_" + i);
            user.setSalary(1.285 * i);
            user.setTelphone("1888888888" + i);
            users.add(user);
        }
        return users;
    }
}

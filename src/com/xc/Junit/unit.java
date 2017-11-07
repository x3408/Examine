package com.xc.Junit;

import com.xc.Service.StudentService;
import com.xc.Service.TeacherService;
import com.xc.util.SqlHelper;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class unit {
    @Test
    public void c() {
        ArrayList<String> arr = new StudentService("1").UnfinishedHomework();
        System.out.println(arr);
    }

//    @Test
//    public void t() {
//        new TeacherService().addNewHomework();
//    }
    @Test
    public void stringTest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入新添加的作业标题，说明，评分项。并以-分开，评分项请用逗号");
        String str = scanner.nextLine();
        String [] data = str.trim().split("[-]");
    }
}

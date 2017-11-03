package com.xc.view;

import com.xc.DAO.TeacherServiceDAO;
import com.xc.Service.TeacherService;
import com.xc.util.SqlHelper;
import javafx.scene.chart.ScatterChart;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class View {

    private static Scanner scanner = new Scanner(System.in);
    private static TeacherServiceDAO ts = new TeacherService();

    public static void main(String[] args) {
        new View().start();
    }

    private void start() {
        while(true) {
            System.out.println("选择功能");
            System.out.println("1.添加作业");
            System.out.println("2.显示当前作业");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    addHomework();
                    break;
                case "2":
                    showHomework();
                    break;
                case "0":
                    System.exit(0);
                default:
                    System.out.println("输入的参数有误");
            }
        }
    }

    private void addHomework() {
        ts.addNewHomework();
    }

    private void showHomework() {

        ArrayList<String> arr = ts.showCurrentHomework();
        System.out.println(arr);
    }
}

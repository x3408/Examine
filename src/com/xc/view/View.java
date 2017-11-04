package com.xc.view;

import com.xc.DAO.TeacherServiceDAO;
import com.xc.Service.TeacherService;
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
                case "3":
                    findHomework();
                    break;
                case "4":
                    deleteHomework();
                    break;
                case "0":
                    System.exit(0);
                default:
                    System.out.println("输入的参数有误");
            }
        }
    }

    private void deleteHomework() {
        System.out.println("输入你想要删除的Id");
        String id = scanner.nextLine();
        ts.deleteHomework(id);
    }

    private void findHomework() {
        System.out.println("输入课程名");
        String course = scanner.nextLine();
        System.out.println("输入章节名");
        String chapter = scanner.nextLine();

        ts.findHomework(course, chapter);
    }

    private void addHomework() {
        ts.addNewHomework();
    }

    private void showHomework() {

        ArrayList<String> arr = ts.showCurrentHomework();
        System.out.println(arr);
        boolean flag = true;
        while (flag) {
            System.out.println("1查看详细信息 2编辑作业 0退出");
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    System.out.println("输入查看的作业id，0退出");
                    String id = scanner.nextLine();
                    if (id.equals("0"))
                        break;
                    arr = ts.showHomeworkDetails(id);
                    System.out.println(arr);
                    break;
                case "2":
                    System.out.println("输入编辑的作业id，0退出");
                    id = scanner.nextLine();
                    if (id.equals("0"))
                        break;
                    ts.editHomework(id);
                    break;
                case "0":
                    flag = false;
                    break;
                default:
                    break;
            }
        }
    }
}

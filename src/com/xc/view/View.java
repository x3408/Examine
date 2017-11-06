package com.xc.view;

import com.xc.DAO.StudentServiceDAO;
import com.xc.DAO.TeacherServiceDAO;
import com.xc.Service.StudentService;
import com.xc.Service.TeacherService;
import java.util.ArrayList;
import java.util.Scanner;

public class View {

    private static Scanner scanner = new Scanner(System.in);
    private static TeacherServiceDAO ts = new TeacherService();
    private static StudentServiceDAO ss;

    public static void main(String[] args) {
        new View().start();
    }

    private void start() {
        while(true) {
            System.out.println("选择功能");
            System.out.println("1.添加作业");
            System.out.println("2.显示当前作业");
            System.out.println("3.按条件查找作业");
            System.out.println("4.删除已经布置的作业");
            System.out.println("5.批改作业");
            System.out.println("6.学生端");
            System.out.println("0.退出");
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
                case "5":
                    updateHomework();
                    break;
                case "6":
                    System.out.println("输入学生id");
                    String student_id = scanner.nextLine();
                    ss = new StudentService(student_id);
                    studentView();
                case "0":
                    System.exit(0);
                default:
                    System.out.println("输入的参数有误");
            }
        }
    }

    private void studentView() {
        while(true) {
            System.out.println("选择对应的功能：");
            System.out.println("1.查看未完成的作业");
            System.out.println("2.查看已经完成的作业");
            System.out.println("0.退出");
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    UnfinishedHomework();
                    break;
                case "2":
                    FinishedHomework();
                    break;
                case "0":
                    System.exit(0);
            }
        }
    }

    private void FinishedHomework() {
        ArrayList<String> arr = ss.FinishedHomework();
        System.out.println(arr);
        boolean flag = true;
        while (flag) {
            System.out.println("1.查看");
            System.out.println("2.修改作业内容");
            System.out.println("0.退出");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    checkHomework();
                    break;
                case "2":
                    updateHomeworkContent();
                    break;
                case "0":
                    flag = false;
                    break;
                default:
                    break;
            }
        }
    }

    private void updateHomeworkContent() {
        System.out.println("输入提交的作业id");
        String id = scanner.nextLine();
        if(id.equals("0"))
            return;
        System.out.println("输入作业内容");
        String content = scanner.nextLine();
        if(content.equals("0"))
            return;
        ss.SubmitHomework(id, content);
    }

    private void checkHomework() {
        System.out.println("输入想查看的id");
        String id = scanner.nextLine();

        ArrayList<String> arr = ss.checkHomework(id);
        System.out.println(arr);
    }

    private void UnfinishedHomework() {
        ArrayList<String> arr = ss.UnfinishedHomework();
        System.out.println(arr);
        while(true) {
            System.out.println("提交作业功能，输入提交的作业id,没有提交按0退出");
            String id = scanner.nextLine();
            if(id.equals("0"))
                break;
            System.out.println("输入作业内容");
            String content = scanner.nextLine();
            ss.SubmitHomework(id, content);
        }
    }

    private void updateHomework() {
        ArrayList<String> arr = ts.showCurrentHomework();
        System.out.println(arr);
        while (true) {
            System.out.println("选择你想要批改的作业, 0退出");
            String id = scanner.nextLine();
            if( "0".equals(id))
                break;
            ArrayList<String> submitHomework = ts.showSubmitHomework(id);
            System.out.println(submitHomework);
            System.out.println("选择功能：1.查看 2.批改");
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    System.out.println("输入学生id");
                    String student_id = scanner.nextLine();
                    ArrayList<String> detail = ts.showSubmitHomeworkDetail(id,student_id);
                    System.out.println(detail);
                    break;
                case "2":
                    System.out.println("依次输入学生id、成绩和评语，用-分开");
                    String [] data = scanner.nextLine().split("[-]");
                    boolean flag = ts.remarkHomework(id, data[0], data[1], data[2]);
                    if(flag)
                        System.out.println("打分成功");
                    else
                        System.out.println("打分失败");
                    break;
                default:
                    break;
            }
        }
    }

    private void deleteHomework() {
        System.out.println("输入你想要删除的Id");
        String id = scanner.nextLine();
        boolean b =ts.deleteHomework(id);
        if(b)
            System.out.println("删除成功");
    }

    private void findHomework() {
        System.out.println("输入课程名");
        String course = scanner.nextLine();
        System.out.println("输入章节名");
        String chapter = scanner.nextLine();

        ArrayList<String> arr = ts.findHomework(course, chapter);
        System.out.println(arr);
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

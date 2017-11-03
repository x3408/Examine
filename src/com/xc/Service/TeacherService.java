package com.xc.Service;

import com.xc.DAO.TeacherServiceDAO;
import com.xc.util.SqlHelper;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class TeacherService implements TeacherServiceDAO{
    private Scanner scanner = new Scanner(System.in);


    @Override
    public ArrayList<String> findHomework(String course, String chapter) {
        ArrayList<String> arr = new ArrayList<>();
        String sql = "SELECT homework.id,course.course_name,chapter.chapter_name,content.content_title,content.content_illustrate,\n"+
                "scope2.showtime\n" +
                "FROM homework,scope2,content,course,chapter\n" +
                "where content.content_id=scope2.content_id and homework.content_id=content.content_id AND\n" +
                "course.course_id=homework.course_id AND\n" +
                "chapter.chapter_id=homework.chapter_id AND course.course_name=? AND chapter.chapter_name=?\n"+
                "GROUP BY\n" +
                "homework.id";
        Connection conn = SqlHelper.getInstance().getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, course);
            ps.setString(2, chapter);

            rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            while(rs.next()) {
                for (int i = 2; i <= rsmd.getColumnCount(); i++) {
                    arr.add(rs.getString(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn, ps, rs);
        }
        return arr;
    }

    @Override
    public boolean addNewHomework() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入新添加的作业标题，说明，评分项。并以=分开，评分项请用逗号");
        String str = scanner.nextLine();
        String [] data = str.trim().split("[=]");


        String sql = "INSERT INTO \n" +
                "content\n" +
                "(content_title,content_illustrate,content_subject,content_answer,scope_id)\n" +
                "VALUES (\n" +
                "?,?,NULL,NULL,?\n" +
                ")";
        Connection conn = SqlHelper.getInstance().getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < 3; i++) {
                ps.setString(i+1, data[i]);
            }

            int i = ps.executeUpdate();
            if( i == 1) {
                System.out.println("插入成功");
            } else {
                throw new RuntimeException("插入失败，结束本次添加");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn, ps, rs);
        }



        sql = "select LAST_INSERT_ID()";
        int content_id = 0;
        rs = SqlHelper.executeStringQuery(sql, null);
        try {
            if(rs.next())
                content_id = Integer.parseInt(rs.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("请依次输入班级id，显示时间，提交时间，用=分开");

        data = scanner.nextLine().split("[=]");
        sql = "INSERT INTO \n" +
                "scope2\n" +
                "VALUES(?,?,?,?)";

        conn = SqlHelper.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1,content_id);
            ps.setInt(2,Integer.parseInt(data[0]));
            ps.setDate(3, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(data[1]).getTime()));
            ps.setDate(4, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(data[2]).getTime()));

            int i = ps.executeUpdate();
            if(i > 0) {
                System.out.println("插入成功");
                System.out.println("请输入课程名，章节名");
            }else {
                throw new RuntimeException("插入失败，结束本次添加");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn, ps ,rs);
        }

        sql = "INSERT INTO\n" +
                "homework\n" +
                "(course_id,chapter_id,content_id)\n" +
                "VALUES(\n" +
                "(SELECT course.course_id from course WHERE course_name=?),\n" +
                "(SELECT chapter.chapter_id from chapter WHERE chapter_name=?),\n" +
                "?\n" +
                ")";
        data = scanner.nextLine().split("[=]");

        conn = SqlHelper.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,data[0]);
            ps.setString(2,data[1]);
            ps.setInt(3,content_id);

            int i = ps.executeUpdate();
            if(i == 1) {
                System.out.println("添加作业成功");
            }else {
                throw new RuntimeException("插入失败，结束本次添加");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn,ps,rs);
        }
        return true;
    }

    @Override
    public boolean editHomework() {
        return false;
    }

    @Override
    public ArrayList<String> showCurrentHomework() {
        ArrayList<String> arr = new ArrayList<>();
        String sql="SELECT homework.id,course.course_name,chapter.chapter_name,content.content_title,\n" +
                "scope2.showtime,scope2.submittime\n" +
                "FROM homework,scope2,content,course,chapter\n" +
                "where content.content_id=scope2.content_id and homework.content_id=content.content_id AND\n" +
                "course.course_id=homework.course_id AND\n" +
                "chapter.chapter_id=homework.chapter_id\n" +
                "GROUP BY\n" +
                "homework.id\n";
        ResultSet rs = SqlHelper.executeStringQuery(sql, null);
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            while(rs.next()) {
                for (int i = 2; i <= rsmd.getColumnCount(); i++) {
                    arr.add(rs.getString(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs!=null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    @Override
    public boolean deleteHomework(String id) {
        return false;
    }

    @Override
    public ArrayList<String> showHomeworkDetails(String id) {
        return null;
    }
}
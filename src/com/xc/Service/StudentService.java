package com.xc.Service;

import com.sun.org.apache.regexp.internal.RE;
import com.xc.DAO.StudentServiceDAO;
import com.xc.util.SqlHelper;

import java.sql.*;
import java.util.ArrayList;

public class StudentService implements StudentServiceDAO{

    private String student_id;
    public StudentService(String student_id) {
        this.student_id = student_id;
    }
    @Override
    public ArrayList<String> UnfinishedHomework() {
        ArrayList<String> arr = new ArrayList<>();
        String sql = "SELECT\n" +
                "\thomework.id,\n" +
                "\tcourse.course_name,\n" +
                "\tchapter.chapter_name,\n" +
                "\tcontent.content_title,\n" +
                "\tscope2.showtime,\n" +
                "\tscope2.submittime\n" +
                "FROM\n" +
                "\thomework,\n" +
                "\tscope2,\n" +
                "\tcontent,\n" +
                "\tcourse,\n" +
                "\tchapter,\n" +
                "\tsbhm\n" +
                "WHERE\n" +
                "\tcontent.content_id = scope2.content_id\n" +
                "AND homework.content_id = content.content_id\n" +
                "AND course.course_id = homework.course_id\n" +
                "AND chapter.chapter_id = homework.chapter_id\n" +
                "AND sbhm.homeworkContent IS NULL\n" +
                "AND sbhm.id = homework.id\n" +
                "AND sbhm.student_id =?\n" +
                "GROUP BY\n" +
                "\thomework.id\n" +
                "LIMIT 15";

        Connection conn = SqlHelper.getInstance().getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(student_id));
            rs = ps.executeQuery();

            while(rs.next())
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    arr.add(rs.getString(i+1));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn, ps ,rs);
        }
        return arr;
    }

    @Override
    public ArrayList<String> FinishedHomework() {
        ArrayList<String> arr = new ArrayList<>();
        String sql = "SELECT\n" +
                "\thomework.id,\n" +
                "\tcourse.course_name,\n" +
                "\tchapter.chapter_name,\n" +
                "\tcontent.content_title,\n" +
                "\tscope2.showtime,\n" +
                "\tscope2.submittime,\n" +
                "\tsbhm.score\n" +
                "FROM\n" +
                "\thomework,\n" +
                "\tscope2,\n" +
                "\tcontent,\n" +
                "\tcourse,\n" +
                "\tchapter,\n" +
                "\tsbhm\n" +
                "WHERE\n" +
                "\tcontent.content_id = scope2.content_id\n" +
                "AND homework.content_id = content.content_id\n" +
                "AND course.course_id = homework.course_id\n" +
                "AND chapter.chapter_id = homework.chapter_id\n" +
                "AND sbhm.homeworkContent is not null\n" +
                "AND sbhm.id = homework.id\n" +
                "AND student_id =?\n" +
                "GROUP BY\n" +
                "\thomework.id\n" +
                "LIMIT 15 ";

        Connection conn = SqlHelper.getInstance().getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(student_id));
            rs = ps.executeQuery();

            while(rs.next())
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    arr.add(rs.getString(i+1));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn, ps ,rs);
        }
        return arr;
    }

    @Override
    public boolean SubmitHomework(String id, String homeworkContent) {
        String sql = "UPDATE sbhm SET homeworkContent= ?,stime = CURDATE() where student_id = ? and id = ?";
        Connection conn = SqlHelper.getInstance().getConnection();
        PreparedStatement ps = null;
        int i = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, homeworkContent);
            ps.setInt(2, Integer.parseInt(student_id));
            ps.setInt(3, Integer.parseInt(id));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn, ps);
        }

        if(i == 0) {
            System.out.println("提交成功");
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<String> checkHomework(String id) {
        ArrayList<String> arr = new ArrayList<>();
        String sql = "select course.course_name,chapter.chapter_name,content.content_title,\n" +
                "content.content_illustrate,content.scope_id\n" +
                "from homework\n" +
                "INNER JOIN course ON\n" +
                "course.course_id=homework.course_id\n" +
                "INNER JOIN chapter ON\n" +
                "chapter.chapter_id=homework.chapter_id\n" +
                "INNER JOIN content ON\n" +
                "homework.content_id=content.content_id\n" +
                "where homework.id = ?";
        Connection conn = SqlHelper.getInstance().getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));
            rs = ps.executeQuery();

            if (rs.next()) {
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    arr.add(rs.getString(i+1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "SELECT sbhm.homeworkContent,sbhm.score from sbhm where student_id=? and sbhm.id=?";
        conn = SqlHelper.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(student_id));
            ps.setInt(2, Integer.parseInt(id));

            rs = ps.executeQuery();

            if (rs.next()) {
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    arr.add(rs.getString(i+1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arr;
    }
}

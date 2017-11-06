package com.xc.Service;

import com.xc.DAO.TeacherServiceDAO;
import com.xc.util.SqlHelper;
import org.omg.CORBA.INTERNAL;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class TeacherService implements TeacherServiceDAO {
    private Scanner scanner = new Scanner(System.in);


    @Override
    public ArrayList<String> findHomework(String course, String chapter) {
        ArrayList<String> arr = new ArrayList<>();
        String sql = "SELECT homework.id,course.course_name,chapter.chapter_name,content.content_title,content.content_illustrate,\n" +
                "scope2.showtime\n" +
                "FROM homework,scope2,content,course,chapter\n" +
                "where content.content_id=scope2.content_id and homework.content_id=content.content_id AND\n" +
                "course.course_id=homework.course_id AND\n" +
                "chapter.chapter_id=homework.chapter_id AND course.course_name=? AND chapter.chapter_name=?\n" +
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
            while (rs.next()) {
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
        int content_id = 0;
        int homework_id = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入新添加的作业标题，说明，评分项。并以=分开，评分项请用逗号");
        String str = scanner.nextLine();
        String[] data = str.trim().split("[=]");


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
                ps.setString(i + 1, data[i]);
            }

            int i = ps.executeUpdate();
            if (i == 1) {
                System.out.println("插入成功");
            } else {
                throw new RuntimeException("插入失败，结束本次添加");
            }
            sql = "select LAST_INSERT_ID()";


            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next())
                content_id = Integer.parseInt(rs.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn, ps, rs);
        }

        System.out.println("请依次输入班级id，显示时间，提交时间，用=分开");

        data = scanner.nextLine().split("[=]");
        sql = "INSERT INTO \n" +
                "scope2\n" +
                "VALUES(?,?,?,?)";

        conn = SqlHelper.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, content_id);
            ps.setInt(2, Integer.parseInt(data[0]));
            ps.setDate(3, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(data[1]).getTime()));
            ps.setDate(4, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(data[2]).getTime()));

            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("插入成功");
                System.out.println("请输入课程名，章节名");
            } else {
                throw new RuntimeException("插入失败，结束本次添加");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn, ps, rs);
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
            ps.setString(1, data[0]);
            ps.setString(2, data[1]);
            ps.setInt(3, content_id);

            int i = ps.executeUpdate();
            if (i == 1) {
                System.out.println("添加作业成功");
            } else {
                throw new RuntimeException("插入失败，结束本次添加");
            }

            sql = "select LAST_INSERT_ID()";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next())
                homework_id = Integer.parseInt(rs.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn, ps, rs);
        }

        sql = "select student_id from student";
        conn = SqlHelper.getInstance().getConnection();
        try {
            ArrayList<Integer> nums = new ArrayList<>();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            for(int i=1;rs.next();i++) {
                nums.add(rs.getInt(1));
            }
            sql = "INSERT INTO sbhm(id, student_id) values(?,?)";

            for (int i = 0; i < nums.size(); i++) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1,homework_id);
                ps.setInt(2, nums.get(i));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn, ps, rs);
        }
        return true;
    }

    @Override
    public boolean editHomework(String id) {
        addNewHomework();
        deleteHomework(id);
        return true;
    }

    @Override
    public ArrayList<String> showCurrentHomework() {
        ArrayList<String> arr = new ArrayList<>();
        String sql = "SELECT homework.id,course.course_name,chapter.chapter_name,content.content_title,\n" +
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
            while (rs.next()) {
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    arr.add(rs.getString(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return arr;
    }

    @Override
    public boolean deleteHomework(String id) {
        String sql = "DELETE FROM homework where id = ?";
        Connection conn = SqlHelper.getInstance().getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn, ps);
        }
        return true;
    }

    @Override
    public ArrayList<String> showHomeworkDetails(String id) {
        ArrayList<String> arr = new ArrayList<>();
        String sql = "SELECT \n" +
                "homework.id,\n" +
                "course.course_name,\n" +
                "chapter.chapter_name,\n" +
                "content.content_title,\n" +
                "content.content_illustrate,\n" +
                "content.content_subject,\n" +
                "content.content_id,\n" +
                "content.scope_id\n" +
                "FROM homework,scope2,content,course,chapter\n" +
                "where homework.content_id=content.content_id AND\n" +
                "course.course_id=homework.course_id AND\n" +
                "chapter.chapter_id=homework.chapter_id AND homework.id=?\n" +
                "GROUP BY\n" +
                "homework.id";

        Connection conn = SqlHelper.getInstance().getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int content_id = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));

            rs = ps.executeQuery();

            if (rs.next()) {
                for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
                    arr.add(rs.getString(i + 1));
                }
                content_id = rs.getInt("content_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn, ps, rs);
        }

        sql = "select class.class_name,showtime,submittime\n" +
                "from scope2\n" +
                "INNER JOIN class ON\n" +
                "class.class_id=scope2.class_id\n" +
                "where content_id=?";

        conn = SqlHelper.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, content_id);
            rs = ps.executeQuery();

            while (rs.next()) {
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    arr.add(rs.getString(i + 1));
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
    public boolean remarkHomework(String id, String student_id, String score, String remark) {
        String sql = "UPDATE sbhm SET score=?,remark=? where id=? and student_id=?";
        Connection conn = SqlHelper.getInstance().getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(score));
            ps.setString(2, remark);
            ps.setInt(3, Integer.parseInt(id));
            ps.setInt(4, Integer.parseInt(student_id));

            int i = ps.executeUpdate();
            if(i == 1){
                System.out.println("批改作业成功");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn, ps);
        }
        return false;
    }

    @Override
    public ArrayList<String> showSubmitHomework(String id) {
        ArrayList<String> arr = new ArrayList<>();
        String sql = "SELECT student.dept,class.class_name,student.name,student.grade,stime,score \n" +
                "from sbhm \n" +
                "INNER JOIN student ON\n" +
                "student.student_id = sbhm.student_id\n" +
                "INNER JOIN class ON\n" +
                "student.class_id = class.class_id\n" +
                "where id = ?";

        Connection conn = SqlHelper.getInstance().getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));

            rs = ps.executeQuery();
            while(rs.next()) {
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    arr.add(rs.getString(i+1));
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
    public ArrayList<String> showSubmitHomeworkDetail(String id, String student_id) {
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

            if(rs.next()) {
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    arr.add(rs.getString(i+1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn, ps);
        }

        sql = "SELECT sbhm.homeworkContent,sbhm.score from sbhm where student_id=? and sbhm.id=?";
        conn = SqlHelper.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));
            ps.setInt(2, Integer.parseInt(student_id));

            rs = ps.executeQuery();
            if (rs.next()) {
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    arr.add(rs.getString(i+1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn, ps, rs);
        }
        return arr;
    }
}

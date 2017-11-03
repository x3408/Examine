package com.xc.Junit;

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
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入新添加的作业标题，说明，评分项。并以-分开，评分项请用逗号");
        String str = scanner.nextLine();
        String [] data = str.trim().split("[-]");


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
            for (int i = 0; i < 2; i++) {
                ps.setString(i + 1, data[i]);
            }
            ps.setInt(3, Integer.parseInt(data[2]));

            int i = ps.executeUpdate();
            System.out.println("设置成功,");
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            SqlHelper.free(conn, ps, rs);
        }
    }

    @Test
    public void stringTest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入新添加的作业标题，说明，评分项。并以-分开，评分项请用逗号");
        String str = scanner.nextLine();
        String [] data = str.trim().split("[-]");
    }
}

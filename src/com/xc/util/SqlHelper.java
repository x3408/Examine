package com.xc.util;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class SqlHelper {
    private static Properties prop = new Properties();
    static {
        try {
            prop.load(SqlHelper.class.getClassLoader().getResourceAsStream("util.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static SqlHelper instance = new SqlHelper();
    private static DataSource dataSource;
    private static Connection conn;
    private static Statement st;
    private static PreparedStatement ps;
    private static ResultSet rs;


    private SqlHelper() {}

    public static SqlHelper getInstance() {
        return instance;
    }

    public Connection getConnection() {
        createDataSource();

        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    private void createDataSource() {
        try {
            dataSource = BasicDataSourceFactory.createDataSource(prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void free(Connection conn, Statement st, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (st != null)
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (conn != null)
//							new MyConnection(conn, instance).close();
                            conn.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public static void free(Connection conn,Statement st){
        try {
            if (st != null)
                st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
//					new MyConnection(conn, instance).close();
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ResultSet executeQuery(String sql,Object [] parameters) {

        try {
            conn = instance.getConnection();
            if(parameters == null) {
                st = conn.createStatement();
                rs = st.executeQuery(sql);
            } else {
                ps = conn.prepareStatement(sql);
                for(int i=0;i<parameters.length;i++) {
                    ps.setObject(i+1, parameters[i]);
                }
                rs = ps.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
}

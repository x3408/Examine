package com.xc.DAO;

import java.util.ArrayList;

public interface TeacherServiceDAO {

    ArrayList<String> findHomework(String course, String chapter);

    boolean addNewHomework();

    boolean editHomework(String id);

    ArrayList<String> showCurrentHomework();

    boolean deleteHomework(String id);

    ArrayList<String> showHomeworkDetails(String id);

}

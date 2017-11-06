package com.xc.DAO;

import java.util.ArrayList;

public interface StudentServiceDAO {

    ArrayList<String> UnfinishedHomework();

    ArrayList<String> FinishedHomework();

    boolean SubmitHomework(String id, String homeworkContent);

    ArrayList<String> checkHomework(String id);


}

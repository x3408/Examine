package com.xc.DAO;

import java.util.ArrayList;

public interface TeacherServiceDAO {

    /**
     * 按照条件查找作业
     * @param course
     * @param chapter
     * @return
     */
    ArrayList<String> findHomework(String course, String chapter);

    /**
     * 添加一个新的作业
     * @return
     */
    boolean addNewHomework();

    /**
     * 修改一个已经布置的作业
     * @param id
     * @return
     */
    boolean editHomework(String id);

    /**
     * 展示当前已经布置的作业
     * @return
     */
    ArrayList<String> showCurrentHomework();

    /**
     * 删除一个作业
     * @param id
     * @return
     */
    boolean deleteHomework(String id);

    /**
     * 详细查看一个作业的具体内容
     * @param id
     * @return
     */
    ArrayList<String> showHomeworkDetails(String id);

    /**
     * 对学生的一个作业进行打分
     * @param id
     * @param student_id
     * @param score
     * @param remark
     * @return
     */
    boolean remarkHomework(String id, String student_id, String score, String remark);

    /**
     * 显示一个作业中已经提交的作业
     * @param id
     * @return
     */
    ArrayList<String> showSubmitHomework(String id);

    /**
     * 查看已经批改的作业的详细信息
     * @param id
     * @param student_id
     * @return
     */
    ArrayList<String> showSubmitHomeworkDetail(String id, String student_id);
}

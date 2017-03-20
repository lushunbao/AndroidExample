package com.study.lusb1.mysinablog.model;

import java.util.Map;

/**
 * Created by lushunbao on 2017/3/7.
 */

public class Task {
    //task id
    private int taskId;
    //task params
    private Map<String,Object> taskParams;

    //auth task id
    public static final int WEIBO_AUTH = 1;
    public static final int READ_USER_INFO = 2;

    public Task(int taskId,Map<String,Object> taskParams){
        this.taskId = taskId;
        this.taskParams = taskParams;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Map<String, Object> getTaskParams() {
        return taskParams;
    }

    public void setTaskParams(Map<String, Object> taskParams) {
        this.taskParams = taskParams;
    }
}

package org.fatmansoft.teach.service;

import org.fatmansoft.teach.models.Achievement;
import org.fatmansoft.teach.models.Activity;
import org.fatmansoft.teach.models.Innovation;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.repository.AchievementRepository;
import org.fatmansoft.teach.repository.ActivityRepository;
import org.fatmansoft.teach.repository.InnovationRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IntroduceService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private InnovationRepository innovationRepository;
    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private ActivityRepository activityRepository;
    //个人简历信息数据准备方法  请同学修改这个方法，请根据自己的数据的希望展示的内容拼接成字符串，放在Map对象里， attachList 可以方多段内容，具体内容有个人决定
    public Map getIntroduceDataMap(){
        Map data = new HashMap();
        data.put("myName", "张三");   // 学生信息
        data.put("overview","本人.");  //学生基本信息综述
        List attachList = new ArrayList();
        Map m;
        m = new HashMap();
        m.put("title","学习成绩");   //
        m.put("content","成绩...");  // 学生成绩综述
        attachList.add(m);
        m = new HashMap();
        m.put("title","社会实践");
        m.put("content","社会实践...");  // 社会实践综述
        attachList.add(m);
        data.put("attachList",attachList);
        return data;
    }

    public Map getIntroduceDataMap(String studentNum){
        if(studentNum == null){
            Map data = new HashMap();
            List attachList = new ArrayList();
            Map m;
            m = new HashMap();
            data.put("myName", "Welcome");
            data.put("overview", "欢迎使用山东大学学生管理系统");
            m.put("title", "简介");
            m.put("content", "本系统主要采用 C/S 结构，从学生入学到毕业每个人在校的信息，以及成绩的管理。本系统主要分为七大模块，基本信息管理、成绩管理、课程中心、创新实践管理、荣誉管理、日常活动管理、日志信息管理。用户可以对各种信息进行录入，修改，删除等操作。更方便各个专业老师对学生信息的系统化管理，提高办公效率，以达到更合理化，方便化等要求。");
            attachList.add(m);
            data.put("attachList",attachList);
            return data;
        }
        List<Student> sList = studentRepository.findStudentListByNumName(studentNum);
        List<Innovation> iList = innovationRepository.findInnovationListByStudentNum(studentNum);
        List<Achievement> aList = achievementRepository.findAchievementListByStudentNum(studentNum);
        List<Activity> acList = activityRepository.findActivityListByStudentNum(studentNum);
        Map data = new HashMap();
        if(sList == null)return data;
        Student s = sList.get(0);
        data.put("myName", s.getStudentName());   // 学生信息
        // 目前设想，将用户画像集成到学生基本信息综述
        List<String> overview = new ArrayList<String>();
        if(Double.parseDouble(s.getGPA()) >= 4.0)overview.add("学习成绩优良");
        if(3 > iList.size() && iList.size() >= 1)overview.add("有过若干次创新实践活动");
        else if(iList.size() >= 3)overview.add("曾多次进行创新实践活动");
        if(3 > aList.size() && aList.size() >= 1)overview.add("获得过数项荣誉");
        else if(aList.size() >= 3)overview.add("获有许多荣誉");
        if(5 > acList.size() && acList.size() >= 1)overview.add("数次参与校园活动");
        else if(acList.size() >= 5)overview.add("多次积极参与校园活动");
        String ov = "";
        if(overview.size() > 0) {
            ov = overview.get(0);
            for(int i = 1; i < overview.size(); ++i)ov += ", " + overview.get(i);
        }else{
            ov = "日积月累，稳步前进";
        }
        data.put("overview", ov);  //学生基本信息综述
        List attachList = new ArrayList();
        Map m;
        m = new HashMap();
        m.put("title","学习成绩");
        m.put("content","绩点： " + s.getGPA());  // 学生成绩综述
        attachList.add(m);
        m = new HashMap();
        m.put("title","创新实践记录");
        String inno = "";
        for(Innovation i: iList){
            inno += i.getInnoName() + ";\n";
        }
        if(inno.equals("")){inno = "暂无";}
        m.put("content", inno);
        attachList.add(m);
        m = new HashMap();
        m.put("title","荣誉记录");
        String honor = "";
        for(Achievement a: aList){
            honor += a.getTitle() + ";\n";
        }
        if(honor.equals("")){honor = "暂无";}
        m.put("content", honor);
        attachList.add(m);
        data.put("attachList",attachList);
        return data;
    }
}

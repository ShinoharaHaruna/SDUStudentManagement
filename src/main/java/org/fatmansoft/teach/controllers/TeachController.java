package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.service.IntroduceService;
import org.fatmansoft.teach.util.CommonMethod;
import org.fatmansoft.teach.util.DateTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

// origins： 允许可访问的域列表
// maxAge:准备响应前的缓存持续的最大时间（以秒为单位）。
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class TeachController {
    //Java 对象的注入 我们定义的这下Java的操作对象都不能自己管理是由有Spring框架来管理的， TeachController 中要使用StudentRepository接口的实现类对象，
    // 需要下列方式注入，否则无法使用， studentRepository 相当于StudentRepository接口实现对象的一个引用，由框架完成对这个引用的复制，
    // TeachController中的方法可以直接使用
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private IntroduceService introduceService;
    // Update @ 2022/3/8 14:17
    // 加 achievement
    @Autowired
    private AchievementRepository achievementRepository;

    // 加入课程中心
    @Autowired
    private CourseRepository courseRepository;

    // 加入创新中心
    @Autowired
    private InnovationRepository innovationRepository;

    // 活动中心
    @Autowired
    private ActivityRepository activityRepository;

    // 日志
    @Autowired
    private LogRepository logRepository;


    //getStudentMapList 查询所有学号或姓名与numName相匹配的学生信息，并转换成Map的数据格式存放到List
    //
    // Map 对象是存储数据的集合类，框架会自动将Map转换程用于前后台传输数据的Json对象，Map的嵌套结构和Json的嵌套结构类似，
    //下面方法是生成前端Table数据的示例，List的每一个Map对用显示表中一行的数据
    //Map 每个键值对，对应每一个列的值，如m.put("studentNum",s.getStudentNum())， studentNum这一列显示的是具体的学号的值
    //按照我们测试框架的要求，每个表的主键都是id, 生成表数据是一定要用m.put("id", s.getId());将id传送前端，前端不显示，
    //但在进入编辑页面是作为参数回传到后台.
    public List getStudentMapList(String numName) {
        List dataList = new ArrayList();
        List<Student> sList = studentRepository.findStudentListByNumName(numName);  //数据库查询操作
        if(sList == null || sList.size() == 0)
            return dataList;
        Student s;
        Map m;
        for(int i = 0; i < sList.size();i++) {
            s = sList.get(i);
            m = new HashMap();
            m.put("id", s.getId());
            m.put("studentNum",s.getStudentNum());
//            String studentNameLink = "/model=student&studentId=" + s.getId() + "&studentName=" + s.getStudentName();
            m.put("studentName",s.getStudentName());
//            m.put("studentName", studentNameLink);
            if("1".equals(s.getSex())) {    //数据库存的是编码，显示是名称

                m.put("sex","男");
            }else {
                m.put("sex","女");
            }
            m.put("age",s.getAge());
            m.put("birthday", DateTimeTool.parseDateTime(s.getBirthday(),"yyyy-MM-dd"));  //时间格式转换字符串
            // Update @ 2022/4/29 16:48
            // 加入院系
            m.put("dept", s.getDept());

            // Update @ 2022/4/29 11:32

            // 在新的设计中，联系电话和 GPA 将不再显示在页面中
            // 再更新，联系电话还是要的
            m.put("phone", s.getPhone());
//            // 测试 GPA 数据
//            // 为了输出保留两位小数，虽然写的有点麻烦但是能跑就行
//            if (s.getGPA() == 0)m.put("GPA", "0.00");
//            else m.put("GPA", (double)((int)(s.getGPA() * 100)) / 100);
//            // 测试结束
            dataList.add(m);
        }
        return dataList;
    }
    //student页面初始化方法
    //Table界面初始是请求列表的数据，这里缺省查出所有学生的信息，传递字符“”给方法getStudentMapList，返回所有学生数据，
    @PostMapping("/studentInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse studentInit(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = getStudentMapList("");
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    //student页面点击查询按钮请求
    //Table界面初始是请求列表的数据，从请求对象里获得前端界面输入的字符串，作为参数传递给方法getStudentMapList，返回所有学生数据，
    @PostMapping("/studentQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse studentQuery(@Valid @RequestBody DataRequest dataRequest) {
        String numName= dataRequest.getString("numName");
        List dataList = getStudentMapList(numName);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    //studentEdit初始化方法
    //studentEdit编辑页面进入时首先请求的一个方法， 如果是Edit,再前台会把对应要编辑的那个学生信息的id作为参数回传给后端，我们通过Integer id = dataRequest.getInteger("id")
    //获得对应学生的id， 根据id从数据库中查出数据，存在Map对象里，并返回前端，如果是添加， 则前端没有id传回，Map 对象数据为空（界面上的数据也为空白）

    // 我认为这里只需要编辑学生的基本信息，因此成绩的改动不在此处。
    @PostMapping("/studentEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse studentEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Student s= null;
        Optional<Student> op;
        if(id != null) {
            op= studentRepository.findById(id);
            if(op.isPresent()) {
                s = op.get();
            }
        }
        Map form = new HashMap();
        if(s != null) {
            form.put("id",s.getId());
            form.put("studentNum",s.getStudentNum());
            form.put("studentName",s.getStudentName());
            form.put("sex",s.getSex());  //这里不需要转换
            form.put("age",s.getAge());
            form.put("birthday", DateTimeTool.parseDateTime(s.getBirthday(),"yyyy-MM-dd")); //这里需要转换为字符串
            form.put("phone", s.getPhone());
            form.put("dept", s.getDept());
        }
        return CommonMethod.getReturnData(form); //这里回传包含学生信息的Map对象
    }
//  学生信息提交按钮方法
    //相应提交请求的方法，前端把所有数据打包成一个Json对象作为参数传回后端，后端直接可以获得对应的Map对象form, 再从form里取出所有属性，复制到
    //实体对象里，保存到数据库里即可，如果是添加一条记录， id 为空，这是先 new Student 计算新的id， 复制相关属性，保存，如果是编辑原来的信息，
    //id 不为空。则查询出实体对象，复制相关属性，保存后修改数据库信息，永久修改

    // 同理，此处不涉及对成绩单的改动
    @PostMapping("/studentEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse studentEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form"); //参数获取Map对象
        Integer id = CommonMethod.getInteger(form,"id");
        String studentNum = CommonMethod.getString(form,"studentNum");  //Map 获取属性的值
        String studentName = CommonMethod.getString(form,"studentName");
        String sex = CommonMethod.getString(form,"sex");
        Integer age = CommonMethod.getInteger(form,"age");
        Date birthday = CommonMethod.getDate(form,"birthday");
        String phone = CommonMethod.getString(form, "phone");
        String dept = CommonMethod.getString(form,"dept");
        Student s= null;
        Optional<Student> op;

        // 不能有信息留空
        if(studentNum == null || studentName == null || sex == null || age == null || birthday == null || phone == null || dept == null)return CommonMethod.getReturnMessageError("信息不全");

        List<Student> sL = studentRepository.findAll();
        if(sL != null){
            for(int i = 0; i < sL.size(); i++){
                if(sL.get(i).getStudentNum().equals(studentNum))return CommonMethod.getReturnMessageError("已存在该学号的学生");
            }
        }

        // Update @ 2022/4/29 11:37
        // 对输入信息进行校验
        // 在年龄校验这里，因为虚岁和周岁的算法不同，所以就不拿来和生日校验了；另外，我相信年龄上限设为 128 岁是合理的
        for(int i = 0; i < studentNum.length(); ++i){
            if(!('0' <= studentNum.charAt(i) && studentNum.charAt(i) <= '9')){
                return CommonMethod.getReturnMessageError("学号格式错误");
            }
        }
        if(age == null)return CommonMethod.getReturnMessageError("年龄输入错误");
        if(age < 0 || age > 128)return CommonMethod.getReturnMessageError("年龄输入错误");
        for(int i = 0; i < phone.length(); ++i){
            if(!('0' <= phone.charAt(i) && phone.charAt(i) <= '9')){
                return CommonMethod.getReturnMessageError("联系电话格式错误");
            }
        }

        if(id != null) {
            op= studentRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s == null) {
            s = new Student();   //不存在 创建实体对象
            id = studentRepository.getMaxId();  // 查询最大的id
            if(id == null)
                id = 1;
            else
                id = id+1;
            s.setId(id);  //设置新的id
        }
        s.setStudentNum(studentNum);  //设置属性
        s.setStudentName(studentName);
        s.setSex(sex);
        s.setAge(age);
        s.setBirthday(birthday);
        s.setPhone(phone);
        s.setDept(dept);
        studentRepository.save(s);  //新建和修改都调用save方法
        return CommonMethod.getReturnData(s.getId());  // 将记录的id返回前端
    }

    //  学生信息删除方法
    //Student页面的列表里点击删除按钮则可以删除已经存在的学生信息， 前端会将该记录的id 回传到后端，方法从参数获取id，查出相关记录，调用delete方法删除
    @PostMapping("/studentDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse studentDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");  //获取id值
        Student s= null;
        Optional<Student> op;
        if(id != null) {
            op= studentRepository.findById(id);   //查询获得实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        // 删除该学生前，还需要在创新、*荣誉*、日常、日志中删除他的信息
        List<Innovation> innoList = innovationRepository.findInnovationListByStudentNum(s.getStudentNum());
        if (innoList != null) {
            for(Innovation i: innoList){
                innovationRepository.delete(i);
            }
        }
        List<Achievement> acList = achievementRepository.findAchievementListByStudentNum(s.getStudentNum());
        if (acList != null) {
            for(Achievement i: acList){
                achievementRepository.delete(i);
            }
        }
        List<Activity> actList = activityRepository.findActivityListByStudentNum(s.getStudentNum());
        if (actList != null) {
            for(Activity i: actList){
                activityRepository.delete(i);
            }
        }
        List<Log> logList = logRepository.findLogListByStudentNum(s.getStudentNum());
        if (logList != null) {
            for(Log i: logList){
                logRepository.delete(i);
            }
        }
        if(s != null) {
            studentRepository.delete(s);    //数据库永久删除
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }

    // 成绩管理（已完成）
    public List getGradeMapList(String numName) {
        List dataList = new ArrayList();
        List<Student> sList = studentRepository.findStudentListByNumName(numName);  //数据库查询操作
        if(sList == null || sList.size() == 0)
            return dataList;
        Student s;
        Map m;
        for(int i = 0; i < sList.size();i++) {
            s = sList.get(i);
            m = new HashMap();
            GradeList glist = new GradeList(s.getGrade());
            for (int j = 0; j < glist.size(); ++j) {
                m.put("courseName", glist.get(i).getCourseName());
                m.put("credit", glist.get(i).getCredit());
                m.put("grade", glist.get(i).getGrade());
                m.put("absence", glist.get(i).getAbsence());
                dataList.add(m);
            }
        }
        return dataList;
    }
    @PostMapping("/gradeInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse gradeInit(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = new ArrayList();
        List<Student> sList = studentRepository.findStudentListByNumName("");  //数据库查询操作
        if(sList == null || sList.size() == 0)
            return CommonMethod.getReturnData(dataList);
        Student s;
        Map m;
        for(int i = 0; i < sList.size();i++) {
            s = sList.get(i);
            m = new HashMap();
            m.put("id", s.getId());
            m.put("studentNum",s.getStudentNum());
            m.put("studentName",s.getStudentName());
            m.put("GPA", s.getGPA());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/gradeEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse gradeEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Student s= null;
        Optional<Student> op;
        if(id != null) {
            op= studentRepository.findById(id);
            if(op.isPresent()) {
                s = op.get();
            }
        }
        List dataList = getGradeMapList(s.getStudentNum());
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    @PostMapping("/gradeEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse gradeEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        String courseName = CommonMethod.getString(form,"courseName");
        Double credit = CommonMethod.getDouble(form,"credit");
        Double grade = CommonMethod.getDouble(form,"grade");
        Integer absence = CommonMethod.getInteger(form,"absence");
        Student s = null;
        Optional<Student> op;
        if(id != null) {
            op= studentRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s == null)return CommonMethod.getReturnMessageError("未找到");
        GradeList glist = new GradeList(s.getGrade());
        Boolean flag = true;    // true 表示是新的课程
        if(glist != null){
            for(int i = 0; i < glist.size(); ++i){
                if(glist.get(i).getCourseName().equals(courseName)){
                    flag = false;
                    glist.get(i).setGrade(grade);
                    glist.get(i).setAbsence(absence);
                    glist.get(i).setCredit(credit);
                }
            }
        }
        if(flag){
            glist.add(new Grade(courseName, credit, grade, absence));
        }
        s.setGrade(glist.toString());
        studentRepository.save(s);
        return CommonMethod.getReturnData(s.getId());  // 将记录的id返回前端
    }


    @PostMapping("/gradeQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse gradeQuery(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = new ArrayList();
        List<Student> sList = studentRepository.findStudentListByNumName(dataRequest.getString("numName"));  //数据库查询操作
        if(sList == null || sList.size() == 0)
            return CommonMethod.getReturnData(dataList);
        Student s;
        Map m;
        for(int i = 0; i < sList.size();i++) {
            s = sList.get(i);
            m = new HashMap();
            m.put("id", s.getId());
            m.put("studentNum",s.getStudentNum());
            m.put("studentName",s.getStudentName());
            m.put("GPA", s.getGPA());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }
    // 成绩管理（已完成）



    // 课程中心（已完成）
    public List getCourseMapList(String CourseNum) {
        List dataList = new ArrayList();
        List<Course> sList = courseRepository.findCourseListByNumName(CourseNum);  //数据库查询操作
        if(sList == null || sList.size() == 0) return dataList;
        Course s;
        Map m;
        for(int i = 0; i < sList.size();i++) {
            s = sList.get(i);
            m = new HashMap();
            m.put("id", s.getId());
            m.put("courseNum",s.getCourseNum());
            m.put("courseName",s.getCourseName());
            m.put("courseReged",s.getCourseReged());
            m.put("courseCapacity",s.getCourseCapacity());
            dataList.add(m);
        }
        return dataList;
    }
    @PostMapping("/courseInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseInit(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = getCourseMapList("");
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    @PostMapping("/courseQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseQuery(@Valid @RequestBody DataRequest dataRequest) {
        String CourseNum= dataRequest.getString("numName");
        List dataList = getCourseMapList(CourseNum);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    @PostMapping("/courseEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Course s= null;
        Optional<Course> op;
        if(id != null) {
            op= courseRepository.findById(id);
            if(op.isPresent()) {
                s = op.get();
            }
        }
        Map form = new HashMap();
        if(s != null) {
            form.put("id",s.getId());
            form.put("courseNum",s.getCourseNum());
            form.put("courseName",s.getCourseName());
            form.put("courseCapacity",s.getCourseCapacity());
            form.put("courseReged", s.getCourseReged());
        }
        return CommonMethod.getReturnData(form);
    }
    @PostMapping("/courseEditSubmit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        String courseNum = CommonMethod.getString(form,"courseNum");
        String courseName = CommonMethod.getString(form,"courseName");
        Integer courseCapacity = CommonMethod.getInteger(form, "courseCapacity");
        Integer courseReged = CommonMethod.getInteger(form, "courseReged");
        Course s= null;
        Optional<Course> op;

        // Update @ 2022/4/30 15:23
        // 对输入信息进行校验
        // 这里认为课程号只能由数字构成
        for(int i = 0; i < courseNum.length(); ++i){
            if(!('0' <= courseNum.charAt(i) && courseNum.charAt(i) <= '9')){
                return CommonMethod.getReturnMessageError("课程号格式错误");
            }
        }
        if(courseCapacity == null)return CommonMethod.getReturnMessageError("课程容量输入错误");
        if(courseReged == null)return CommonMethod.getReturnMessageError("选课人数输入错误");
        if(courseCapacity < courseReged)return CommonMethod.getReturnMessageError("选课人数错误");

        if(id != null) {
            op= courseRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s == null) {
            s = new Course();   //不存在 创建实体对象
            id = courseRepository.getMaxId();  // 查询最大的id
            if(id == null)
                id = 1;
            else
                id = id+1;
            s.setId(id);  //设置新的id
        }
        s.setCourseNum(courseNum);  //设置属性
        s.setCourseName(courseName);
        s.setCourseCapacity(courseCapacity);
        s.setCourseReged(courseReged);
        courseRepository.save(s);  //新建和修改都调用save方法
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/courseDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse courseDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");  //获取id值
        Course s= null;
        Optional<Course> op;
        if(id != null) {
            op= courseRepository.findById(id);   //查询获得实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s != null) {
            courseRepository.delete(s);    //数据库永久删除
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }
    // 课程中心（已完成）

    // 荣誉中心（已完成）
    // 根据更改的前端需要，重写荣誉系统
    // Update @ 2022/3/9 20：01
    // 荣誉查询界面
//    @PostMapping("/achievementQuery")
//    @PreAuthorize(" hasRole('ADMIN')")
//    public DataResponse achievementQuery(@Valid @RequestBody DataRequest dataRequest) {
//        Integer id = dataRequest.getInteger("id");
//        System.out.println(id);
//        Achievement a = null;
//        Optional<Achievement> op;
//        if (id != null) {
//            op = achievementRepository.findById(id);
//            if (op.isPresent()) {
//                a = op.get();
//            }
//        }
//        Map form = new HashMap();
//        if (a != null) {
//            form.put("id", a.getId());
////            form.put("studentNum",s.getStudentNum());
//            form.put("title", a.getTitle());
//        }
//        return CommonMethod.getReturnData(form); //这里回传包含荣誉信息的Map对象
//    }
    // 根据学号来查询荣誉成绩
    public List getAchievementMapList(String studentNum) {
        List dataList = new ArrayList();
        List<Achievement> sList = achievementRepository.findAchievementListByStudentNum(studentNum);
        if(sList == null || sList.size() == 0)
            return dataList;
        List<Student> sL = studentRepository.findStudentListByNumName(studentNum);
        Achievement s;
        Map m;
        for(int i = 0; i < sList.size();i++) {
            s = sList.get(i);
            m = new HashMap();
            m.put("id", s.getId());
            m.put("studentNum",s.getStudentNum());
            m.put("studentName",sL.get(i).getStudentName());
            m.put("title",s.getTitle());
            dataList.add(m);
        }
        return dataList;
    }

    @PostMapping("/achievementInit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse achievementInit(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = getAchievementMapList("");
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }

    @PostMapping("/achievementQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse achievementQuery(@Valid @RequestBody DataRequest dataRequest) {
        String studentNum= dataRequest.getString("studentNum");
        List dataList = getAchievementMapList(studentNum);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    @PostMapping("/achievementEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse achievementEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Achievement s= null;
        Optional<Achievement> op;
        if(id != null) {
            op= achievementRepository.findById(id);
            if(op.isPresent()) {
                s = op.get();
            }
        }
        Map form = new HashMap();
        if(s == null){
            form.put("id","");
            form.put("studentNum","");
            form.put("title","");
        }else{
            List<Student> sL = studentRepository.findStudentListByNumName(s.getStudentNum());
            if(s != null) {
                form.put("id",s.getId());
                form.put("studentNum",s.getStudentNum());
                form.put("title",s.getTitle());
            }
        }
        return CommonMethod.getReturnData(form);
    }
    @PostMapping("/achievementEditSubmit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse achievementSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        String studentNum = CommonMethod.getString(form,"studentNum");
        String title = CommonMethod.getString(form,"title");
        Achievement s= null;
        Optional<Achievement> op;

        // 不能有信息留空
        if(studentNum == null || title == null) return CommonMethod.getReturnMessageError("不能有信息留空");
        if(studentNum.equals("") || title.equals("")) return CommonMethod.getReturnMessageError("不能有信息留空");

        for(int i = 0; i < studentNum.length(); ++i){
            if(!('0' <= studentNum.charAt(i) && studentNum.charAt(i) <= '9')){
                return CommonMethod.getReturnMessageError("学号格式错误");
            }
        }

        List<Student> t = studentRepository.findStudentListByNumName(studentNum);
        if(t.size() == 0) return CommonMethod.getReturnMessageError("未找到");
//        String studentName = t.get(0).getStudentName();
//        List<Student> sL = studentRepository.findAll();
//        Boolean existQ = false;  // 表示这个学生不存在
//        for(int i = 0; i < sL.size(); ++i){
//            String n = sL.get(i).getStudentName();
//            if(n.equals(studentName)){
//                existQ = true;
//                break;
//            }
//        }
//        if(!existQ)return CommonMethod.getReturnMessageError("该学生不存在");

        if(id != null) {
            op= achievementRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s == null) {
            s = new Achievement();   //不存在 创建实体对象
            id = achievementRepository.getMaxId();  // 查询最大的id
            if(id == null)
                id = 1;
            else
                id = id+1;
            s.setId(id);  //设置新的id
        }
        s.setStudentNum(studentNum);  //设置属性
        s.setTitle(title);
        achievementRepository.save(s);  //新建和修改都调用save方法
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/achievementDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse achievementDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");  //获取id值
        Achievement s= null;
        Optional<Achievement> op;
        if(id != null) {
            op= achievementRepository.findById(id);   //查询获得实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s != null) {
            achievementRepository.delete(s);    //数据库永久删除
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }
    // 荣誉中心

    // 创新中心（已完成）
    public String innoTypeConvert(Integer typeNum){
        switch(typeNum){
            case 1: return "社会实践";
            case 2: return "学科竞赛";
            case 3: return "科技成果";
            case 4: return "培训讲座";
            case 5: return "创新项目";
            case 6: return "校外实习";
        }
        return "";
    }
    public Integer innoTypeConvert(String innoType){
        if(innoType.equals("社会实践"))return 1;
        if(innoType.equals("学科竞赛"))return 2;
        if(innoType.equals("科技成果"))return 3;
        if(innoType.equals("培训讲座"))return 4;
        if(innoType.equals("创新项目"))return 5;
        if(innoType.equals("校外实习"))return 6;
        return -1;
    }

    public List getInnovationMapList(String studentNum) {
        List dataList = new ArrayList();
        List<Innovation> iList = innovationRepository.findInnovationListByStudentNum(studentNum);  //数据库查询操作
        List<Student> sList = studentRepository.findAll();
        if(sList == null)return dataList;
        if(iList == null || iList.size() == 0) return dataList;
        Innovation s;
        Map m;
        for(int i = 0; i < iList.size();i++) {
            s = iList.get(i);
            m = new HashMap();
            m.put("id", s.getId());
            m.put("studentNum",s.getStudentNum());
            m.put("innoType",innoTypeConvert(s.getInnoType()));
            for(int j = 0; j < sList.size(); ++j) {
                if (sList.get(j).getStudentNum().equals(s.getStudentNum())) {
                    m.put("studentName", sList.get(j).getStudentName());
                    break;
                }
            }
            m.put("innoName", s.getInnoName());
            dataList.add(m);
        }
        return dataList;
    }
    @PostMapping("/innovationInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse innovationInit(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = getInnovationMapList("");
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    @PostMapping("/innovationQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse innovationQuery(@Valid @RequestBody DataRequest dataRequest) {
        String studentNum= dataRequest.getString("studentNum");
        List dataList = getInnovationMapList(studentNum);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    @PostMapping("/innovationEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse innovationEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Innovation s= null;
        Optional<Innovation> op;
        if(id != null) {
            op= innovationRepository.findById(id);
            if(op.isPresent()) {
                s = op.get();
            }
        }
        Map form = new HashMap();
        if(s != null) {
            form.put("id",s.getId());
            form.put("studentNum",s.getStudentNum());
            form.put("innoName",s.getInnoName());
            form.put("innoType",innoTypeConvert(s.getInnoType()));
            form.put("innoDate", DateTimeTool.parseDateTime(s.getInnoDate(),"yyyy-MM-dd"));
        }
        return CommonMethod.getReturnData(form);
    }
    @PostMapping("/innovationEditSubmit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse innovationSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        String studentNum = CommonMethod.getString(form,"studentNum");
        Integer innoType = CommonMethod.getInteger(form, "innoType");
        String innoName = CommonMethod.getString(form, "innoName");
        Date innoDate = CommonMethod.getDate(form, "innoDate");
        Innovation s= null;
        Optional<Innovation> op;

        // 不能有信息留空
        if(studentNum == null || innoType == null || innoName == null || innoDate == null)return CommonMethod.getReturnMessageError("不能有信息留空");

        // 校验部分
        for(int i = 0; i < studentNum.length(); ++i){
            if(!('0' <= studentNum.charAt(i) && studentNum.charAt(i) <= '9')){
                return CommonMethod.getReturnMessageError("学号错误");
            }
        }
        Boolean existQ = false;
        List<Student> sL = studentRepository.findAll();
        for(int i = 0; i < sL.size(); ++i){
            if(sL.get(i).getStudentNum().equals(studentNum)) {
                existQ = true;
                break;
            }
        }
        if(!existQ)return CommonMethod.getReturnMessageError("不存在该学生");

        if(id != null) {
            op= innovationRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s == null) {
            s = new Innovation();   //不存在 创建实体对象
            id = innovationRepository.getMaxId();  // 查询最大的id
            if(id == null)
                id = 1;
            else
                id = id+1;
            s.setId(id);  //设置新的id
        }
        s.setStudentNum(studentNum);  //设置属性
        s.setInnoType(innoType);
        s.setInnoDate(innoDate);
        s.setInnoName(innoName);
        innovationRepository.save(s);  //新建和修改都调用save方法
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/innovationDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse innovationDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");  //获取id值
        Innovation s= null;
        Optional<Innovation> op;
        if(id != null) {
            op= innovationRepository.findById(id);   //查询获得实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s != null) {
            innovationRepository.delete(s);    //数据库永久删除
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }
    // 创新中心

    // 日常活动（已完成）
    public String activityTypeConvert(Integer typeNum){
        if(typeNum == null)return "";
        switch(typeNum){
            case 1: return "体育活动";
            case 2: return "旅游";
            case 3: return "文艺演出";
            case 4: return "聚会";
            default: return "";
        }
    }
    public Integer activityTypeConvert(String activityType){
        if(activityType.equals("体育活动"))return 1;
        if(activityType.equals("旅游"))return 2;
        if(activityType.equals("文艺演出"))return 3;
        if(activityType.equals("聚会"))return 4;
        return -1;
    }
    public List getActivityMapList(String studentNum) {
        List dataList = new ArrayList();
        List<Activity> iList = activityRepository.findActivityListByStudentNum(studentNum);  //数据库查询操作
        List<Student> sList = studentRepository.findAll();
        if(sList == null)return dataList;
        if(iList == null || iList.size() == 0) return dataList;
        Activity s;
        Map m;
        for(int i = 0; i < iList.size();i++) {
            s = iList.get(i);
            m = new HashMap();
            m.put("id", s.getId());
            m.put("studentNum",s.getStudentNum());
            m.put("acType",activityTypeConvert(s.getAcType()));
            for(int j = 0; j < sList.size(); ++j) {
                if (sList.get(j).getStudentNum().equals(s.getStudentNum())) {
                    m.put("studentName", sList.get(j).getStudentName());
                    break;
                }
            }
            m.put("acDate", DateTimeTool.parseDateTime(s.getAcDate(),"yyyy-MM-dd"));
            m.put("acName", s.getAcName());
            dataList.add(m);
        }
        return dataList;
    }
    @PostMapping("/activityInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse activityInit(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = getActivityMapList("");
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    @PostMapping("/activityQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse activityQuery(@Valid @RequestBody DataRequest dataRequest) {
        String studentNum= dataRequest.getString("studentNum");
        List dataList = getActivityMapList(studentNum);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    @PostMapping("/activityEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse activityEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Activity s= null;
        Optional<Activity> op;
        if(id != null) {
            op= activityRepository.findById(id);
            if(op.isPresent()) {
                s = op.get();
            }
        }
        Map form = new HashMap();
        if(s != null) {
            form.put("id",s.getId());
            form.put("studentNum",s.getStudentNum());
            form.put("acName",s.getAcName());
            form.put("acType",activityTypeConvert(s.getAcType()));
            form.put("acDate", DateTimeTool.parseDateTime(s.getAcDate(),"yyyy-MM-dd"));
        }
        return CommonMethod.getReturnData(form);
    }
    @PostMapping("/activityEditSubmit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse activitySubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        String studentNum = CommonMethod.getString(form,"studentNum");
        Integer acType = CommonMethod.getInteger(form, "acType");
        String acName = CommonMethod.getString(form, "acName");
        Date acDate = CommonMethod.getDate(form, "acDate");
        Activity s= null;
        Optional<Activity> op;

        // 不能有信息留空
        if(studentNum == null || acType == null || acName == null || acDate == null)return CommonMethod.getReturnMessageError("不能有信息留空");

        // 校验部分
        for(int i = 0; i < studentNum.length(); ++i){
            if(!('0' <= studentNum.charAt(i) && studentNum.charAt(i) <= '9')){
                return CommonMethod.getReturnMessageError("学号错误");
            }
        }
//        Boolean existQ = false;
//        List<Student> sL = studentRepository.findAll();
//        for(int i = 0; i < sL.size(); ++i){
//            if(sL.get(i).getStudentNum().equals(studentNum)) {
//                existQ = true;
//                break;
//            }
//        }
//        if(!existQ)return CommonMethod.getReturnMessageError("不存在该学生");

        if(id != null) {
            op= activityRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s == null) {
            s = new Activity();   //不存在 创建实体对象
            id = activityRepository.getMaxId();  // 查询最大的id
            if(id == null)
                id = 1;
            else
                id = id+1;
            s.setId(id);  //设置新的id
        }
        s.setStudentNum(studentNum);  //设置属性
        s.setAcType(acType);
        s.setAcDate(acDate);
        s.setAcName(acName);
        activityRepository.save(s);  //新建和修改都调用save方法
        return CommonMethod.getReturnMessageOK();
    }
    @PostMapping("/activityDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse activityDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");  //获取id值
        Activity s= null;
        Optional<Activity> op;
        if(id != null) {
            op= activityRepository.findById(id);   //查询获得实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s != null) {
            activityRepository.delete(s);    //数据库永久删除
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }
    // 日常

    // 日志系统（已完成）
    public String logTypeConvert(Integer typeNum){
        switch(typeNum){
            case 1: return "外出请假";
            case 2: return "消费流水";
            case 3: return "场地、教室申请";
            case 4: return "其他";
        }
        return "";
    }
    public Integer LogTypeConvert(String logType){
        if(logType.equals("外出请假"))return 1;
        if(logType.equals("消费流水"))return 2;
        if(logType.equals("场地、教室申请"))return 3;
        if(logType.equals("其他"))return 4;
        return -1;
    }
    public List getLogMapList(String studentNum) {
        List dataList = new ArrayList();
        List<Log> iList = logRepository.findLogListByStudentNum(studentNum);  //数据库查询操作
        List<Student> sList = studentRepository.findAll();
        if(sList == null)return dataList;
        if(iList == null || iList.size() == 0) return dataList;
        Log s;
        Map m;
        for(int i = 0; i < iList.size();i++) {
            s = iList.get(i);
            m = new HashMap();
            m.put("id", s.getId());
            m.put("studentNum",s.getStudentNum());
            m.put("logType",logTypeConvert(s.getLogType()));
            for(int j = 0; j < sList.size(); ++j) {
                if (sList.get(j).getStudentNum().equals(s.getStudentNum())) {
                    m.put("studentName", sList.get(j).getStudentName());
                    break;
                }
            }
            m.put("logDetail", s.getLogDetail());
            dataList.add(m);
        }
        return dataList;
    }
    @PostMapping("/logInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse logInit(@Valid @RequestBody DataRequest dataRequest) {
        List dataList = getLogMapList("");
//        System.out.println(dataList.size());
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    @PostMapping("/logQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse logQuery(@Valid @RequestBody DataRequest dataRequest) {
        String studentNum= dataRequest.getString("studentNum");
        List dataList = getLogMapList(studentNum);
        return CommonMethod.getReturnData(dataList);  //按照测试框架规范会送Map的list
    }
    @PostMapping("/logEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse LogEditInit(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Log s= null;
        Optional<Log> op;
        if(id != null) {
            op= logRepository.findById(id);
            if(op.isPresent()) {
                s = op.get();
            }
        }
        Map form = new HashMap();
        if(s != null) {
            form.put("id",s.getId());
            form.put("studentNum",s.getStudentNum());
            form.put("logDetail",s.getLogDetail());
            form.put("logType",logTypeConvert(s.getLogType()));
        }
        return CommonMethod.getReturnData(form);
    }
    @PostMapping("/logEditSubmit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse logSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Map form = dataRequest.getMap("form");
        Integer id = CommonMethod.getInteger(form,"id");
        String studentNum = CommonMethod.getString(form,"studentNum");
        Integer logType = CommonMethod.getInteger(form, "logType");
        String logDetail = CommonMethod.getString(form, "logDetail");
        Log s= null;
        Optional<Log> op;

        // 不能有信息留空
        if(studentNum == null || logType == null || logDetail == null)return CommonMethod.getReturnMessageError("不能有信息留空");
        if(studentNum.equals("") || logDetail.equals(""))return CommonMethod.getReturnMessageError("不能有信息留空");

        // 校验部分
        for(int i = 0; i < studentNum.length(); ++i){
            if(!('0' <= studentNum.charAt(i) && studentNum.charAt(i) <= '9')){
                return CommonMethod.getReturnMessageError("学号错误");
            }
        }
        Boolean existQ = false;
        List<Student> sL = studentRepository.findAll();
        for(int i = 0; i < sL.size(); ++i){
            if(sL.get(i).getStudentNum().equals(studentNum)) {
                existQ = true;
                break;
            }
        }
        if(!existQ)return CommonMethod.getReturnMessageError("不存在该学生");

        if(id != null) {
            op= logRepository.findById(id);  //查询对应数据库中主键为id的值的实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s == null) {
            s = new Log();   //不存在 创建实体对象
            id = logRepository.getMaxId();  // 查询最大的id
            if(id == null)
                id = 1;
            else
                id = id+1;
            s.setId(id);  //设置新的id
        }
        s.setStudentNum(studentNum);  //设置属性
        s.setLogType(logType);
        s.setLogDetail(logDetail);
        logRepository.save(s);  //新建和修改都调用save方法
        System.out.println("#1086 done");
        return CommonMethod.getReturnMessageOK();
    }
    @PostMapping("/logDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse logDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");  //获取id值
        Log s= null;
        Optional<Log> op;
        if(id != null) {
            op= logRepository.findById(id);   //查询获得实体对象
            if(op.isPresent()) {
                s = op.get();
            }
        }
        if(s != null) {
            logRepository.delete(s);    //数据库永久删除
        }
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }
    // 日志













































    //  学生个人简历页面
    //在系统在主界面内点击个人简历，后台准备个人简历所需要的各类数据组成的段落数据，在前端显示
    @PostMapping("/getStudentIntroduceData")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse getStudentIntroduceData(@Valid @RequestBody DataRequest dataRequest) {
        String studentNum= dataRequest.getString("studentNum");
        Map data = introduceService.getIntroduceDataMap(studentNum);
        return CommonMethod.getReturnData(data);  //返回前端个人简历数据
    }
}

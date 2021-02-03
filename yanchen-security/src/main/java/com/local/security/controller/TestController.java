package com.local.security.controller;

import com.local.common.entity.ResultResponse;
import com.local.security.entity.Student;
import com.local.security.entity.StudentDetail;
import com.local.security.entity.Teacher;
import com.local.security.repository.StudentDetailRepository;
import com.local.security.repository.StudentRepository;
import com.local.security.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @Create-By: yanchen 2021/1/23 12:17
 * @Description: TODO
 */
@RestController
public class TestController {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentDetailRepository studentDetailRepository;

    @GetMapping(value = "/saveTeacher")
    public ResultResponse saveTeacher(String name){
        Teacher teacher=new Teacher();
        teacher.setName(name);
        Teacher save = teacherRepository.save(teacher);
        return ResultResponse.builder().code(0).data(save).build();
    }
    @GetMapping(value = "/saveStudent")
    public  ResultResponse saveStudent(String tid,String name,String email,String phone){
        Student student=new Student();
        student.setAge(10);
        student.setTeacherId(Integer.valueOf(tid));
        Student save = studentRepository.save(student);
        StudentDetail detail=new StudentDetail();
        detail.setStudentId(save.getId());
        detail.setPhone(phone);
        detail.setEmail(email);
        StudentDetail save1 = studentDetailRepository.save(detail);
        return ResultResponse.builder().code(0).data(save.toString()+save1.toString()).build();
    }

    @GetMapping(value = "/findStudent")
    public ResultResponse findStudent(Integer tid){
        List<Student> all = studentRepository.findAll(new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                Join<Object, Object> join = root.join("teacher");
                Predicate id = criteriaBuilder.equal(join.get("id"), tid);
                Join<Student, StudentDetail> studentDetail = root.join("studentDetail");
                Predicate sd=criteriaBuilder.equal(studentDetail.get("studentId"),root.get("id"));
                query.where(id,sd);
                return query.getRestriction();
            }
        });
        return ResultResponse.builder().code(0).data(all).build();
    }
}

package com.example.studentsmanagement.service;

import com.example.studentsmanagement.dao.StudentDao;
import com.example.studentsmanagement.dao.UniversityClassDao;
import com.example.studentsmanagement.exceptions.InvalidUniversityClassException;
import com.example.studentsmanagement.exceptions.StudentEmptyNameException;
import com.example.studentsmanagement.exceptions.StudentNonExistException;
import com.example.studentsmanagement.model.Student;
import com.example.studentsmanagement.model.UniversityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private StudentDao studentDao;
    private UniversityClassDao universityClassDao;

    @Autowired
    public StudentService(StudentDao studentDao, UniversityClassDao universityClassDao) {
        this.studentDao = studentDao;
        this.universityClassDao = universityClassDao;
    }

    public Student addStudent(Student student) {
        if (student.getName().isEmpty()) {
            throw new StudentEmptyNameException("Student name cannot be empty");
        }
        return studentDao.save(student);
    }

    public Student updateStudent(Student student) {
        if (student.getId() == null || !studentDao.existsById(student.getId())) {
            throw new StudentNonExistException("Cannot find student id");
        }
        return studentDao.save(student);
    }

    public Student assignClass(Long studentId, Long classId) {
        if (!studentDao.existsById(studentId)) {
            throw new StudentNonExistException("Cannot find student ID");
        }
        if (!studentDao.existsById(classId)) {
            throw new InvalidUniversityClassException("Cannot find class ID");
        }
        Student student = getStudentById(studentId).get();
        UniversityClass universityClass = universityClassDao.findById(classId).get();

        student.setUniversityClass(universityClass);
        return studentDao.save(student);
    }

    public List<Student> getAllStudents() {
        return (List<Student>) studentDao.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentDao.findById(id);
    }

    public List<Student> getStudentsByName(String name) {
        return studentDao.findByName(name);
    }

//    public List<Student> getStudentsContainName(String name) {
//        return studentMapper.getStudentsContainsStrInName("%" + name + "%");
//    }
}

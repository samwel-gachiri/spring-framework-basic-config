package com.example.demo2.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudent(){
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> optionalStudent = studentRepository.findByEmail(student.getEmail());
        if(optionalStudent.isPresent()){
            throw new IllegalStateException("Email is already present");
        }
        studentRepository.save(student);
        System.out.println(student);
    }

    public void deleteStudent(Long studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        studentOptional.ifPresentOrElse(studentRepository::delete,() -> {
            throw  new IllegalStateException("Student with id "+studentId+" does not exists");
        });

    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        if(name!= null && !name.isBlank()&&!student.getName().equals(name)){
            student.setName(name);
        }
        if(email!=null && !email.isBlank() && !student.getEmail().equals(email)){
            if(studentRepository.findByEmail(email).isPresent()){
                throw new IllegalStateException("Email taken");
            }
            student.setEmail(email);
        }
    }
}

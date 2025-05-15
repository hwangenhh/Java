package BTH2.controller;
import BTH2.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StudentController {

    @GetMapping("/students")
    public String showStudents(Model model) {
        List<Student> students = List.of(
            new Student("SV001", "Nguyen Thi C", "0123456789", "Hanoi"),
            new Student("SV002", "Tran Van A", "0987654321", "HCM"),
            new Student("SV003", "Le Huu Duc", "0909090909", "Da Nang")
        );
        model.addAttribute("students", students);
        return "students";
    }
}

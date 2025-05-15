package BTH3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    // Hiển thị form login
	@GetMapping("/login")
	public String loginForm() {
	    return "login";  
	}

    // Xử lý submit form login
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, Model model) {
	    if ("admin".equals(username) && "123".equals(password)) {
	        model.addAttribute("username", username);
	        return "welcome"; 
	    } else {
	        model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu");
	        return "login";
	    }
	}

}

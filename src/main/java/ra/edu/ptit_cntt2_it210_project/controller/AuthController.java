package ra.edu.ptit_cntt2_it210_project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.ptit_cntt2_it210_project.model.dto.LoginDTO;
import ra.edu.ptit_cntt2_it210_project.model.dto.RegisterDTO;
import ra.edu.ptit_cntt2_it210_project.model.entity.Role;
import ra.edu.ptit_cntt2_it210_project.model.entity.UserProfiles;
import ra.edu.ptit_cntt2_it210_project.model.entity.Users;
import ra.edu.ptit_cntt2_it210_project.service.UserService;
import ra.edu.ptit_cntt2_it210_project.service.UserServiceImpl;
import ra.edu.ptit_cntt2_it210_project.util.PasswordHasher;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("registerDTO", new RegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO, BindingResult result, Model model){
        if(result.hasErrors()){
            return "auth/register";
        }

        Users user = new Users();
        user.setEmail(registerDTO.getEmail());
        user.setPassword(userService.hash(registerDTO.getPassword()));
        user.setRole(Role.STUDENT);
        userService.createUser(user);

        UserProfiles userProfiles = new UserProfiles();
        userProfiles.setFullName(registerDTO.getFullName());
        userProfiles.setPhone(registerDTO.getPhone());
        userProfiles.setEmail(registerDTO.getEmail());
        userProfiles.setUser(user);

        userService.createUserProfile(userProfiles);

        return "redirect:/auth/login";
    }


    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("loginDTO", new LoginDTO());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginDTO") LoginDTO loginDTO, BindingResult result, Model model, RedirectAttributes redirectAttributes, HttpSession session){
        if(result.hasErrors()){
            return "auth/login";
        }

//        if (session.getAttribute("userLogin") != null) {
//            String role = (String) session.getAttribute("role");
//            return role.equals("ADMIN")
//                    ? "redirect:/admin/dashboard"
//                    : role.equals("LECTURER")
//                    ? "redirect:/lecturer/home"
//                    : "redirect:/student/home";
//        }

        if(loginDTO.getEmail().equals("admin123@gmail.com") && loginDTO.getPassword().equals("admin123")){
            session.setAttribute("userLogin", "admin");
            session.setAttribute("role", "ADMIN");
            return "redirect:/admin/dashboard";
        }
        else {
            Optional<Users> user = Optional.ofNullable(userService.login(loginDTO.getEmail(), loginDTO.getPassword()));
            if (user.isPresent()) {
                session.setAttribute("userLogin", user.get().getEmail());
                session.setAttribute("role", user.get().getRole());
                return user.get().getRole().equals(Role.LECTURER) ? "redirect:/lecturer/home" : "redirect:/student/home";
            } else {
                redirectAttributes.addFlashAttribute("error" , "Tài khoản hoặc mật khẩu không chính xác !");
                return "auth/login";
            }
        }
    }
}

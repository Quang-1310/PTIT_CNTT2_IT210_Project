package ra.edu.ptit_cntt2_it210_project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.ptit_cntt2_it210_project.model.dto.LoginDTO;
import ra.edu.ptit_cntt2_it210_project.model.dto.RegisterDTO;
import ra.edu.ptit_cntt2_it210_project.model.entity.Role;
import ra.edu.ptit_cntt2_it210_project.model.entity.UserProfiles;
import ra.edu.ptit_cntt2_it210_project.model.entity.Users;
import ra.edu.ptit_cntt2_it210_project.service.UserService;
import ra.edu.ptit_cntt2_it210_project.util.PasswordHasher;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        if (userService.existsByEmail(registerDTO.getEmail())) {
            model.addAttribute("registerDTO", registerDTO);
            model.addAttribute("activePage", "add-lecturer");
            model.addAttribute("emailExists", true);
            return "admin/add-lecturer";
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

        return "redirect:/auth/login?success=registered";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginDTO") LoginDTO loginDTO,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes,
                        HttpSession session) {

        if (result.hasErrors()) {
            return "auth/login";
        }

        Optional<Users> userOp = Optional.ofNullable(userService.login(loginDTO.getEmail(), loginDTO.getPassword()));

        if (userOp.isPresent()) {
            Users user = userOp.get();

            session.setAttribute("loginUser", user);
            session.setAttribute("userLogin", user.getEmail());
            session.setAttribute("role", user.getRole().name());

            Role role = user.getRole();
            if (role == Role.ADMIN) {
                return "redirect:/admin/overview";
            } else if (role == Role.LECTURER) {
                return "redirect:/lecturer/home";
            } else {  // STUDENT
                return "redirect:/student/schedule";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Tài khoản hoặc mật khẩu không chính xác!");
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        UserProfiles profile = userService.findUserProfileByUserId(user.getUserId());
        model.addAttribute("userProfile", profile);
        model.addAttribute("user", user);
        return "auth/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute UserProfiles updatedProfile,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        Users user = (Users) session.getAttribute("loginUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        userService.updateUserProfile(user.getEmail(), updatedProfile);
        redirectAttributes.addFlashAttribute("message", "Cập nhật thành công!");

        String role = (String) session.getAttribute("role");
        return switch (role) {
            case "ADMIN" -> "redirect:/admin/equipment";
            case "LECTURER" -> "redirect:/lecturer/home";
            default -> "redirect:/student/schedule";
        };
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
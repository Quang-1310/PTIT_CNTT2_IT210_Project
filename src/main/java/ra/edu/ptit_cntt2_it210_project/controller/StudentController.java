package ra.edu.ptit_cntt2_it210_project.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.ptit_cntt2_it210_project.model.dto.ScheduleFormDTO;
import ra.edu.ptit_cntt2_it210_project.model.entity.MentoringSessions;
import ra.edu.ptit_cntt2_it210_project.model.entity.Users;
import ra.edu.ptit_cntt2_it210_project.service.DepartmentService;
import ra.edu.ptit_cntt2_it210_project.service.LecturerService;
import ra.edu.ptit_cntt2_it210_project.service.MentoringSessionService;
import ra.edu.ptit_cntt2_it210_project.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final DepartmentService departmentService;
    private final LecturerService lecturerService;
    private final MentoringSessionService sessionService;
    private final UserService userService;

    public StudentController(DepartmentService departmentService,
                             LecturerService lecturerService,
                             MentoringSessionService sessionService,
                             UserService userService) {
        this.departmentService = departmentService;
        this.lecturerService = lecturerService;
        this.sessionService = sessionService;
        this.userService = userService;
    }

    // ✅ CORE-05: Form đặt lịch cố vấn
    @GetMapping("/schedule")
    public String scheduleForm(
            @RequestParam(required = false) Long deptId,
            HttpSession session,
            Model model) {

        // ✅ Lấy user từ session
        Users currentUser = getCurrentUserFromSession(session);
        if (currentUser == null) {
            return "redirect:/login?error=unauthorized";
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("lecturers", lecturerService.findByDepartment(deptId));

        // ✅ Min datetime (hiện tại)
        model.addAttribute("minDateTime", LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));

        model.addAttribute("scheduleForm", new ScheduleFormDTO());
        return "student/schedule";
    }

    // ✅ CORE-05: Đặt lịch + VALIDATE CONFLICT
    @PostMapping("/schedule/book")
    public String bookSchedule(
            @Valid @ModelAttribute("scheduleForm") ScheduleFormDTO form,
            BindingResult result,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMsg", "Vui lòng kiểm tra thông tin form!");
            return "redirect:/student/schedule?error";
        }

        Users currentUser = getCurrentUserFromSession(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        try {
            // ✅ CORE LOGIC 1: Không quá khứ
            if (form.getStartTime().isBefore(LocalDateTime.now())) {
                redirectAttributes.addFlashAttribute("errorMsg", "Không thể đặt lịch trong quá khứ!");
                return "redirect:/student/schedule?error";
            }

            // ✅ CORE LOGIC 2: Check conflict giảng viên
            LocalDateTime endTime = form.getStartTime().plusMinutes(30);
            if (sessionService.hasConflict(form.getLecturerId(), form.getStartTime(), endTime)) {
                redirectAttributes.addFlashAttribute("errorMsg",
                        "Giảng viên đã có lịch trong khung giờ này! Vui lòng chọn giờ khác.");
                return "redirect:/student/schedule?error";
            }

            // ✅ Tạo session với student từ session
            sessionService.createSession(form, currentUser.getUserId());
            redirectAttributes.addFlashAttribute("successMsg",
                    " Đặt lịch thành công! Chờ giảng viên xác nhận trong 24h.");

        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Giảng viên không tồn tại!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi hệ thống: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/student/history";
    }

    // ✅ CORE-09: Xác nhận hủy lịch
    @GetMapping("/schedule/cancel/{id}")
    public String cancelConfirm(
            @PathVariable Long id,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Users currentUser = getCurrentUserFromSession(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        try {
            MentoringSessions sessionEntity = sessionService.findByStudentIdAndId(currentUser.getId(), id);
            if (!sessionService.canCancel(sessionEntity)) {
                redirectAttributes.addFlashAttribute("errorMsg", "Quá hạn hủy (phải trước 24h)!");
                return "redirect:/student/history?error=too_late";
            }
            model.addAttribute("session", sessionEntity);
            return "student/cancel-confirm";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lịch không tồn tại hoặc không thuộc bạn!");
            return "redirect:/student/history";
        }
    }

    // ✅ CORE-09: Thực hiện hủy + Unlock slot
    @PostMapping("/schedule/cancel/{id}")
    public String cancelSchedule(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Users currentUser = getCurrentUserFromSession(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        try {
            sessionService.cancelSession(currentUser.getUserId(), id);
            redirectAttributes.addFlashAttribute("successMsg",
                    "✅ Hủy lịch thành công! Khung giờ đã được giải phóng cho sinh viên khác.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi hủy lịch: " + e.getMessage());
        }
        return "redirect:/student/history";
    }

    // ✅ CORE-07: Lịch sử học tập + thiết bị mượn
    @GetMapping("/history")
    public String history(HttpSession session, Model model) {
        Users currentUser = getCurrentUserFromSession(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("sessions", sessionService.findStudentHistory(currentUser.getUserId()));
        // TODO: borrowingRecords khi có entity
        model.addAttribute("borrowingRecords", List.of());
        return "student/history";
    }

    // ✅ Helper: Lấy user từ session
    private Users getCurrentUserFromSession(HttpSession session) {
        return (Users) session.getAttribute("loginUser"); // Từ login controller
    }
}
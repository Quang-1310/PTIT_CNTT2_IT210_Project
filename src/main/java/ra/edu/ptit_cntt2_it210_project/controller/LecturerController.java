package ra.edu.ptit_cntt2_it210_project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.ptit_cntt2_it210_project.model.dto.AssessmentDTO;
import ra.edu.ptit_cntt2_it210_project.model.entity.Equipments;
import ra.edu.ptit_cntt2_it210_project.model.entity.Labs;
import ra.edu.ptit_cntt2_it210_project.model.entity.MentoringSessions;
import ra.edu.ptit_cntt2_it210_project.model.entity.Users;
import ra.edu.ptit_cntt2_it210_project.service.EquipmentService;
import ra.edu.ptit_cntt2_it210_project.service.LabService;
import ra.edu.ptit_cntt2_it210_project.service.LecturerService;
import ra.edu.ptit_cntt2_it210_project.service.MentoringSessionService;

import java.util.List;

@Controller
@RequestMapping("/lecturer")
public class LecturerController {
    private final MentoringSessionService sessionService;
    private final LecturerService lecturerService;
    private final EquipmentService equipmentService;
    private final LabService labService;

    public LecturerController(MentoringSessionService sessionService,
                              LecturerService lecturerService,
                              EquipmentService equipmentService,
                              LabService labService){
        this.sessionService = sessionService;
        this.lecturerService = lecturerService;
        this.equipmentService = equipmentService;
        this.labService = labService;
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size){
        Users currentUser = (Users) session.getAttribute("loginUser");
        if (currentUser == null) return "redirect:/auth/login";

        Pageable pageable = PageRequest.of(page, size);

        Page<MentoringSessions> pendingPage = lecturerService.findPendingByLecturer(currentUser.getUserId(), pageable);
        model.addAttribute("pendingSessions", pendingPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pendingPage.getTotalPages());
        model.addAttribute("totalItems", pendingPage.getTotalElements());

        return "lecturer/home";
    }

    @GetMapping("/assessment/{id}")
    public String showAssessmentForm(@PathVariable Long id,
                                     @RequestParam(required = false) Long labId,
                                     @RequestParam(required = false) String currentEval,
                                     Model model) {

        MentoringSessions sessionCurrent = sessionService.findById(id);
        Long deptId = (sessionCurrent.getDepartment() != null)
                ? sessionCurrent.getDepartment().getDeptId()
                : sessionCurrent.getLecturer().getDepartment().getDeptId();
        List<Labs> labs = labService.findByDepartmentId(deptId);
        List<Equipments> equipments = null;
        if (labId != null) {
            equipments = equipmentService.findAvailableEquipmentsByLab(labId);
        }

        AssessmentDTO assessmentDTO = new AssessmentDTO();
        assessmentDTO.setSessionId(id);
        assessmentDTO.setLabId(labId);

        if (currentEval != null) {
            assessmentDTO.setAssessment(currentEval);
        }
        if (!model.containsAttribute("assessmentDTO")) {
            model.addAttribute("assessmentDTO", assessmentDTO);
        }

        model.addAttribute("sessionCurrent", sessionCurrent);
        model.addAttribute("equipments", equipments);
        model.addAttribute("labs", labs);

        return "lecturer/assessment-form";
    }

    @PostMapping("/assessment/submit")
    public String submitAssessment(
            @Valid @ModelAttribute("assessmentDTO") AssessmentDTO dto,
            BindingResult result,
            RedirectAttributes ra,
            HttpSession session) {
        System.out.println("===== SUBMIT =====");

        if (result.hasErrors()) {
            System.out.println("VALIDATE ERROR");
            ra.addFlashAttribute(
                    "org.springframework.validation.BindingResult.assessmentDTO",
                    result);

            ra.addFlashAttribute("assessmentDTO", dto);

            return "redirect:/lecturer/assessment/" + dto.getSessionId();
        }
        try {
            System.out.println("CALL SERVICE");
            lecturerService.completeAssessment(
                    dto.getSessionId(),
                    dto.getAssessment(),
                    dto.getLabId(),
                    dto.getEquipmentIds());

            ra.addFlashAttribute("successMsg",
                    "Đã hoàn tất đánh giá và gửi yêu cầu thiết bị");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
            return "redirect:/lecturer/assessment/" + dto.getSessionId();
        }

        return "redirect:/lecturer/home";
    }
}

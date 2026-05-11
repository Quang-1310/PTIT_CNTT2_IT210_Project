package ra.edu.ptit_cntt2_it210_project.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.ptit_cntt2_it210_project.model.dto.EquipmentDTO;
import ra.edu.ptit_cntt2_it210_project.model.dto.RegisterDTO;
import ra.edu.ptit_cntt2_it210_project.model.entity.*;
import ra.edu.ptit_cntt2_it210_project.service.*;

import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final EquipmentService equipmentService;
    private final DepartmentService departmentService;
    private final LabService labService;
    private final MentoringSessionService sessionService;
    private final OverviewService overviewService;
    private final UserService userService;
    public AdminController(EquipmentService equipmentService, DepartmentService departmentService, LabService labService, MentoringSessionService sessionService, OverviewService overviewService, UserService userService){
        this.equipmentService = equipmentService;
        this.departmentService = departmentService;
        this.labService = labService;
        this.sessionService = sessionService;
        this.overviewService = overviewService;
        this.userService = userService;
    }


    @GetMapping("/overview")
    public String overview(Model model) {
        Map<String, Object> stats = overviewService.getOverviewStats();
        model.addAttribute("stats", stats);
        model.addAttribute("activePage", "overview");
        return "admin/overview";
    }

    @GetMapping("/equipment")
    public String manageDevices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keywordName,
            @RequestParam(required = false) Long selectedDeptId,
            Model model) {

        model.addAttribute("activePage", "equipment");
        model.addAttribute("departments", departmentService.findAll());

        Pageable pageable = PageRequest.of(page, size);

        Page<Equipments> equipments;

        if (StringUtils.hasText(keywordName) && selectedDeptId != null && selectedDeptId > 0) {
            equipments = equipmentService.searchByNameAndDepartmentId(keywordName.trim(), selectedDeptId, pageable);
            model.addAttribute("keywordName", keywordName.trim());
            model.addAttribute("selectedDeptId", selectedDeptId);
        } else if (StringUtils.hasText(keywordName)) {
            equipments = equipmentService.searchByName(keywordName.trim(), pageable);
            model.addAttribute("keywordName", keywordName.trim());
        } else if (selectedDeptId != null && selectedDeptId > 0) {
            equipments = equipmentService.searchByDepartmentId(selectedDeptId, pageable);
            model.addAttribute("selectedDeptId", selectedDeptId);
        } else {
            equipments = equipmentService.findAll(pageable);
        }

        model.addAttribute("equipments", equipments);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", equipments.getTotalPages());
        model.addAttribute("totalItems", equipments.getTotalElements());

        return "admin/equipment";
    }

    @GetMapping("/borrowing-requests")
    public String listBorrowingRequests(
            @RequestParam(defaultValue = "CONFIRMED") String status,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, 5);
        Page<MentoringSessions> requestPage = sessionService.findByStatus(status, pageable);

        model.addAttribute("requests", requestPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", requestPage.getTotalPages());
        model.addAttribute("activePage", "requests");

        return "admin/borrowing-requests";
    }

    @PostMapping("/borrowing-requests/export/{id}")
    public String exportEquipment(@PathVariable Long id, RedirectAttributes ra) {
        try {

            equipmentService.exportEquipmentForSession(id);
            ra.addFlashAttribute("successMsg", "Xuất kho thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/borrowing-requests?status=CONFIRMED";
    }

    @GetMapping("/equipment/save")
    public String addEquipment(Model model) {
        model.addAttribute("activePage", "equipment");
        model.addAttribute("equipment", new EquipmentDTO());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("labs", labService.findAll());
        model.addAttribute("isNew", true);
        return "admin/equipment-form";
    }

    @GetMapping("/equipment/edit/{id}")
    public String editEquipment(@PathVariable Long id, Model model) {
        model.addAttribute("activePage", "equipment");
        Equipments equipment = equipmentService.findEquipmentsByEquipmentId(id);
        EquipmentDTO dto = new EquipmentDTO();
        dto.setEquipmentId(equipment.getEquipmentId());
        dto.setEquipmentName(equipment.getEquipmentName());
        dto.setStockQuantity(equipment.getStockQuantity());
        dto.setDescription(equipment.getDescription());
        dto.setLab(equipment.getLab());
        dto.setDepartments(equipment.getDepartment());

        model.addAttribute("equipment", dto);
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("labs", labService.findAll());
        model.addAttribute("isNew", false);
        return "admin/equipment-form";
    }

    @PostMapping("/equipment/save")
    public String saveEquipment(@Valid @ModelAttribute("equipment") EquipmentDTO equipment,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
            model.addAttribute("labs", labService.findAll());
            model.addAttribute("activePage", "equipment");
            return "admin/equipment-form";
        }

        Equipments equipments;
        if (equipment.getEquipmentId() != null) {
            equipments = equipmentService.findEquipmentsByEquipmentId(equipment.getEquipmentId());
        } else {
            equipments = new Equipments();
        }

        try {
            equipments.setEquipmentName(equipment.getEquipmentName());
            equipments.setLab(equipment.getLab());
            equipments.setDepartment(equipment.getDepartments());
            equipments.setDescription(equipment.getDescription());
            equipments.setStockQuantity(equipment.getStockQuantity());
            equipmentService.addEquipment(equipments);
            redirectAttributes.addFlashAttribute("successMsg",
                    "Lưu thiết bị " + equipments.getEquipmentName() + " thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            model.addAttribute("departments", departmentService.findAll());
            model.addAttribute("labs", labService.findAll());
            model.addAttribute("activePage", "equipment");
            return "admin/equipment-form";
        }

        return "redirect:/admin/equipment";
    }

    @GetMapping("/equipment/delete/{id}")
    public String deleteEquipment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            equipmentService.deleteEquipment(id);
            redirectAttributes.addFlashAttribute("successMsg", "🗑Xóa thiết bị thành công!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi hệ thống: " + e.getMessage());
        }
        return "redirect:/admin/equipment";
    }

    @GetMapping("/add-lecturer")
    public String addLecturerForm(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        model.addAttribute("activePage", "add-lecturer");
        return "admin/add-lecturer";
    }

    @PostMapping("/add-lecturer")
    public String addLecturer(@Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            System.out.println("=== VALIDATION ERRORS ===");
            result.getAllErrors().forEach(error ->
                    System.out.println("Field: " + error.getDefaultMessage())
            );
            model.addAttribute("registerDTO", registerDTO);
            model.addAttribute("activePage", "add-lecturer");
            return "admin/add-lecturer";
        }

        try {
            System.out.println("=== BẮT ĐẦU TẠO USER ===");
            Users user = new Users();
            user.setEmail(registerDTO.getEmail());
            user.setPassword(userService.hash(registerDTO.getPassword()));
            user.setRole(Role.LECTURER);
            userService.createUser(user);

            UserProfiles profile = new UserProfiles();
            profile.setFullName(registerDTO.getFullName());
            profile.setPhone(registerDTO.getPhone());
            profile.setEmail(registerDTO.getEmail());
            profile.setUser(user);
            userService.createUserProfile(profile);

            redirectAttributes.addFlashAttribute("successMsg", "Thêm tài khoản giảng viên thành công!");
            return "redirect:/admin/overview";
        } catch (Exception e) {
            System.err.println("=== LỖI TẠO USER ===");
            System.err.println("ERROR CLASS: " + e.getClass().getSimpleName());
            System.err.println("ERROR MESSAGE: " + e.getMessage());
            System.err.println("Email: " + registerDTO.getEmail());
            System.err.println("Password: " + registerDTO.getPassword());
            model.addAttribute("registerDTO", registerDTO);
            model.addAttribute("activePage", "add-lecturer");
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
            return "redirect:/admin/add-lecturer";
        }
    }

    @GetMapping("/departments")
    public String listDepartments(Model model) {
        model.addAttribute("activePage", "departments");
        model.addAttribute("departments", departmentService.findAll());
        return "admin/departments";
    }

    @GetMapping("/labs")
    public String listLabs(Model model) {
        model.addAttribute("activePage", "labs");
        model.addAttribute("labs", labService.findAll());
        return "admin/labs";
    }
}

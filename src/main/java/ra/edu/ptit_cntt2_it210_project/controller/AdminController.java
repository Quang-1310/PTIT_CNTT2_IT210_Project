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
import ra.edu.ptit_cntt2_it210_project.model.entity.Equipments;
import ra.edu.ptit_cntt2_it210_project.service.DepartmentService;
import ra.edu.ptit_cntt2_it210_project.service.EquipmentService;
import ra.edu.ptit_cntt2_it210_project.service.LabService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final EquipmentService equipmentService;
    private final DepartmentService departmentService;
    private final LabService labService;
    public AdminController(EquipmentService equipmentService, DepartmentService departmentService, LabService labService){
        this.equipmentService = equipmentService;
        this.departmentService = departmentService;
        this.labService = labService;
    }


    @GetMapping("/layout")
    public String layout(Model model) {
        model.addAttribute("activePage", "layout");
        model.addAttribute("totalEquipments", 120);
        model.addAttribute("pendingCount", 3);
        model.addAttribute("borrowingCount", 12);
        return "admin/layout";
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
    public String borrowingRequests(Model model) {
        model.addAttribute("activePage", "requests");
        model.addAttribute("pendingCount", 3);
        return "admin/borrowing-requests";
    }

    @GetMapping("/student-borrowing")
    public String studentBorrowing(Model model) {
        model.addAttribute("activePage", "students");
        model.addAttribute("pendingCount", 3);
        return "admin/student-borrowing";
    }

    @GetMapping("/equipment/save")
    public String addEquipment(Model model) {
        model.addAttribute("activePage", "equipment");
        model.addAttribute("equipment", new Equipments());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("labs", labService.findAll());
        model.addAttribute("isNew", true);
        return "admin/equipment-form";
    }

    @GetMapping("/equipment/edit/{id}")
    public String editEquipment(@PathVariable Long id, Model model) {
        model.addAttribute("activePage", "equipment");
        Equipments equipment = equipmentService.findEquipmentsByEquipmentId(id);
        model.addAttribute("equipment", equipment);
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("labs", labService.findAll());
        model.addAttribute("isNew", false);
        return "admin/equipment-form";
    }

    @PostMapping("/equipment/save")
    public String saveEquipment(@Valid @ModelAttribute("equipment") Equipments equipment,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
            model.addAttribute("labs", labService.findAll());
            model.addAttribute("activePage", "equipment");
            return "admin/equipment-form";
        }

        try {
            equipmentService.addEquipment(equipment);
            redirectAttributes.addFlashAttribute("successMsg",
                    "Lưu thiết bị " + equipment.getEquipmentName() + " thành công!");
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
            redirectAttributes.addFlashAttribute("successMsg", "🗑️ Xóa thiết bị thành công!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "Lỗi hệ thống: " + e.getMessage());
        }
        return "redirect:/admin/equipment";
    }
}

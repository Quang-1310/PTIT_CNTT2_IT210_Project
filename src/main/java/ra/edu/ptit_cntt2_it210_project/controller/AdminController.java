package ra.edu.ptit_cntt2_it210_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ra.edu.ptit_cntt2_it210_project.repository.EquipmentRepository;
import ra.edu.ptit_cntt2_it210_project.service.EquipmentService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final EquipmentService equipmentService;
    public AdminController(EquipmentService equipmentService){
        this.equipmentService = equipmentService;
    }


    @GetMapping("/equipment")
    public String viewEquipment(Model model){
        model.addAttribute("devices", equipmentService.findAll());
        return "admin/equipment";
    }
}

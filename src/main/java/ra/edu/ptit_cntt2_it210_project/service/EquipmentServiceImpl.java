package ra.edu.ptit_cntt2_it210_project.service;

import org.springframework.stereotype.Service;
import ra.edu.ptit_cntt2_it210_project.model.entity.Equipments;
import ra.edu.ptit_cntt2_it210_project.repository.EquipmentRepository;

import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentService{
    private final EquipmentRepository equipmentRepository;
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository){
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public List<Equipments> findAll() {
        return equipmentRepository.findAll();
    }
}

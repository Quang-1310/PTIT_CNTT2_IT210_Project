package ra.edu.ptit_cntt2_it210_project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.edu.ptit_cntt2_it210_project.model.entity.Equipments;
import ra.edu.ptit_cntt2_it210_project.repository.EquipmentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentServiceImpl implements EquipmentService{
    private final EquipmentRepository equipmentRepository;
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository){
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public Page<Equipments> findAll(Pageable pageable) {
        return equipmentRepository.findAllActive(pageable);
    }

    @Override
    public Page<Equipments> searchByName(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll(pageable);
        }
        return equipmentRepository.searchByName(keyword.trim(), pageable);
    }

    @Override
    public Page<Equipments> searchByNameAndDepartmentId(String nameKeyword, Long deptId, Pageable pageable) {
        if ((nameKeyword == null || nameKeyword.trim().isEmpty()) &&
                (deptId == null)) {
            return findAll(pageable);
        }
        return equipmentRepository.searchByNameAndDepartmentId(nameKeyword, deptId, pageable);
    }

    @Override
    public Page<Equipments> searchByDepartmentId(Long deptId, Pageable pageable) {
        return equipmentRepository.searchByDepartmentId(deptId, pageable);
    }

    @Override
    public Equipments addEquipment(Equipments newEquipment) {
        return equipmentRepository.save(newEquipment);
    }

    @Override
    public void updateEquipment(Equipments equipment) {
        equipmentRepository.save(equipment);
    }

    @Override
    public void deleteEquipment(Long equipmentId) {
        Equipments equipment = equipmentRepository.findEquipmentsByEquipmentId(equipmentId);
        if (equipment == null) {
            throw new IllegalStateException("Không thể xóa thiết bị đang được mượn!");
        }
        if (isEquipmentBeingBorrowed(equipmentId)) {
            throw new IllegalStateException("Không thể xóa! Thiết bị đang được mượn/trong phiếu chờ xác nhận");
        }

        equipment.setIsDeleted(true);
        equipmentRepository.save(equipment);
    }

    private boolean isEquipmentBeingBorrowed(Long equipmentId) {
        return false;
    }

    @Override
    public Equipments findEquipmentsByEquipmentId(Long equipmentId) {
        return equipmentRepository.findEquipmentsByEquipmentId(equipmentId);
    }


}

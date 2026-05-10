package ra.edu.ptit_cntt2_it210_project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.edu.ptit_cntt2_it210_project.model.entity.Equipments;

import java.util.List;

public interface EquipmentService {
    Page<Equipments> findAll(Pageable pageable);
    List<Equipments> findAllActiveDefault();
    Equipments addEquipment(Equipments newEquipment);
    void updateEquipment(Equipments equipment);
    void deleteEquipment(Long equipmentId);
    Equipments findEquipmentsByEquipmentId(Long equipmentId);
    Page<Equipments> searchByName(String keyword, Pageable pageable);
    Page<Equipments> searchByNameAndDepartmentId(String nameKeyword, Long deptId, Pageable pageable);
    Page<Equipments> searchByDepartmentId(Long deptId, Pageable pageable);
    List<Equipments> findByLabId(Long labId);
    List<Equipments> findAvailableEquipmentsByLab(Long labId);
    void exportEquipmentForSession(Long sessionId);
}

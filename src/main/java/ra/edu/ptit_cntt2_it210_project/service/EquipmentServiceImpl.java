package ra.edu.ptit_cntt2_it210_project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.ptit_cntt2_it210_project.model.entity.BorrowingDetails;
import ra.edu.ptit_cntt2_it210_project.model.entity.BorrowingRecords;
import ra.edu.ptit_cntt2_it210_project.model.entity.Equipments;
import ra.edu.ptit_cntt2_it210_project.model.entity.MentoringSessions;
import ra.edu.ptit_cntt2_it210_project.repository.EquipmentRepository;
import ra.edu.ptit_cntt2_it210_project.repository.MentoringSessionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentServiceImpl implements EquipmentService{
    private final EquipmentRepository equipmentRepository;
    private final MentoringSessionRepository sessionRepository;
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository, MentoringSessionRepository sessionRepository){
        this.equipmentRepository = equipmentRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Page<Equipments> findAll(Pageable pageable) {
        return equipmentRepository.findAllActive(pageable);
    }

    @Override
    public List<Equipments> findAllActiveDefault() {
        return equipmentRepository.findAllActiveDefault();
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
    public List<Equipments> findByLabId(Long labId) {
        return equipmentRepository.findByLab_LabId(labId);
    }

    @Override
    public List<Equipments> findAvailableEquipmentsByLab(Long labId) {
        return equipmentRepository.findByLab_LabIdAndStockQuantityGreaterThan(labId, 0);
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

    @Override
    @Transactional
    public void exportEquipmentForSession(Long sessionId) {
        MentoringSessions session = sessionRepository.findById(sessionId).orElseThrow();

        BorrowingRecords record = session.getBorrowingRecord();
        if (record == null) {
            throw new RuntimeException("Không tìm thấy thông tin phiếu mượn cho ca tư vấn này!");
        }

        if (record.getDetails() != null) {
            for (BorrowingDetails detail : record.getDetails()) {
                Equipments equipment = detail.getEquipment();
                int requestQuantity = detail.getQuantity();

                // Kiểm tra tồn kho
                if (equipment.getStockQuantity() < requestQuantity) {
                    throw new RuntimeException("Thiết bị [" + equipment.getEquipmentName() +
                            "] không đủ số lượng trong kho (Hiện có: " +
                            equipment.getStockQuantity() + ")");
                }

                // Trừ số lượng tồn kho
                equipment.setStockQuantity(equipment.getStockQuantity() - requestQuantity);
                equipmentRepository.save(equipment);
            }
        }

        record.setStatus("COMPLETED");
        session.setStatus("COMPLETED");

        sessionRepository.save(session);
    }


}

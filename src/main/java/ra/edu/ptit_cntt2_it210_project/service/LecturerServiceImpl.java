package ra.edu.ptit_cntt2_it210_project.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.ptit_cntt2_it210_project.model.entity.*;
import ra.edu.ptit_cntt2_it210_project.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository;
    private final MentoringSessionRepository sessionRepository;
    private final AcademicEvaluationRepository evaluationRepository;
    private final BorrowingRecordRepository borrowingRepository;
    private final EquipmentRepository equipmentRepository;
    private final LabRepository labRepository;

    public LecturerServiceImpl(LecturerRepository lecturerRepository,MentoringSessionRepository sessionRepository,
                               AcademicEvaluationRepository evaluationRepository,
                               BorrowingRecordRepository borrowingRepository,
                               EquipmentRepository equipmentRepository,
                               LabRepository labRepository) {
        this.lecturerRepository = lecturerRepository;
        this.sessionRepository = sessionRepository;
        this.evaluationRepository = evaluationRepository;
        this.borrowingRepository = borrowingRepository;
        this.equipmentRepository = equipmentRepository;
        this.labRepository = labRepository;
    }

    @Override
    public List<Lecturers> findByDepartment(Long deptId) {
        if (deptId == null || deptId <= 0) {
            return List.of();
        }
        List<Lecturers> lecturers = lecturerRepository.findByDepartmentId(deptId);
        return lecturers.stream()
                .filter(l -> l.getUser() != null && !Boolean.TRUE.equals(l.getUser().getIsDeleted()))
                .toList();
    }

    @Override
    public List<Lecturers> findAllActive() {
        return lecturerRepository.findAllActive();
    }

    @Override
    public Lecturers findByUserId(Long userId) {
        return lecturerRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Giảng viên không tồn tại userId: " + userId));
    }

    @Override
    public Page<MentoringSessions> findPendingByLecturer(Long lecturerId, Pageable pageable) {
        return lecturerRepository.findPendingByLecturer(lecturerId, pageable);
    }

    @Override
    @Transactional
    public void completeAssessment(Long sessionId, String assessment, Long labId, List<Long> equipmentIds) {
        MentoringSessions session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy ca tư vấn"));

        if (!"PENDING".equals(session.getStatus()) && !"CONFIRMED".equals(session.getStatus())) {
            throw new IllegalStateException("Ca tư vấn này không ở trạng thái có thể đánh giá");
        }

        AcademicEvaluations eval = new AcademicEvaluations();
        eval.setAssessment(assessment);
        Labs lab = labRepository.findById(labId).orElseThrow();
        eval.setAssignedLab(lab.getLabName());
        eval.setEvaluationDate(LocalDateTime.now());
        eval.setMentoringSession(session);
        evaluationRepository.save(eval);

        if (equipmentIds != null && !equipmentIds.isEmpty()) {
            BorrowingRecords record = new BorrowingRecords();
            record.setBorrowDate(LocalDateTime.now());
            record.setStatus("WAITING_FOR_DELIVERY");
            record.setMentoringSession(session);

            List<BorrowingDetails> details = equipmentIds.stream().map(id -> {
                BorrowingDetails d = new BorrowingDetails();
                d.setEquipment(equipmentRepository.getReferenceById(id));
                d.setBorrowingRecord(record);
                d.setQuantity(1);
                return d;
            }).collect(Collectors.toList());

            record.setDetails(details);
            borrowingRepository.save(record);
        }

        session.setStatus("CONFIRMED");
        sessionRepository.save(session);
    }
}
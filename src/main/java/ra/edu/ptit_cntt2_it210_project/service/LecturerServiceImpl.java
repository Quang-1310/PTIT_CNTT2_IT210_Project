package ra.edu.ptit_cntt2_it210_project.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.ptit_cntt2_it210_project.model.entity.Lecturers;
import ra.edu.ptit_cntt2_it210_project.repository.LecturerRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository;

    public LecturerServiceImpl(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
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
}
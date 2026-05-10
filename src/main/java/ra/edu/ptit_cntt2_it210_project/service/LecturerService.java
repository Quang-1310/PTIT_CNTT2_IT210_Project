package ra.edu.ptit_cntt2_it210_project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.edu.ptit_cntt2_it210_project.model.entity.Lecturers;
import ra.edu.ptit_cntt2_it210_project.model.entity.MentoringSessions;

import java.util.List;

public interface LecturerService {
    List<Lecturers> findByDepartment(Long deptId);
    List<Lecturers> findAllActive();
    Lecturers findByUserId(Long userId);
    Page<MentoringSessions> findPendingByLecturer(Long lecturerId, Pageable page);
    void completeAssessment(Long sessionId, String assessment, Long labId, List<Long> equipmentIds);
}

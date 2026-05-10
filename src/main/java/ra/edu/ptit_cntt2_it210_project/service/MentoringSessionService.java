package ra.edu.ptit_cntt2_it210_project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.edu.ptit_cntt2_it210_project.model.dto.ScheduleFormDTO;
import ra.edu.ptit_cntt2_it210_project.model.entity.MentoringSessions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MentoringSessionService {
    boolean hasConflict(Long lecturerId, LocalDateTime start, LocalDateTime end);
    void createSession(ScheduleFormDTO form, Long studentId);
    MentoringSessions findById(Long id);
    List<MentoringSessions> findStudentHistory(Long studentId);
    boolean canCancel(MentoringSessions session);
    void cancelSession(Long studentId, Long id);
    Optional<MentoringSessions> findByStudentIdAndId(Long studentId, Long sessionId);
    Page<MentoringSessions> findByStatus(String status, Pageable pageable);
}

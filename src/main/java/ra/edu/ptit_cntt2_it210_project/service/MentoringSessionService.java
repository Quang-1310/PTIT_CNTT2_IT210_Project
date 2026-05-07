package ra.edu.ptit_cntt2_it210_project.service;

import ra.edu.ptit_cntt2_it210_project.model.dto.ScheduleFormDTO;
import ra.edu.ptit_cntt2_it210_project.model.entity.MentoringSessions;
import ra.edu.ptit_cntt2_it210_project.model.entity.Users;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MentoringSessionService {
    boolean hasConflict(Long lecturerId, LocalDateTime start, LocalDateTime end);
    MentoringSessions createSession(ScheduleFormDTO form, Long studentId);
    MentoringSessions findById(Long id);
    List<MentoringSessions> findStudentHistory(Long studentId);
    boolean canCancel(MentoringSessions session);
    void cancelSession(Long studentId, Long id);
}

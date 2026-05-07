package ra.edu.ptit_cntt2_it210_project.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.ptit_cntt2_it210_project.model.dto.ScheduleFormDTO;
import ra.edu.ptit_cntt2_it210_project.model.entity.Lecturers;
import ra.edu.ptit_cntt2_it210_project.model.entity.MentoringSessions;
import ra.edu.ptit_cntt2_it210_project.model.entity.Users;
import ra.edu.ptit_cntt2_it210_project.repository.LecturerRepository;
import ra.edu.ptit_cntt2_it210_project.repository.MentoringSessionRepository;
import ra.edu.ptit_cntt2_it210_project.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MentoringSessionServiceImpl implements MentoringSessionService {

    private final MentoringSessionRepository sessionRepository;
    private final LecturerRepository lecturerRepository;
    private final UserRepository userRepository;

    public MentoringSessionServiceImpl(MentoringSessionRepository sessionRepository,
                                       LecturerRepository lecturerRepository,
                                       UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.lecturerRepository = lecturerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean hasConflict(Long lecturerId, LocalDateTime start, LocalDateTime end) {
        return sessionRepository.existsConflictByLecturer(lecturerId, start, end);
    }

    @Override
    public MentoringSessions createSession(ScheduleFormDTO form, Long studentId) {
        Lecturers lecturer = lecturerRepository.findByUserId(form.getLecturerId())
                .orElseThrow(() -> new EntityNotFoundException("Giảng viên không tồn tại ID: " + form.getLecturerId()));

        Users student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Sinh viên không tồn tại ID: " + studentId));

        LocalDateTime endTime = form.getStartTime().plusMinutes(30);

        MentoringSessions session = MentoringSessions.builder()
                .lecturer(lecturer)
                .student(student)
                .startTime(form.getStartTime())
                .endTime(endTime)
                .status("PENDING")
                .build();

        return sessionRepository.save(session);
    }

    @Override
    public MentoringSessions findById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lịch không tồn tại ID: " + id));
    }

    // ✅ FIX: Nhận studentId từ Controller
    @Override
    public List<MentoringSessions> findStudentHistory(Long studentId) {
        return sessionRepository.findHistoryByStudentId(studentId);
    }

    @Override
    public boolean canCancel(MentoringSessions session) {
        // ✅ CORE-09: Hủy trước 24h
        return Duration.between(LocalDateTime.now(), session.getStartTime()).toHours() >= 24;
    }

    // ✅ FIX: Nhận studentId để validate quyền
    @Override
    public void cancelSession(Long studentId, Long sessionId) {
        // ✅ Check session thuộc student
        MentoringSessions session = sessionRepository.findByStudentIdAndId(studentId, sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Lịch không tồn tại hoặc không thuộc bạn!"));

        if (!canCancel(session)) {
            throw new IllegalStateException(" Quá hạn hủy! Phải hủy trước 24h.");
        }

        // ✅ Unlock slot: Status = CANCELLED
        session.setStatus("CANCELLED");
        sessionRepository.save(session);
    }

    // ✅ Internal: Tìm session theo student + id
    public Optional<MentoringSessions> findByStudentIdAndId(Long studentId, Long sessionId) {
        return sessionRepository.findByStudentIdAndId(studentId, sessionId);
    }
}
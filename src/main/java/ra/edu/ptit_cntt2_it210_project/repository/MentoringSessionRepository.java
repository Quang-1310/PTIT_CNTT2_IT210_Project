package ra.edu.ptit_cntt2_it210_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.edu.ptit_cntt2_it210_project.model.entity.MentoringSessions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MentoringSessionRepository extends JpaRepository<MentoringSessions, Long> {

    @Query("SELECT COUNT(s) > 0 FROM MentoringSessions s " +
            "WHERE s.lecturer.userId = :lecturerId " +
            "AND s.status NOT IN ('CANCELLED', 'COMPLETED') " +
            "AND ((s.startTime <= :endTime AND s.endTime >= :startTime))")
    boolean existsConflictByLecturer(
            @Param("lecturerId") Long lecturerId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT s FROM MentoringSessions s " +
            "LEFT JOIN FETCH s.lecturer l " +
            "LEFT JOIN FETCH l.user " +
            "LEFT JOIN FETCH s.student " +
            "WHERE s.student.userId = :studentId " +
            "ORDER BY s.startTime DESC")
    List<MentoringSessions> findHistoryByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT s FROM MentoringSessions s " +
            "JOIN FETCH s.lecturer l " +
            "WHERE s.lecturer.userId = :id AND s.student.userId = :studentId")
    Optional<MentoringSessions> findByStudentIdAndId(
            @Param("studentId") Long studentId,
            @Param("id") Long id
    );

    @Query("SELECT s FROM MentoringSessions s " +
            "WHERE s.lecturer.userId = :lecturerId " +
            "AND s.status = 'PENDING' " +
            "ORDER BY s.startTime ASC")
    List<MentoringSessions> findPendingByLecturer(@Param("lecturerId") Long lecturerId);
}

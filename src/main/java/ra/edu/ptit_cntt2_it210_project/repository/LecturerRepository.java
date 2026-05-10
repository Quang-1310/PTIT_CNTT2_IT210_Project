package ra.edu.ptit_cntt2_it210_project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.edu.ptit_cntt2_it210_project.model.entity.Lecturers;
import ra.edu.ptit_cntt2_it210_project.model.entity.MentoringSessions;

import java.util.List;
import java.util.Optional;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturers, Long> {

    @Query("SELECT l FROM Lecturers l " +
            "JOIN FETCH l.user u " +
            "JOIN FETCH l.department d " +
            "WHERE d.deptId = :deptId " +
            "ORDER BY u.profile.fullName")
    List<Lecturers> findByDepartmentId(@Param("deptId") Long deptId);

    Optional<Lecturers> findByUserId(Long userId);

    @Query("SELECT l FROM Lecturers l " +
            "JOIN FETCH l.user u " +
            "WHERE u.isDeleted = false " +
            "ORDER BY u.profile.fullName")
    List<Lecturers> findAllActive();

    @Query("SELECT s FROM MentoringSessions s " +
            "JOIN s.lecturer l " +
            "WHERE l.user.userId = :lecturerId " +
            "AND s.status IN ('PENDING', 'CONFIRMED', 'COMPLETED') " +
            "ORDER BY s.startTime ASC")
    Page<MentoringSessions> findPendingByLecturer(@Param("lecturerId") Long lecturerId, Pageable pageable);
}

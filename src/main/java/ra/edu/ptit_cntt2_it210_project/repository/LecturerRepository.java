package ra.edu.ptit_cntt2_it210_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.edu.ptit_cntt2_it210_project.model.entity.Lecturers;

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
}

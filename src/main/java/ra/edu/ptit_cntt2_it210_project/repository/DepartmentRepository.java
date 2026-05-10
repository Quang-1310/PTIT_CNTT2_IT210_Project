package ra.edu.ptit_cntt2_it210_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.ptit_cntt2_it210_project.model.entity.Departments;
import ra.edu.ptit_cntt2_it210_project.model.entity.Lecturers;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Departments, Long> {
    Optional<Departments> findByDeptId(Long deptId);
}

package ra.edu.ptit_cntt2_it210_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.ptit_cntt2_it210_project.model.entity.Labs;

import java.util.List;

public interface LabRepository extends JpaRepository<Labs, Long> {
    List<Labs> findByDepartment_DeptId(Long deptId);
}

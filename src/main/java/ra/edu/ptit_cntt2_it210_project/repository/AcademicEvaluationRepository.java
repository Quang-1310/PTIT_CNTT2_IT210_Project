package ra.edu.ptit_cntt2_it210_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.ptit_cntt2_it210_project.model.entity.AcademicEvaluations;

@Repository
public interface AcademicEvaluationRepository extends JpaRepository<AcademicEvaluations, Long> {
}

package ra.edu.ptit_cntt2_it210_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.ptit_cntt2_it210_project.model.entity.Equipments;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipments, Long> {
}

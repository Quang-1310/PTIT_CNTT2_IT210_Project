package ra.edu.ptit_cntt2_it210_project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.edu.ptit_cntt2_it210_project.model.entity.Equipments;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipments, Long> {
    Equipments findEquipmentsByEquipmentId(Long equipmentId);
    List<Equipments> findByEquipmentNameContainingIgnoreCase(String keyword);
    @Query("SELECT SUM(e.stockQuantity) FROM Equipments e WHERE e.equipmentId = :id")
    Integer getTotalStockById(@Param("id") Long id);

    @Query("SELECT e FROM Equipments e WHERE e.isDeleted = false")
    List<Equipments> findAllActiveDefault();

    @Query("SELECT e FROM Equipments e WHERE e.isDeleted = false")
    Page<Equipments> findAllActive(Pageable pageable);

    @Query("SELECT e FROM Equipments e WHERE e.equipmentId = :id AND e.isDeleted = false")
    Optional<Equipments> findActiveById(@Param("id") Long id);

    @Query("SELECT e FROM Equipments e WHERE e.isDeleted = false " +
            "AND LOWER(e.equipmentName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Equipments> searchByName(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT e FROM Equipments e " +
            "JOIN e.department d " +
            "WHERE e.isDeleted = false " +
            "AND LOWER(e.equipmentName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "AND d.deptId = :deptId")
    Page<Equipments> searchByNameAndDepartmentId(
            @Param("keyword") String keyword,
            @Param("deptId") Long deptId,
            Pageable pageable
    );

    @Query("SELECT e FROM Equipments e " +
            "JOIN e.department d " +
            "WHERE e.isDeleted = false AND d.deptId = :deptId")
    Page<Equipments> searchByDepartmentId(@Param("deptId") Long deptId, Pageable pageable);
}

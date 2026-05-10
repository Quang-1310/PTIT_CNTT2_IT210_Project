package ra.edu.ptit_cntt2_it210_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.edu.ptit_cntt2_it210_project.model.entity.BorrowingDetails;

@Repository
public interface BorrowingDetailRepository extends JpaRepository<BorrowingDetails, Long> {
    @Query("SELECT SUM(d.quantity) FROM BorrowingDetails d WHERE d.borrowingRecord.status = :status")
    Integer sumQuantityByBorrowingStatus(@Param("status") String status);
}

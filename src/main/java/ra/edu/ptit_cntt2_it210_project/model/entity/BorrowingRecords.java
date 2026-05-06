package ra.edu.ptit_cntt2_it210_project.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "borrowing_records")
public class BorrowingRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    private LocalDateTime borrowDate;
    private String status;

    @OneToMany(mappedBy = "borrowingRecord")
    private List<BorrowingDetails> details;
}

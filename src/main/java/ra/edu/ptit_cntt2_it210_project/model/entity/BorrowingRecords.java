package ra.edu.ptit_cntt2_it210_project.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "borrowing_records")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BorrowingRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    private LocalDateTime borrowDate;
    private String status;

    @OneToOne
    @JoinColumn(name = "session_id")
    private MentoringSessions mentoringSession;

    @OneToMany(mappedBy = "borrowingRecord",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<BorrowingDetails> details;
}

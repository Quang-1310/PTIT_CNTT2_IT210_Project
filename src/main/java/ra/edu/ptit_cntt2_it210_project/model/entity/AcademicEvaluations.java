package ra.edu.ptit_cntt2_it210_project.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "academic_evaluations")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcademicEvaluations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evaluationId;

    @Column(columnDefinition = "TEXT")
    private String assessment;

    private LocalDateTime evaluationDate;

    private String assignedLab;

    @OneToOne
    @JoinColumn(name = "session_id")
    private MentoringSessions mentoringSession;
}

package ra.edu.ptit_cntt2_it210_project.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mentoring_sessions")
@Getter @Setter
public class MentoringSessions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturers lecturer;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Users student;
}

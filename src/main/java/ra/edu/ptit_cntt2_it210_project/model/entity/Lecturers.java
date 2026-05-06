package ra.edu.ptit_cntt2_it210_project.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "lecturers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Lecturers {
    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String academicRank;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Departments department;

    @OneToMany(mappedBy = "lecturer")
    private List<MentoringSessions> sessions;
}

package ra.edu.ptit_cntt2_it210_project.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "labs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Labs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long labId;

    @Column(nullable = false)
    private String labName;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Departments department;
}

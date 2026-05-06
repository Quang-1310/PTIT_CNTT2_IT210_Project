package ra.edu.ptit_cntt2_it210_project.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Equipments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipmentId;

    private String equipmentName;
    private String description;
    private Integer stockQuantity;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Departments department;
}

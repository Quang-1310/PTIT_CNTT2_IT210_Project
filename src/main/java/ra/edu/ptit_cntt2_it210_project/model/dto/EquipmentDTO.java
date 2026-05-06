package ra.edu.ptit_cntt2_it210_project.model.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EquipmentDTO {
    private Long equipmentId;
    private String equipmentName;
    private String description;
    private Integer stockQuantity;
    private Long deptId;
}

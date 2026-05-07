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
    @NotBlank(message = "Tên thiết bị không được để trống")
    @Size(max = 255, message = "Tên thiết bị quá dài")
    private String equipmentName;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải >= 0")
    private Integer stockQuantity;

    @NotNull(message = "Vui lòng chọn Khoa")
    private Long departmentId;

    @NotNull(message = "Vui lòng chọn Phòng Lab")
    private Integer labId;

    @Size(max = 1000, message = "Mô tả quá dài")
    private String description;
}

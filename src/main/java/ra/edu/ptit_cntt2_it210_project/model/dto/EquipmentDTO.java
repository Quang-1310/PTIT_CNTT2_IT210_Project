package ra.edu.ptit_cntt2_it210_project.model.dto;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ra.edu.ptit_cntt2_it210_project.model.entity.Departments;
import ra.edu.ptit_cntt2_it210_project.model.entity.Labs;
import ra.edu.ptit_cntt2_it210_project.validate.form.FirstCheck;
import ra.edu.ptit_cntt2_it210_project.validate.form.SecondCheck;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@GroupSequence({FirstCheck.class, SecondCheck.class, EquipmentDTO.class})
public class EquipmentDTO {
    private Long equipmentId;

    @NotBlank(message = "Tên thiết bị không được để trống", groups = FirstCheck.class)
    @Size(max = 255, message = "Tên thiết bị quá dài", groups = SecondCheck.class)
    private String equipmentName;

    @NotNull(message = "Số lượng không được để trống", groups = FirstCheck.class)
    @Min(value = 0, message = "Số lượng phải >= 0", groups = SecondCheck.class)
    private Integer stockQuantity;

    @NotNull(message = "Vui lòng chọn Khoa", groups = FirstCheck.class)
    private Departments departments;

    @NotNull(message = "Vui lòng chọn Phòng Lab", groups = FirstCheck.class)
    private Labs lab;

    @Size(max = 1000, message = "Mô tả quá dài", groups = FirstCheck.class)
    private String description;
}

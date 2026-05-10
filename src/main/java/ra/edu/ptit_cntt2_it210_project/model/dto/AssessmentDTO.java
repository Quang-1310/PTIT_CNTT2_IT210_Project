package ra.edu.ptit_cntt2_it210_project.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentDTO {
    @NotNull(message = "ID ca tư vấn không được để trống")
    private Long sessionId;

    @NotBlank(message = "Nội dung đánh giá không được để trống")
    private String assessment;

    @NotNull(message = "Vui lòng chọn phòng Lab")
    private Long labId;

    private List<Long> equipmentIds;
}
package ra.edu.ptit_cntt2_it210_project.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleFormDTO {
    @NotNull(message = "Chọn khoa")
    private Long departmentId;

    @NotNull(message = "Chọn giảng viên")
    private Long lecturerId;

    @NotNull(message = "Chọn thời gian")
    @FutureOrPresent(message = "Không đặt lịch trong quá khứ")
    private LocalDateTime startTime;
}

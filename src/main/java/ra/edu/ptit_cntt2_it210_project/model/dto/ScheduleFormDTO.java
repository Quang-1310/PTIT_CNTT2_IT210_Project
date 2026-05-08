package ra.edu.ptit_cntt2_it210_project.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleFormDTO {
    @NotNull(message = "Chọn khoa")
    private Long departmentId;

    @NotNull(message = "Chọn giảng viên")
    private Long lecturerId;

    @NotNull(message = "Chọn ngày")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate bookingDate;

    @NotNull(message = "Chọn khung giờ")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime bookingTime;
}

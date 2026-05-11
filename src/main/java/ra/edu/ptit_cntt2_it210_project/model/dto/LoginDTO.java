package ra.edu.ptit_cntt2_it210_project.model.dto;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ra.edu.ptit_cntt2_it210_project.validate.form.FirstCheck;
import ra.edu.ptit_cntt2_it210_project.validate.form.SecondCheck;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@GroupSequence({FirstCheck.class, SecondCheck.class, LoginDTO.class})
public class LoginDTO {
    @NotBlank(message = "Email không được để trống", groups = FirstCheck.class)
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Định dạng Email không hợp lệ (Ví dụ: example@gmail.com)",
            groups = SecondCheck.class
    )
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống", groups = FirstCheck.class)
    @Size(min = 6, message = "Mật khẩu tối thiểu 6 ký tự", groups = SecondCheck.class)
    private String password;
}

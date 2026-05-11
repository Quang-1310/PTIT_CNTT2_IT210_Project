package ra.edu.ptit_cntt2_it210_project.model.dto;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
import lombok.*;
import ra.edu.ptit_cntt2_it210_project.validate.PasswordMatching;
import ra.edu.ptit_cntt2_it210_project.validate.form.FirstCheck;
import ra.edu.ptit_cntt2_it210_project.validate.form.SecondCheck;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@PasswordMatching(message = "Mật khẩu xác nhận không khớp")
@GroupSequence({FirstCheck.class, SecondCheck.class, RegisterDTO.class})
public class RegisterDTO {

    @NotBlank(message = "Họ tên không được để trống", groups = FirstCheck.class)
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống", groups = FirstCheck.class)
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải có 10 chữ số", groups = SecondCheck.class)
    private String phone;

    @NotBlank(message = "Email không được để trống", groups = FirstCheck.class)
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Định dạng Email không hợp lệ (Ví dụ: example@gmail.com)",
            groups = SecondCheck.class
    )
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống", groups = FirstCheck.class)
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự", groups = SecondCheck.class)
    private String password;

    @NotBlank(message = "Vui lòng xác nhận lại mật khẩu", groups = FirstCheck.class)
    private String confirmPassword;

}

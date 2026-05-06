package ra.edu.ptit_cntt2_it210_project.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ra.edu.ptit_cntt2_it210_project.validate.impl.PasswordMatchingValidator;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordMatchingValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatching {
    String message() default "Mật khẩu xác nhận không khớp";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
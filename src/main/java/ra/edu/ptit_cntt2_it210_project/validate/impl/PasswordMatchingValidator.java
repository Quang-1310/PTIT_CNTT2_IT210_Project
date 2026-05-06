package ra.edu.ptit_cntt2_it210_project.validate.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ra.edu.ptit_cntt2_it210_project.model.dto.RegisterDTO;
import ra.edu.ptit_cntt2_it210_project.validate.PasswordMatching;


public class PasswordMatchingValidator implements ConstraintValidator<PasswordMatching, RegisterDTO> {

    @Override
    public boolean isValid(RegisterDTO dto, ConstraintValidatorContext context) {
        if (dto.getPassword() == null || !dto.getPassword().equals(dto.getConfirmPassword())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
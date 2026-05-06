package ra.edu.ptit_cntt2_it210_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.ptit_cntt2_it210_project.model.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findUsersByEmail(String email);
}

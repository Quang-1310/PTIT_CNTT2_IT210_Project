package ra.edu.ptit_cntt2_it210_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.ptit_cntt2_it210_project.model.entity.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findUsersByEmail(String email);
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);
}

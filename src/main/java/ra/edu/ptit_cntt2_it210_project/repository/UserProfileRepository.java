package ra.edu.ptit_cntt2_it210_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.ptit_cntt2_it210_project.model.entity.UserProfiles;

public interface UserProfileRepository extends JpaRepository<UserProfiles, Long> {
    UserProfiles findByEmail(String email);

    UserProfiles findByUserUserId(Long userId);

}

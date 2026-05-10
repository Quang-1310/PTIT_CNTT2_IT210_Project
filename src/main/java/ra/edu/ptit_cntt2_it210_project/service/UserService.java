package ra.edu.ptit_cntt2_it210_project.service;

import ra.edu.ptit_cntt2_it210_project.model.entity.UserProfiles;
import ra.edu.ptit_cntt2_it210_project.model.entity.Users;

import java.util.Optional;

public interface UserService {
    Users login(String email , String password);
    void createUser(Users user);
    void createUserProfile(UserProfiles userProfile);
    String hash(String rawPassword);
    boolean verify(String rawPassword, String hashed);
    UserProfiles findUserProfileByUserId(Long userId);
    void updateUserProfile(String email, UserProfiles updatedProfile);
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);

}

package ra.edu.ptit_cntt2_it210_project.service;

import ra.edu.ptit_cntt2_it210_project.model.entity.UserProfiles;
import ra.edu.ptit_cntt2_it210_project.model.entity.Users;

public interface UserService {
    Users login(String email , String password);
    void createUser(Users user);
    void createUserProfile(UserProfiles userProfile);
    String hash(String rawPassword);
    boolean verify(String rawPassword, String hashed);
    UserProfiles findUserProfilesByEmail(String email);
    void updateUserProfile(String email, UserProfiles updatedProfile);
}

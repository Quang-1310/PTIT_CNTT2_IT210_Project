package ra.edu.ptit_cntt2_it210_project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.ptit_cntt2_it210_project.model.entity.UserProfiles;
import ra.edu.ptit_cntt2_it210_project.model.entity.Users;
import ra.edu.ptit_cntt2_it210_project.repository.UserProfileRepository;
import ra.edu.ptit_cntt2_it210_project.repository.UserRepository;
import ra.edu.ptit_cntt2_it210_project.util.PasswordHasher;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public UserServiceImpl(UserRepository userRepository, UserProfileRepository userProfileRepository){
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public Users login(String email, String password) {
        Optional<Users> userOp = Optional.ofNullable(userRepository.findUsersByEmail(email));
        if(userOp.isPresent()){
            Users user = userOp.get();
            if (verify(password, userOp.get().getPassword())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void createUser(Users user) {
        userRepository.save(user);
    }

    @Override
    public void createUserProfile(UserProfiles userProfile){
        userProfileRepository.save(userProfile);
    }

    @Override
    public String hash(String rawPassword) {
        return PasswordHasher.hashPassword(rawPassword);
    }

    @Override
    public boolean verify(String rawPassword, String hashed) {
        try {
            return PasswordHasher.checkPassword(rawPassword, hashed);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UserProfiles findUserProfilesByEmail(String email) {
        return userProfileRepository.findUserProfilesByEmail(email);
    }

    @Override
    @Transactional
    public void updateUserProfile(String email, UserProfiles updatedProfile) {
        UserProfiles existingProfile = userProfileRepository.findUserProfilesByEmail(email);

        if (existingProfile != null) {
            existingProfile.setFullName(updatedProfile.getFullName());
            existingProfile.setPhone(updatedProfile.getPhone());
            existingProfile.setAddress(updatedProfile.getAddress());

            userProfileRepository.save(existingProfile);
        } else {
            throw new RuntimeException("Không tìm thấy thông tin người dùng với email: " + email);
        }
    }


}

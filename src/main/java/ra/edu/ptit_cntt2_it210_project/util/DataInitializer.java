package ra.edu.ptit_cntt2_it210_project.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ra.edu.ptit_cntt2_it210_project.model.entity.Role;
import ra.edu.ptit_cntt2_it210_project.model.entity.UserProfiles;
import ra.edu.ptit_cntt2_it210_project.model.entity.Users;
import ra.edu.ptit_cntt2_it210_project.repository.UserRepository;
import ra.edu.ptit_cntt2_it210_project.service.UserService;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserService userService;
    public DataInitializer(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }
    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin123@gmail.com";
        String lecturerEmail = "lecturer123@gmail.com";
        if (userRepository.findUsersByEmail(adminEmail) == null) {

            Users admin = new Users();
            admin.setEmail(adminEmail);
            admin.setPassword(userService.hash("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            Users lecturer = new Users();
            lecturer.setEmail(lecturerEmail);
            lecturer.setPassword(userService.hash("lecturer123"));
            lecturer.setRole(Role.LECTURER);
            userRepository.save(lecturer);

            UserProfiles userProfilesAdmin = new UserProfiles();
            userProfilesAdmin.setFullName("Admin");
            userProfilesAdmin.setPhone("0123456789");
            userProfilesAdmin.setEmail(adminEmail);
            userProfilesAdmin.setUser(admin);

            UserProfiles userProfilesLecturer = new UserProfiles();
            userProfilesLecturer.setFullName("Lecturer");
            userProfilesLecturer.setPhone("0111111111");
            userProfilesLecturer.setEmail(lecturerEmail);
            userProfilesLecturer.setUser(lecturer);

            userService.createUserProfile(userProfilesAdmin);
            userService.createUserProfile(userProfilesLecturer);

            System.out.println("Khởi tạo tài khoản Admin và Lecturer thành công");
        } else {
            System.out.println("Đã khởi tạo tài khoản Admin và Lecturer");
        }
    }
}

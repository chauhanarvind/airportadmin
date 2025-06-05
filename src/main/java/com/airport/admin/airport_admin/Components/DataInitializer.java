//package com.airport.admin.airport_admin.Components;
//
//import com.airport.admin.airport_admin.features.roles.Role;
//import com.airport.admin.airport_admin.features.Admin.user.User;
//import com.airport.admin.airport_admin.features.roles.RoleRepository;
//import com.airport.admin.airport_admin.features.Admin.user.UserRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DataInitializer implements CommandLineRunner {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public DataInitializer(UserRepository userRepository,
//                           RoleRepository roleRepository,
//                           PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    public void run(String... args) {
//        if (userRepository.findByEmail("admin@airport.com").isEmpty()) {
//            Role adminRole = roleRepository.findByName("Admin")
//                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
//
//            User admin = new User();
//            admin.setFirstName("System");
//            admin.setLastName("Admin");
//            admin.setEmail("admin@airport.com");
//            admin.setPassword(passwordEncoder.encode("admin123")); // 🔐 hashed
//            admin.setRole(adminRole);
//            admin.setJobRole(null);  // or set a valid JobRole
//            admin.setJobLevel(null); // or set a valid JobLevel
//
//            userRepository.save(admin);
//            System.out.println("✅ Admin user created");
//        }
//    }
//}

package tp.agil.backend;

import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {

        System.out.println("------ ADMIN ------");
        String password1 = "admin123";
        String hashed1 = "$2a$10$9T5R0sMoQ5iVIRpokZnuIeb7XoV6jewwTk/i8p4xwAxmKkY7p6g4i";

        System.out.println("Contraseña normal: " + password1);
        System.out.println("Contraseña hasheada: " + hashed1);

        boolean matches = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().matches(password1, hashed1);
        System.out.println("Coincide?: " + matches);

        System.out.println("------ USER ------");
        String password2 = "user123";
        String hashed2 = "$2a$10$J0mLVRrHCbD6Gxr9E8cr/.ZF72EfNmxqYWdV3kEXrRwQsOJ7mUg/e";

        System.out.println("Contraseña normal: " + password2);
        System.out.println("Contraseña hasheada: " + hashed2);

        boolean matches2 = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().matches(password2, hashed2);
        System.out.println("Coincide?: " + matches2);

        SpringApplication.run(BackendApplication.class, args);
    }

}
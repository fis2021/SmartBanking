package smart.banking.services;


import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import smart.banking.exceptions.UsernameAlreadyExists;
import smart.banking.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;



import static smart.banking.services.FileSystemService.getPathToFile;

public class UserService {

    private static ObjectRepository<User> userRepository;
    private static List<User> users;

    public static void initDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(getPathToFile("smart-banking.db").toFile())
                .openOrCreate("test", "test");

        userRepository = database.getRepository(User.class);
    }

    public static void addUser(String username, String password, String role) throws UsernameAlreadyExists {
        checkUserDoesNotAlreadyExist(username);
        userRepository.insert(new User(username, encodePassword(username, password), role));
    }

    private static void checkUserDoesNotAlreadyExist(String username) throws UsernameAlreadyExists {
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername()))
                throw new UsernameAlreadyExists(username);
        }
    }

    private static String encodePassword(String salt, String password) {
        MessageDigest md = getMessageDigest();
        md.update(salt.getBytes(StandardCharsets.UTF_8));

        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        return new String(hashedPassword, StandardCharsets.UTF_8)
                .replace("\"", "");
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 does not exist!");
        }
        return md;
    }

    public static int checkUsers(String username,String password) {
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername())
                    && Objects.equals(encodePassword(username, password), user.getPassword())
                    && Objects.equals("Client", user.getRole()))
                return 1;
            if (Objects.equals(username, user.getUsername())
                    && Objects.equals(encodePassword(username, password), user.getPassword())
                    && Objects.equals("Bank Representative", user.getRole()))
                return 2;
        }
        return 0;

    }
}


package pl.kuba.user;

import org.springframework.stereotype.Repository;
import pl.kuba.error.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {
    private final Map<String, User> userMap = new ConcurrentHashMap<>();

    public User update(String email, User user) throws EntityNotFoundException {
        if (!exists(email)) {
            throw new EntityNotFoundException("Użytkownik " + email + " nie istnieje");
        }
        user.setEmail(email);
        return userMap.put(email, user);
    }
    public User save(User user) {
        return userMap.put(user.getEmail(), user);
    }
    public User findOne(String email) throws EntityNotFoundException {
        if (!exists(email)) {
            throw new EntityNotFoundException("Użytkownik " + email + " nie istnieje");
        }
        return userMap.get(email);
    }
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }
    public void delete(String email) throws EntityNotFoundException {
        if (!exists(email)) {
            throw new EntityNotFoundException("Użytkownik " + email + " nie istnieje");
        }
        userMap.remove(email);
    }
    public boolean exists(String email) {
        return userMap.containsKey(email);
    }

    public void reset(User... users) {
        userMap.clear();
        for (User user : users) {
            save(user);
        }
    }
}

package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private Map<Integer, User> userMapId = new ConcurrentHashMap<>();
    private Map<String, User> userMapEmail = new ConcurrentHashMap<>();
    private AtomicInteger userIdCounter = new AtomicInteger(0);

    @Override
    public boolean delete(int id) {
        log.info("delete user id {}", id);

        if (userMapId.get(id) != null) {
            userMapEmail.remove(userMapId.get(id).getEmail());
            userMapId.remove(id);
            return true;
        }
        else
            return false;
    }

    @Override
    public User save(User user) {
        log.info("save user {}", user);

        if (user.isNew()) {
            user.setId(userIdCounter.incrementAndGet());
            userMapId.put(user.getId(), user);
            userMapEmail.put(user.getEmail(), user);
            return user;
        }
        else {
            userMapEmail.computeIfPresent(user.getEmail(), (email, oldUser) -> user);
            return userMapId.computeIfPresent(user.getId(), (id, oldUser) -> user);
        }
    }

    @Override
    public User get(int id) {
        if (userMapId.get(id) != null) {
            log.info("get user id {}", id);
            return userMapId.get(id);
        }
        else
            return null;
    }

    @Override
    public List<User> getAll() {
        log.info("getAllUsers");
        List<User> userList = new CopyOnWriteArrayList<>();
        Collections.sort(userList, (u1, u2) -> u1.getName().compareTo(u2.getName()));
        return userList;
    }

    @Override
    public User getByEmail(String email) {
        if (userMapEmail.get(email) != null) {
            log.info("get user email {}", email);
            return userMapEmail.get(email);
        }
        else
            return null;
    }
}

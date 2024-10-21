package hexlet.code.controller;

import hexlet.code.model.User;
import hexlet.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Controller for user-related operations.
 * <p>
 * This class can be subclassed to provide additional user-related functionality.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new user.
     * Subclasses should ensure to call this method safely.
     *
     * @param user the user object to create.
     * @return ResponseEntity containing the created user.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Retrieves a user by their ID.
     * Subclasses should ensure to call this method safely.
     *
     * @param id the ID of the user to retrieve.
     * @return ResponseEntity containing the user if found, or a 404 Not Found status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing user.
     * Subclasses should ensure to call this method safely.
     *
     * @param id the ID of the user to update.
     * @param updatedUser the updated user object.
     * @return ResponseEntity containing the updated user.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }

    /**
     * Deletes a user by their ID.
     * Subclasses should ensure to call this method safely.
     *
     * @param id the ID of the user to delete.
     * @return ResponseEntity indicating the result of the deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all users.
     * Subclasses should ensure to call this method safely.
     *
     * @return List of all users.
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}

//package io.github.adrbloch.bookcatalog.controller.rest;
//
//import io.github.adrbloch.bookcatalog.domain.User;
//import io.github.adrbloch.bookcatalog.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/usersRest")
//public class UserRestController {
//
//    private UserService userService;
//
//    @Autowired
//    public UserRestController(UserService userService) {
//        this.userService = userService;
//    }
//
//
//    @GetMapping("/all")
//    ResponseEntity<List<User>> viewAll() {
//        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    ResponseEntity<User> viewAuthor(@PathVariable Long id) {
//        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
//    }
//
//    @PostMapping
//    ResponseEntity<User> createAuthor(@RequestBody User newUser) {
//        return new ResponseEntity<>(userService.createUser(newUser), HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    ResponseEntity<User> updateAuthor(@RequestBody User userToUpdate, @PathVariable Long id) {
//        return new ResponseEntity<>(userService.updateUser(id, userToUpdate), HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    ResponseEntity<User> deleteAuthor(@PathVariable Long id) {
//        return new ResponseEntity<>(userService.deleteUserById(id), HttpStatus.OK);
//
//    }
//
//}

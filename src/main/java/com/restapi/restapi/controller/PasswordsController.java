package com.restapi.restapi.controller;

import com.restapi.restapi.model.Passwords;
import com.restapi.restapi.dto.PasswordsDTO;
import com.restapi.restapi.dto.PasswordsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/tasks")
public class PasswordsController {

    private final PasswordsService passwordsService;

    @Autowired
    public PasswordsController(PasswordsService passwordsService){
        this.passwordsService = passwordsService;
    }

    @GetMapping("/passwords/{id}")
    public ResponseEntity<Passwords> getPassword(@PathVariable int id){
            Passwords password = passwordsService.getPassword(id);

            return password != null ? ResponseEntity.ok(password) : ResponseEntity.notFound().build();
    }

    @GetMapping("/passwords")
    public ResponseEntity<List<Passwords>> getPassword(){
        List<Passwords> passwords = passwordsService.getAllPassword();

        return passwords.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(passwords);
    }

    @PostMapping("/passwords")
    public ResponseEntity<String> postPasswords(@Valid @RequestBody PasswordsDTO passwordsDTO){
        return passwordsService.createPassword(passwordsDTO) ?
                ResponseEntity.status(HttpStatus.CREATED).body("Password inserted") :
                ResponseEntity.badRequest().body("Insert failed");
    }

    @PutMapping("/passwords/{id}")
    public ResponseEntity<String> putPasswords(@PathVariable int id, @RequestBody PasswordsDTO passwordsDTO){
        boolean success = passwordsService.putPassword(passwordsDTO, id);

        return success ? ResponseEntity.ok("Successfully done the update!") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("No passwords with this ID!");
    }

    @DeleteMapping("/passwords/{id}")
    public ResponseEntity<String> deletePasswords(@PathVariable int id){
        boolean success = passwordsService.deletePassword(id);
        return success ? ResponseEntity.ok("Successfully deleted") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("No passwords with this ID!");
    }

    @PutMapping("/passwords/mark-for-delete/{id}")
    public ResponseEntity<String> markPasswordForDelete(@PathVariable int id, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam Date date){
        boolean success = passwordsService.markForDelete(date, id);
        return success ? ResponseEntity.ok("Successfully marked for delete!") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("No passwords with this ID!");
    }


    @DeleteMapping("/passwords/older-than")
    public ResponseEntity<String> deletePasswordsOlderThan(@RequestParam int years){
        boolean success = passwordsService.deletePasswordsOlderThan(years);
        return success ? ResponseEntity.ok("Deleted all passwords that were older than " + years + " years!") :
                ResponseEntity.ok("No passwords were found that are older than " + years + " years!");
    }
}

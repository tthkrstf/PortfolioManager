package com.restapi.restapi.service;

import com.restapi.restapi.dto.PasswordsDTO;
import com.restapi.restapi.model.Passwords;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class PasswordsService {

    private final JdbcTemplate jdbcTemplate;
    private final Random random = new Random();

    @Autowired
    public PasswordsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private int generateUniqueRandomId() {
        int id;
        boolean exists;
        do {
            id = random.nextInt(1_000_000);
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM Passwords WHERE id = ?",
                    new Object[]{id}, Integer.class);
            exists = (count != null && count > 0);
        } while (exists);
        return id;
    }

    public boolean createPassword(PasswordsDTO passwordsDTO) {
        if(passwordsDTO.getUrl() == null) passwordsDTO.setUrl("base url");
        if(passwordsDTO.getNotes() == null) passwordsDTO.setNotes("base notes");

        int newId = generateUniqueRandomId();
        String sql = "INSERT INTO Passwords values(null, ?, ?, ?, ?, " +
                "?, null, CURDATE(), null)";
        int rows = jdbcTemplate.update(sql, passwordsDTO.getName(), newId,
                passwordsDTO.getPassword(), passwordsDTO.getUrl(), passwordsDTO.getNotes());
        return rows == 1;
    }

    public Passwords getPassword(final int id){
            try{
                Passwords password = jdbcTemplate.queryForObject("select * from passwords where id = ?",
                        new Object[]{id}, new BeanPropertyRowMapper<>(Passwords.class));

                return password;
            } catch(EmptyResultDataAccessException e){
                return null;
            }
    }

    public List<Passwords> getAllPassword(){
        List<Passwords> passwords = jdbcTemplate.query("select * from passwords",
                new Object[]{}, new BeanPropertyRowMapper<>(Passwords.class));

        return passwords;
    }

    public boolean putPassword(PasswordsDTO passwordsDTO, final int id){
        Passwords existing = getPassword(id);

        if(existing == null){
            return false;
        }

        if(passwordsDTO.getName() != null) existing.setName(passwordsDTO.getName());
        if(passwordsDTO.getPassword() != null) existing.setPassword(passwordsDTO.getPassword());
        if(passwordsDTO.getUrl() != null) existing.setUrl(passwordsDTO.getUrl());
        if(passwordsDTO.getNotes() != null) existing.setNotes(passwordsDTO.getNotes());

        String sql = "UPDATE Passwords set name = ?, password = ?, url = ?, notes = ?, modificationdate = CURDATE() where id = ?";
        int rows = jdbcTemplate.update(sql, existing.getName(), existing.getPassword(),
                existing.getUrl(), existing.getNotes(), id);

        return rows == 1;
    }

    public boolean deletePassword(int id){
        String sql = "DELETE FROM Passwords where id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return rows == 1;
    }

    public boolean deletePasswordsOlderThan(int years) {
        String sql = "DELETE FROM Passwords WHERE deleteddate < DATE_SUB(CURDATE(), INTERVAL ? YEAR)";
        int rows = jdbcTemplate.update(sql, years);
        return rows == 1;
    }

    public boolean markForDelete(Date date, int id) {
        String sql = "UPDATE Passwords set deleteddate = ?, modificationdate = CURDATE() where id = ?";
        int rows = jdbcTemplate.update(sql, date, id);
        return rows == 1;
    }
}

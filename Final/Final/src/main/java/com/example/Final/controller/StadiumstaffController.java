package com.example.Final.controller;

import com.example.Final.model.Stadiumstaff;
import com.example.Final.services.StadiumstaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stadiumstaff")
@CrossOrigin(origins ="http://localhost:3000/")
public class StadiumstaffController {

    @Autowired
    private StadiumstaffService stadiumstaffService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Stadiumstaff> getAllStadiumstaff() {
        return stadiumstaffService.getAllStadiumstaff();
    }

    @GetMapping("/{userid}")
    public ResponseEntity<Stadiumstaff> getStadiumstaffById(@PathVariable Long userid) {
        return stadiumstaffService.getStadiumstaffById(userid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Stadiumstaff createStadiumstaff(@RequestBody Map<String, Object> payload) {
        Stadiumstaff stadiumstaff = new Stadiumstaff();
        stadiumstaff.setFirstname((String) payload.get("firstname"));
        stadiumstaff.setLastname((String) payload.get("lastname"));
        stadiumstaff.setUsername((String) payload.get("username"));
        stadiumstaff.setEmail((String) payload.get("email"));
        stadiumstaff.setPassword(passwordEncoder.encode((String) payload.get("password")));
        Long stadiumId = ((Number) payload.get("stadiumId")).longValue();
        return stadiumstaffService.createStadiumstaff(stadiumstaff, stadiumId);
    }

    @PutMapping("/{userid}")
    public ResponseEntity<Stadiumstaff> updateStadiumstaff(@PathVariable Long userid, @RequestBody Stadiumstaff stadiumstaffDetails) {
        Stadiumstaff updatedStadiumstaff = stadiumstaffService.updateStadiumstaff(userid, stadiumstaffDetails);
        if (updatedStadiumstaff != null) {
            return ResponseEntity.ok(updatedStadiumstaff);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{userid}")
    public ResponseEntity<Void> deleteStadiumstaff(@PathVariable Long userid) {
        stadiumstaffService.deleteStadiumstaff(userid);
        return ResponseEntity.noContent().build();
    }
}

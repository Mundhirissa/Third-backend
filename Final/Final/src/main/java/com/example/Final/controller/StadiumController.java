package com.example.Final.controller;

import com.example.Final.model.Stadium;
import com.example.Final.services.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stadiums")
@CrossOrigin(origins ="http://localhost:3000/")
public class StadiumController {

    @Autowired
    private StadiumService stadiumService;

    @GetMapping
    public List<Stadium> getAllStadiums() {
        return stadiumService.getAllStadiums();
    }

    @GetMapping("/{id}")
    public Optional<Stadium> getStadiumById(@PathVariable Long id) {
        return stadiumService.getStadiumById(id);
    }

    @PostMapping
    public Stadium createStadium(@RequestBody Stadium stadium) {
        return stadiumService.createStadium(stadium);
    }

    @PutMapping("/{id}")
    public Stadium updateStadium(@PathVariable Long id, @RequestBody Stadium stadiumDetails) {
        return stadiumService.updateStadium(id, stadiumDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteStadium(@PathVariable Long id) {
        stadiumService.deleteStadium(id);
    }
}

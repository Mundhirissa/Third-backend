package com.example.Final.services;

import com.example.Final.model.Stadium;
import com.example.Final.repo.Stadiumrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StadiumService {

    @Autowired
    private Stadiumrepo stadiumRepository;

    public List<Stadium> getAllStadiums() {
        return stadiumRepository.findAll();
    }

    public Optional<Stadium> getStadiumById(Long id) {
        return stadiumRepository.findById(id);
    }

    public Stadium createStadium(Stadium stadium) {
        return stadiumRepository.save(stadium);
    }

    public Stadium updateStadium(Long id, Stadium stadiumDetails) {
        Stadium stadium = stadiumRepository.findById(id).orElseThrow(() -> new RuntimeException("Stadium not found"));
        stadium.setName(stadiumDetails.getName());
        stadium.setCapacity(stadiumDetails.getCapacity());
        stadium.setLocation(stadiumDetails.getLocation());
        return stadiumRepository.save(stadium);
    }

    public void deleteStadium(Long id) {
        stadiumRepository.deleteById(id);
    }
}

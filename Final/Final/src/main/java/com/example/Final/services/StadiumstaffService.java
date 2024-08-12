package com.example.Final.services;

import com.example.Final.model.Stadium;
import com.example.Final.model.Stadiumstaff;
import com.example.Final.repo.Stadiumrepo;
import com.example.Final.repo.Stadiumstaffreop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StadiumstaffService {

    @Autowired
    private Stadiumstaffreop stadiumstaffRepository;


    @Autowired
    private Stadiumrepo stadiumRepository;



    public Optional<Stadiumstaff> findStadiumstaffByUsername(String username) {
        return stadiumstaffRepository.findByUsername(username);
    }

    public List<Stadiumstaff> getAllStadiumstaff() {
        return stadiumstaffRepository.findAll();
    }

    public Optional<Stadiumstaff> getStadiumstaffById(Long userid) {
        return stadiumstaffRepository.findById(userid);
    }

    public Stadiumstaff createStadiumstaff(Stadiumstaff stadiumstaff, Long stadiumId) {
        Stadium stadium = stadiumRepository.findById(stadiumId)
                .orElseThrow(() -> new RuntimeException("Stadium not found with id " + stadiumId));
        stadiumstaff.setStadium(stadium);
        stadiumstaff.setRole("staff");
        return stadiumstaffRepository.save(stadiumstaff);
    }



    public Stadiumstaff updateStadiumstaff(Long userid, Stadiumstaff stadiumstaffDetails) {
        Optional<Stadiumstaff> stadiumstaff = stadiumstaffRepository.findById(userid);
        if (stadiumstaff.isPresent()) {
            Stadiumstaff existingStadiumstaff = stadiumstaff.get();
            existingStadiumstaff.setFirstname(stadiumstaffDetails.getFirstname());
            existingStadiumstaff.setLastname(stadiumstaffDetails.getLastname());
            existingStadiumstaff.setUsername(stadiumstaffDetails.getUsername());
            existingStadiumstaff.setPassword(stadiumstaffDetails.getPassword());
            existingStadiumstaff.setRole(stadiumstaffDetails.getRole());
            existingStadiumstaff.setStadium(stadiumstaffDetails.getStadium());
            return stadiumstaffRepository.save(existingStadiumstaff);
        }
        return null;
    }



    public void deleteStadiumstaff(Long id) {
        stadiumstaffRepository.deleteById(id);
    }
}

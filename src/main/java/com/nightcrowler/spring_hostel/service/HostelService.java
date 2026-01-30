package com.nightcrowler.spring_hostel.service;

import com.nightcrowler.spring_hostel.model.Hostel;
import com.nightcrowler.spring_hostel.repository.HostelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HostelService {

    private final HostelRepository repo;

    public HostelService(HostelRepository repo) {
        this.repo = repo;
    }

    public List<Hostel> listAll() {
        return repo.findAll();
    }

    public List<Hostel> listAvailable() {
        return repo.findByAvailableTrue();
    }

    public Hostel save(Hostel hostel) {
        return repo.save(hostel);
    }

    public Optional<Hostel> findById(Long id) {
        return repo.findById(id);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}

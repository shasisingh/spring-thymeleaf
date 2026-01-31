package com.nightcrawler.spring.hostel.service;

import com.nightcrawler.spring.hostel.model.Allocation;
import com.nightcrawler.spring.hostel.repository.AllocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AllocationService {
    private final AllocationRepository repo;

    public void save(Allocation a) {
        repo.save(a);
    }

    public Optional<Allocation> findById(Long id) {
        return repo.findById(id);
    }

    public List<Allocation> findAll() {
        return repo.findAll();
    }

    public List<Allocation> findByHostelId(Long hostelId) {
        return repo.findByHostelIdWithRoom(hostelId);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}

package com.himachalam.aiinterview.service;

import com.himachalam.aiinterview.model.Domain;
import com.himachalam.aiinterview.repository.DomainRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DomainServiceImpl implements DomainService {

    private final DomainRepository domainRepository;

    public DomainServiceImpl(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    @Override
    public List<Domain> getAllDomains() {
        return domainRepository.findAll();
    }

    @Override
    public Domain getById(Long id) {
        return domainRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domain not found: " + id));
    }

    @Override
    public Domain save(Domain domain) {
        return domainRepository.save(domain);
    }

    @Override
    public void delete(Long id) {
        domainRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return domainRepository.existsByName(name);
    }
}

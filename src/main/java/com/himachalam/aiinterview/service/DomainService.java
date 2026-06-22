package com.himachalam.aiinterview.service;

import com.himachalam.aiinterview.model.Domain;
import java.util.List;

public interface DomainService {
    List<Domain> getAllDomains();
    Domain getById(Long id);
    Domain save(Domain domain);
    void delete(Long id);
    boolean existsByName(String name);
}

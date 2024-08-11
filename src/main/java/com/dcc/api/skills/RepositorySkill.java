package com.dcc.api.skills;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorySkill extends JpaRepository<Skill, Integer> {
    Skill findByName(String name);
    List<Skill> findAll(Specification<Skill> spec,Pageable pageable);
}

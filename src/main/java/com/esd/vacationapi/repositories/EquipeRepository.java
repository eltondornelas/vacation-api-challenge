package com.esd.vacationapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.esd.vacationapi.domain.Equipe;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Integer> {
	
	
}

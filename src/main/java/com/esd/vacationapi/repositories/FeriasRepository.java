package com.esd.vacationapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.esd.vacationapi.domain.Ferias;

@Repository
public interface FeriasRepository extends JpaRepository<Ferias, Integer> {
	

}

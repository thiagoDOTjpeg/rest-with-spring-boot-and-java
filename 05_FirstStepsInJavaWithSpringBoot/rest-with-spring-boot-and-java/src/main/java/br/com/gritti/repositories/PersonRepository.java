package br.com.gritti.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gritti.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{
	
}

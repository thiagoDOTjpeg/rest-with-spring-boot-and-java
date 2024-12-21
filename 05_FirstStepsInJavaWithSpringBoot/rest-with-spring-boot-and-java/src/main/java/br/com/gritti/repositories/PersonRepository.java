package br.com.gritti.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gritti.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
	
}

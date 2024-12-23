package br.com.gritti.services;

import java.util.List;
import java.util.logging.Logger;

import br.com.gritti.exceptions.RequiredObjectsIsNullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gritti.data.vo.v1.PersonVO;
import br.com.gritti.data.vo.v2.PersonVOV2;
import br.com.gritti.exceptions.ResourceNotFoundException;
import br.com.gritti.mapper.DozerMapper;
import br.com.gritti.mapper.custom.PersonMapper;
import br.com.gritti.model.Person;
import br.com.gritti.repositories.PersonRepository;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	@Autowired
	PersonMapper mapper;
	
	public List<PersonVO> findAll() {
		
		logger.info("Finding all people!");
		
		return DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
	}
	

	public PersonVO findByID(Long id) {
		logger.info("Finding one person!");
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		return DozerMapper.parseObjetct(entity, PersonVO.class);
	}
	
	public PersonVO update(PersonVO person) {
		if(person == null) {
			throw new RequiredObjectsIsNullException();
		}

		logger.info("Updating one person!");
		
		var entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		var vo =  DozerMapper.parseObjetct(repository.save(entity), PersonVO.class);
		
		return vo;
	}
	
	public PersonVO create(PersonVO person) {
		if(person == null) {
			throw new RequiredObjectsIsNullException();
		}

		logger.info("Creating one person!");
		var entity = DozerMapper.parseObjetct(person, Person.class);
		var vo =  DozerMapper.parseObjetct(repository.save(entity), PersonVO.class);
		return vo;
	}
	
	public PersonVOV2 createV2(PersonVOV2 person) {
		logger.info("Creating one person!");
		
		var entity = mapper.convertVoToEntity(person);
		
		var vo =  mapper.convertEntityToVo(repository.save(entity));
		
		return vo;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one person!");
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		repository.delete(entity);
	}
}

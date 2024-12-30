package br.com.gritti.services;

import br.com.gritti.controllers.PersonController;
import br.com.gritti.data.vo.v1.PersonVO;
import br.com.gritti.data.vo.v2.PersonVOV2;
import br.com.gritti.exceptions.RequiredObjectsIsNullException;
import br.com.gritti.exceptions.ResourceNotFoundException;
import br.com.gritti.mapper.DozerMapper;
import br.com.gritti.mapper.custom.PersonMapper;
import br.com.gritti.model.Person;
import br.com.gritti.repositories.PersonRepository;
import br.com.gritti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserServices implements UserDetailsService {

	private Logger logger = Logger.getLogger(UserServices.class.getName());

	UserRepository repository;

	@Autowired
	public UserServices(UserRepository repository) {
		this.repository = repository;
	}


	public PersonVO findByID(Long id) throws Exception {
		logger.info("Finding one person!");
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		PersonVO vo =  DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Finding one user by name " + username + "!");
		var user = repository.findByUsername(username);
		if(user != null) {
			return user;
		} else {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
	}
}

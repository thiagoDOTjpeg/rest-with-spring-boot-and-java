package br.com.gritti.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.gritti.data.vo.v1.PersonVO;
import br.com.gritti.data.vo.v2.PersonVOV2;
import br.com.gritti.services.PersonServices;


@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController {
	
	@Autowired
	private PersonServices service;

	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Finds a Person", description = "Finds a Person", tags = {"People"},
					responses = { @ApiResponse(description = "Success", responseCode = "200", content = {
									@Content(schema = @Schema(implementation = PersonVO.class))
					}),
									@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
									@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
									@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
									@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
									@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
					}
	)
	public PersonVO findById(@PathVariable(value = "id") Long id) throws Exception{
		
		return service.findByID(id);
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Finds all People", description = "Finds all People", tags = {"People"},
					responses = { @ApiResponse(description = "Success", responseCode = "200", content = {
									@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonVO.class)))
					}),
									@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
									@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
									@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
									@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		}
	)
	public List<PersonVO> findAll() {
		
		return service.findAll();
	}

	@CrossOrigin(origins = {"http://localhost:8080", "https://gritti.com.br"})
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Add a new Person", description = "Adds a new Person by passing in a JSON", tags = {"People"},
					responses = { @ApiResponse(description = "Success", responseCode = "200", content = {
									@Content(schema = @Schema(implementation = PersonVO.class))
					}),
									@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
									@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
									@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
					}
	)
	public PersonVO create(@RequestBody PersonVO person) throws Exception {
		
		return service.create(person);
	}
	
	@PostMapping(value = "/v2", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
	public PersonVOV2 createV2(@RequestBody PersonVOV2 person) {
		
		return service.createV2(person);
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Updates a person", description = "Updates a person by passing in a JSON", tags = {"People"},
					responses = { @ApiResponse(description = "Success", responseCode = "200", content = {
									@Content(schema = @Schema(implementation = PersonVO.class))
					}),
									@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
									@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
									@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
									@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
					}
	)
	public PersonVO update(@RequestBody PersonVO person) throws Exception {
		
		return service.update(person);
	}

	@DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Deletes a person", description = "Deletes a person by passing in a JSON", tags = {"People"},
					responses = { @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
									@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
									@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
									@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
									@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
					}
	)
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}

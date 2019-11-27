package com.mercadolivre.simios.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercadolivre.simios.repositories.SimiosRepository;
import com.mercadolivre.simios.services.ProcessaSimiosServices;
import com.mercadolivre.simos.models.Dna;

@RestController
@RequestMapping("/api/simio")
public class SimiosController {

	@Autowired
	private final ProcessaSimiosServices processaSimiosService;

	SimiosController(ProcessaSimiosServices processaSimiosService) {

		this.processaSimiosService = processaSimiosService;

	}

	@PostMapping
	public ResponseEntity<String> simio(@Valid @RequestBody Dna dna) {

		if (!processaSimiosService.isSimian(dna)) {

			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<>(HttpStatus.OK);

	}

}

/**
 * 
 */
package com.mercadolivre.simios.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Kaio
 *
 */
@Entity
@Table(name = "simios")
public class Simio {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull(message = "dna não pode ser nulo")
	@Column(name = "dna", nullable = false, unique = true)
	private String dna;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the dna
	 */
	public String getDna() {

		return dna;
	}

	/**
	 * @param dna the dna to set
	 */
	public void setDna(String dna) {

		this.dna = dna;
	}

}

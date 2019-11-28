/**
 * 
 */
package com.mercadolivre.simos.models;

import java.util.Arrays;
import java.util.List;

/**
 * @author Kaio
 *
 */
public class Dna {
	
	private List<String> dna;

	/**
	 * @return the dna
	 */
	public List<String> getDna() {
		return dna;
	}

	/**
	 * @param dna the dna to set
	 */
	public void setDna(List<String> dna) {
		this.dna = dna;
	}

	@Override
	public String toString() {
		return "Dna [dna=" + dna + "]";
	}

	
}

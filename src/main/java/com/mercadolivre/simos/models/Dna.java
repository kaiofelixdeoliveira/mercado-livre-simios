/**
 * 
 */
package com.mercadolivre.simos.models;

import java.util.Arrays;

/**
 * @author Kaio
 *
 */
public class Dna {
	
	private String [] dna;

	/**
	 * @return the dna
	 */
	public String[] getDna() {
		return dna;
	}

	/**
	 * @param dna the dna to set
	 */
	public void setDna(String[] dna) {
		this.dna = dna;
	}

	@Override
	public String toString() {
		return "Dna [dna=" + Arrays.toString(dna) + "]";
	}

}

package com.mercadolivre.simios.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mercadolivre.simios.entities.Simio;
import com.mercadolivre.simios.enums.Flux;
import com.mercadolivre.simios.repositories.SimiosRepository;
import com.mercadolivre.simios.services.ProcessaSimiosServices;
import com.mercadolivre.simos.models.Dna;

@Service
public class ProcessaSimiosServicesImpl implements ProcessaSimiosServices {

	private static final Logger log = LoggerFactory.getLogger(ProcessaSimiosServicesImpl.class);

	@Autowired
	private SimiosRepository simiosRepository;

	/*
	 * String[] dna = { "CTGAGA", "CTAGGC", "TATTGT", "AGAGGG", "CCCCTA", "TCACTG"
	 * };
	 * 
	 * if(isSimian(dna)) {
	 * 
	 * log.info("O animalzinho é simio :] "); }else {
	 * 
	 * log.info("Que pena ele é humano :[ "); } }
	 */

	public boolean isSimian(Dna dna) {

		String[][] dnaMatrix = DoMatrix(dna.getDna());

		int countWordSequenceEqualsMax = 0;

		countWordSequenceEqualsMax += readVerticalOrHorizontal(dnaMatrix, Flux.Vertical);
		countWordSequenceEqualsMax += readVerticalOrHorizontal(dnaMatrix, Flux.Horizontal);

		countWordSequenceEqualsMax += cursorDiagonalLeftToRight(dnaMatrix);
		countWordSequenceEqualsMax += cursorDiagonalRightToLeft(dnaMatrix);

		log.info("##################################################");
		log.info("Total de sequencias de quatro letras encontradas: " + countWordSequenceEqualsMax);
		log.info("##################################################");

		if (countWordSequenceEqualsMax > 1) {

			Simio simio = new Simio();

			simio.setDna(dna.toString());
			simiosRepository.save(simio);

			return true;
		}

		return false;

	}

	/***
	 * 
	 * Mounta a matriz de dna
	 * 
	 * @param dna
	 * @return
	 */
	static String[][] DoMatrix(String dna[]) {

		String dnaMatrix[][] = new String[dna.length][6];

		for (int row = 0; row < dna.length; row++) {

			int indexCol = 0;
			for (int col = 1; col <= 6; col++) {

				dnaMatrix[row][indexCol] = dna[row].substring(indexCol, col);

				System.out.print("[" + dnaMatrix[row][indexCol] + "]");

				indexCol++;

			}

			System.out.println("");

		}

		return dnaMatrix;
	}

	/**
	 * 
	 * Controla o cursor da diagonal da direita para a esquerda
	 * 
	 * @param dna
	 * @return
	 */

	static int cursorDiagonalRightToLeft(String[][] dna) {

		int countWordSequenceEqualsMax = 0;
		log.info("");
		log.info("Lendo matriz na diagonal da direita para a esquerda...");
		// ler da direita para a esquerda
		for (int cursorRow = 0; cursorRow < dna.length; cursorRow++) {

			log.info("Movendo cursor para linha: " + cursorRow);

			if (cursorRow == 0) {

				for (int cursorCol = dna.length - 1; cursorCol > 0; cursorCol--) {

					if (cursorCol >= 3) {

						countWordSequenceEqualsMax += readDiagonaisRightToLeftAndLeftToRight(cursorCol, cursorRow, dna,
								Flux.DiagonalRightToLeft);
					}
				}

			} else {

				// linhas na posição <3 não possuem mais que 3 sequencias, portanto são
				// //excluidas
				if (cursorRow < 3) {
					countWordSequenceEqualsMax += readDiagonaisRightToLeftAndLeftToRight(dna.length - 1, cursorRow, dna,
							Flux.DiagonalRightToLeft);
				}
			}

		}
		log.info(
				"Numero de sequencias repetidas encontradas na direita para a esquerda: " + countWordSequenceEqualsMax);

		return countWordSequenceEqualsMax;
	}

	/**
	 * 
	 * Controla o cursor da diagonal da esquerda para a direita
	 * 
	 * @param dna
	 * @return
	 */
	static int cursorDiagonalLeftToRight(String[][] dna) {

		int countWordSequenceEqualsMax = 0;

		log.info("Lendo matriz na diagonal da esquerda para a direita...");
		for (int cursorRow = 0; cursorRow < dna.length; cursorRow++) {

			log.info("Movendo cursor para linha: " + cursorRow);

			if (cursorRow == 0) {

				for (int cursorCol = 0; cursorCol < dna.length; cursorCol++) {

					if (cursorCol < 3) {
						countWordSequenceEqualsMax += readDiagonaisRightToLeftAndLeftToRight(cursorCol, cursorRow, dna,
								Flux.DiagonalLeftToRight);
					}
				}

			} else {

				// linhas na posição <3 não possuem mais que 3 sequencias, portanto são
				// //excluidas

				if (cursorRow < 3) {
					countWordSequenceEqualsMax += readDiagonaisRightToLeftAndLeftToRight(0, cursorRow, dna,
							Flux.DiagonalLeftToRight);
				}
			}

		}
		log.info(
				"Numero de sequencias repetidas encontradas na esquerda para a direita: " + countWordSequenceEqualsMax);

		return countWordSequenceEqualsMax;
	}

	/*
	 * static int cursorDiagonalLeftToRightOrRightToLeft(String[][] dna, Flux flux)
	 * {
	 * 
	 * int countWordSequenceEqualsMax = 0; int cursorCol = dna.length - 1;
	 * 
	 * if (flux == Flux.DiagonalLeftToRight) {
	 * 
	 * cursorCol = 0;
	 * 
	 * }
	 * 
	 * log.info("Lendo matriz " + flux.toString()); for (int cursorRow = 0;
	 * cursorRow < dna.length; cursorRow++) {
	 * 
	 * log.info("Movendo cursor para linha: " + cursorRow);
	 * 
	 * if (cursorRow == 0) {
	 * 
	 * while (cursorCol < dna.length) {
	 * 
	 * if (cursorCol < 3) { countWordSequenceEqualsMax +=
	 * readDiagonaisRightToLeftAndLeftToRight(cursorCol, cursorRow, dna, flux); } if
	 * (flux == Flux.DiagonalLeftToRight) { cursorCol++; } else { cursorCol--; } }
	 * 
	 * } else {
	 * 
	 * // linhas na posição <3 não possuem mais que 3 sequencias, portanto são //
	 * excluidas if (cursorRow < 3) { countWordSequenceEqualsMax +=
	 * readDiagonaisRightToLeftAndLeftToRight( flux == Flux.DiagonalLeftToRight ? 0
	 * : dna.length - 1, cursorRow, dna, flux); } }
	 * 
	 * } log.info("Numero de sequencias repetidas encontradas " + flux.toString() +
	 * countWordSequenceEqualsMax);
	 * 
	 * return countWordSequenceEqualsMax; }
	 */

	/**
	 * Lê a matriz em diagonal da direita para esquerda
	 * 
	 * @param cursorCol
	 * @param cursorRow
	 * @param dna
	 * @return
	 */
	static int readDiagonaisRightToLeftAndLeftToRight(int cursorCol, int cursorRow, //
			String[][] dna, Flux flux) {

		int countWordSequenceEquals = 0;
		int countWordSequenceEqualsMax = 0;
		String wordSequence = "";

		int indexCol = cursorCol;//

		for (int row = cursorRow; row < dna.length; row++) {

			if (indexCol < 0 || indexCol == dna.length) {
				break;
			}

			log.info(flux.toString() + "-L{" + row + "}: C-{" + indexCol + "} :[" + dna[row][indexCol] + "]");//

			if (wordSequence.contains(dna[row][indexCol])) {

				countWordSequenceEquals++;

			} else {

				countWordSequenceEquals = 0;
			}

			// faz três comparações para descobir uma sequencia de quatro letras
			// Exemplo: G=>G=>G=>G
			if (countWordSequenceEquals == 3) {

				countWordSequenceEquals = 0;
				countWordSequenceEqualsMax++;

			}
			wordSequence = dna[row][indexCol];

			if (flux == Flux.DiagonalRightToLeft) {
				indexCol--;
			} else {
				indexCol++;
			}

		}
		return countWordSequenceEqualsMax;

	}

	static int readVerticalOrHorizontal(String[][] dna, Flux flux) {

		int colAux = 0;
		int rowAux = 0;
		int countWordSequenceEquals = 0;
		int countWordSequenceEqualsMax = 0;
		String wordSequence = "";

		log.info("Lendo matriz na " + flux.toString());

		for (int col = 0; col < dna.length; col++) {

			for (int row = 0; row < dna.length; row++) {

				if (flux == Flux.Horizontal) {

					colAux = row;
					rowAux = col;
				} else {

					colAux = col;
					rowAux = row;
				}
				log.info(flux.toString() + "-L{" + rowAux + "}: C-{" + colAux + "} :[" + dna[rowAux][colAux] + "]");//

				if (wordSequence.contains(dna[rowAux][colAux])) {

					countWordSequenceEquals++;

				} else {

					countWordSequenceEquals = 0;
				}

				// faz três comparações para descobir uma sequencia de quatro letras
				// Exemplo: G=>G=>G=>G
				if (countWordSequenceEquals == 3) {

					countWordSequenceEquals = 0;
					countWordSequenceEqualsMax++;

				}
				wordSequence = dna[rowAux][colAux];

			}
		}

		log.info("Numero de sequencias repetidas encontradas na " + flux.toString() + ":" + countWordSequenceEqualsMax);

		return countWordSequenceEqualsMax;

	}

}

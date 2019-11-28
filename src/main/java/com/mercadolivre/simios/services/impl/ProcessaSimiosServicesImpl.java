package com.mercadolivre.simios.services.impl;

import java.util.ArrayList;
import java.util.List;

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

		List<ArrayList<String>> dnaMatrixList = DoMatrixList(dna.getDna());

		int countWordSequenceEqualsMax = 0;

		countWordSequenceEqualsMax += readVerticalOrHorizontal(dnaMatrixList, Flux.Vertical);
		countWordSequenceEqualsMax += readVerticalOrHorizontal(dnaMatrixList, Flux.Horizontal);

		countWordSequenceEqualsMax += cursorDiagonalLeftToRight(dnaMatrixList);
		countWordSequenceEqualsMax += cursorDiagonalRightToLeft(dnaMatrixList);

		log.info("--------------------------------------------------");
		log.info("Total de sequencias de quatro letras encontradas: " + countWordSequenceEqualsMax);
		log.info("--------------------------------------------------");

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
	static List<ArrayList<String>> DoMatrixList(List<String> dna) {

		System.out.println("Generating matrix...");

		List<ArrayList<String>> matrix = new ArrayList<ArrayList<String>>();

		for (int row = 0; row < dna.size(); row++) {

			ArrayList<String> rowList = new ArrayList<String>();

			int indexCol = 0;
			for (int col = 1; col <= 6; col++) {

				rowList.add(indexCol, dna.get(row).substring(indexCol, col));

				indexCol++;

			}

			matrix.add(rowList);
			System.out.println(rowList.toString());

		}
		return matrix;

	}
	

	/**
	 * 
	 * Controla o cursor da diagonal da direita para a esquerda
	 * 
	 * @param dnaMatrixList
	 * @return
	 */

	static int cursorDiagonalRightToLeft(List<ArrayList<String>> dnaMatrixList) {

		int countWordSequenceEqualsMax = 0;
		log.info("");
		log.info("Lendo matriz na diagonal da direita para a esquerda...");
		// ler da direita para a esquerda
		for (int cursorRow = 0; cursorRow < dnaMatrixList.size(); cursorRow++) {

			log.info("Movendo cursor para linha: " + cursorRow);

			if (cursorRow == 0) {

				for (int cursorCol = dnaMatrixList.size() - 1; cursorCol > 0; cursorCol--) {

					if (cursorCol >= 3) {

						countWordSequenceEqualsMax += readDiagonaisRightToLeftAndLeftToRight(cursorCol, cursorRow,
								dnaMatrixList, Flux.DiagonalRightToLeft);
					}
				}

			} else {

				// linhas na posição <3 não possuem mais que 3 sequencias, portanto são
				// //excluidas
				if (cursorRow < 3) {
					countWordSequenceEqualsMax += readDiagonaisRightToLeftAndLeftToRight(dnaMatrixList.size() - 1,
							cursorRow, dnaMatrixList, Flux.DiagonalRightToLeft);
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
	 * @param dnaMatrixList
	 * @return
	 */
	static int cursorDiagonalLeftToRight(List<ArrayList<String>> dnaMatrixList) {

		int countWordSequenceEqualsMax = 0;

		log.info("Lendo matriz na diagonal da esquerda para a direita...");
		for (int cursorRow = 0; cursorRow < dnaMatrixList.size(); cursorRow++) {

			log.info("Movendo cursor para linha: " + cursorRow);

			if (cursorRow == 0) {

				for (int cursorCol = 0; cursorCol < dnaMatrixList.size(); cursorCol++) {

					if (cursorCol < 3) {
						countWordSequenceEqualsMax += readDiagonaisRightToLeftAndLeftToRight(cursorCol, cursorRow,
								dnaMatrixList, Flux.DiagonalLeftToRight);
					}
				}

			} else {

				// linhas na posição <3 não possuem mais que 3 sequencias, portanto são
				// //excluidas

				if (cursorRow < 3) {
					countWordSequenceEqualsMax += readDiagonaisRightToLeftAndLeftToRight(0, cursorRow, dnaMatrixList,
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
	 * @param dnaMatrixList
	 * @return
	 */
	static int readDiagonaisRightToLeftAndLeftToRight(int cursorCol, int cursorRow, //
			List<ArrayList<String>> dnaMatrixList, Flux flux) {

		int countWordSequenceEquals = 0;
		int countWordSequenceEqualsMax = 0;
		String wordSequence = "";
	

		int indexCol = cursorCol;//

		for (int row = cursorRow; row < dnaMatrixList.size(); row++) {

			if (indexCol < 0 || indexCol == dnaMatrixList.size()) {
				break;
			}

			log.info(flux.toString() + "-L{" + row + "}: C-{" + cursorCol + "} :["
					+ dnaMatrixList.get(row).get(cursorCol) + "]");//

			if (wordSequence.contains(dnaMatrixList.get(row).get(indexCol))) {

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
			wordSequence = dnaMatrixList.get(row).get(indexCol);

			if (flux == Flux.DiagonalRightToLeft) {
				indexCol--;
			} else {
				indexCol++;
			}

		}
		return countWordSequenceEqualsMax;

	}

	static int readVerticalOrHorizontal(List<ArrayList<String>> dnaMatrixList, Flux flux) {

		int colAux = 0;
		int rowAux = 0;
		int countWordSequenceEquals = 0;
		int countWordSequenceEqualsMax = 0;
		String wordSequence = "";

		log.info("Lendo matriz na " + flux.toString());

		for (int col = 0; col < dnaMatrixList.size(); col++) {

			for (int row = 0; row < dnaMatrixList.size(); row++) {

				if (flux == Flux.Horizontal) {

					colAux = row;
					rowAux = col;
				} else {

					colAux = col;
					rowAux = row;
				}
				log.info(flux.toString() + "-L{" + rowAux + "}: C-{" + colAux + "} :["
						+ dnaMatrixList.get(rowAux).get(colAux) + "]");//

				if (wordSequence.contains(dnaMatrixList.get(rowAux).get(colAux))) {

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
				wordSequence = dnaMatrixList.get(rowAux).get(colAux);

			}
		}

		log.info("Numero de sequencias repetidas encontradas na " + flux.toString() + ":" + countWordSequenceEqualsMax);

		return countWordSequenceEqualsMax;

	}

}

package daoc.mpj;

import java.io.Serializable;
import java.util.Arrays;

public class MiMatriz implements Serializable {
	private static final long serialVersionUID = 1L;
	double[][] matriz;
	
	MiMatriz() {}
	MiMatriz(int fs, int cs) {
		matriz = new double[fs][cs];
	}
	MiMatriz(double[][] matriz) {
		this.matriz = matriz;
	}
	
	double[][] getMatriz() {
		return matriz;
	}
	void setMatriz(double[][] matriz) {
		this.matriz = matriz;
	}
	
	int getFilas() {
		return matriz==null ? 0 : matriz.length;
	}
	int getCols() {
		return matriz==null ? 0 : matriz[0].length;
	}
	
	double getValor(int f, int c) {
		return matriz[f][c];
	}
	void setValor(double valor, int f, int c) {
		matriz[f][c] = valor;
	}		
	
	MiMatriz getSubMatriz(int initF, int finF, int initC, int finC) {
		MiMatriz subM = new MiMatriz(finF-initF+1, finC-initC+1);
		for(int f = initF; f <= finF; f++) {
			for(int c = initC; c <= finC; c++) {
				subM.setValor(this.getValor(f, c), f-initF, c-initC);
			}
		}
		return subM;
	}
	
	void setSubMatriz(MiMatriz data, int initF, int finF, int initC, int finC) {
		for(int f = initF; f <= finF; f++) {
			for(int c = initC; c <= finC; c++) {
				this.setValor(data.getValor(f-initF, c-initC), f, c);
			}
		}
	}	
	
	MiMatriz[] divideEnBloques(int numMat, int bloque) {
		MiMatriz[] chicas = new MiMatriz[numMat];
		int counter = 0;
		for(int f = 0; f < numMat; f+=bloque) {
			for(int c = 0; c < numMat; c+=bloque) {
				chicas[counter++] = this.getSubMatriz(f, f+bloque-1, c, c+bloque-1);
			}
		}
		return chicas;
	}
	
	static MiMatriz reuneBloques(MiMatriz[] chicas) {
		int numMat = chicas.length;
		int bloque = chicas[0].getCols();
		int matLado = (int) Math.sqrt(numMat);
		int cellLado = matLado * bloque;
		MiMatriz nueva = new MiMatriz(cellLado, cellLado);
		
		int counter = 0;
		for(int f = 0; f < numMat; f+=bloque) {
			for(int c = 0; c < numMat; c+=bloque) {
				nueva.setSubMatriz(chicas[counter++], f, f+bloque-1, c, c+bloque-1);
			}
		}
		
		return nueva;
	}
	
	static MiMatriz ejemplo01 () {
		double[][] matriz = {
			{1, 2, 3, 10, 20, 30, 100, 200, 300},
			{1, 2, 3, 10, 20, 30, 100, 200, 300},
			{1, 2, 3, 10, 20, 30, 100, 200, 300},
			{4, 5, 6, 40, 50, 60, 400, 500, 600},
			{4, 5, 6, 40, 50, 60, 400, 500, 600},
			{4, 5, 6, 40, 50, 60, 400, 500, 600},
			{7, 8, 9, 70, 80, 90, 700, 800, 900},
			{7, 8, 9, 70, 80, 90, 700, 800, 900},
			{7, 8, 9, 70, 80, 90, 700, 800, 900}
		};
		return new MiMatriz(matriz);		
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < getFilas(); i++) {
			str.append(Arrays.toString(matriz[i]));
			str.append('\n');
		}
		return str.toString();
	}
}

package daoc.mpj;

import mpi.MPI;

public class MatrizBloques {

	/**
	 * Este ejemplo está armado para una matriz original de 9x9 que se divide
	 * en 9 submatrices de 3x3
	 * Requiere que haya 9 procesos (MPI.COMM_WORLD.Size)
	 * 
	 * Podría funcionar para matrices cuadradas que puedan subdividirse en un 
	 * número exacto de submatrices cuadradas. Luego debe haber un número de
	 * procesos igual al número de submatrices
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MPI.Init(args);
		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		
		int master = 0;
		int numMat = 9;
		int bloque = 3;
		MiMatriz[] chicas = new MiMatriz[numMat];
		MiMatriz[] mLocal = new MiMatriz[1];
		
		if(rank == master ) {
			MiMatriz grande = MiMatriz.ejemplo01();
			System.out.println(grande);
			System.out.println("==========");
			chicas = grande.divideEnBloques(numMat, bloque);
			for(MiMatriz m : chicas) System.out.println(m);
			System.out.println("==========");
		}
		
		MPI.COMM_WORLD.Scatter(chicas, 0, 1, MPI.OBJECT, mLocal, 0, 1, MPI.OBJECT, master);
		
		//sacamos el promedio de todos los valores en la sumatriz recibida
		double promedio = 0;
		for(int f = 0; f < mLocal[0].getFilas(); f++) {
			for(int c = 0; c < mLocal[0].getCols(); c++) {
				promedio += mLocal[0].getValor(f, c);
			}
		}
		promedio /= (mLocal[0].getFilas() * mLocal[0].getCols());
		
		//reemplazamos todos los valores de la sumatriz, con el promedio calculado
		for(int f = 0; f < mLocal[0].getFilas(); f++) {
			for(int c = 0; c < mLocal[0].getCols(); c++) {
				mLocal[0].setValor(promedio, f, c);
			}
		}		
		
		MiMatriz[] lasmatrices = new MiMatriz[numMat];		
		MPI.COMM_WORLD.Gather(mLocal, 0, 1, MPI.OBJECT, lasmatrices, 0, 1, MPI.OBJECT, master);
		
		if(rank == master ) {
			for(MiMatriz m : lasmatrices) System.out.println(m);
			System.out.println("==========");
			MiMatriz nueva = MiMatriz.reuneBloques(lasmatrices);
			System.out.println(nueva);
		}

		MPI.Finalize();
	}

}

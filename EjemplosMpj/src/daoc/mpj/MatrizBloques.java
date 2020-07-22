package daoc.mpj;

import mpi.MPI;

public class MatrizBloques {

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
			MiMatriz grande = new MiMatriz(matriz());
			chicas = grande.divideEnBloques(numMat, bloque);
			for(MiMatriz m : chicas) System.out.println(m);
		}
		
		MPI.COMM_WORLD.Scatter(chicas, 0, 1, MPI.OBJECT, mLocal, 0, 1, MPI.OBJECT, master);
		
		double promedio = 0;
		for(int f = 0; f < mLocal[0].getFilas(); f++) {
			for(int c = 0; c < mLocal[0].getCols(); c++) {
				promedio += mLocal[0].getValor(f, c);
			}
		}
		promedio /= (mLocal[0].getFilas() * mLocal[0].getCols());
		for(int f = 0; f < mLocal[0].getFilas(); f++) {
			for(int c = 0; c < mLocal[0].getCols(); c++) {
				mLocal[0].setValor(promedio, f, c);
			}
		}		
		
		MiMatriz[] lasmatrices = new MiMatriz[numMat];		
		MPI.COMM_WORLD.Gather(mLocal, 0, 1, MPI.OBJECT, lasmatrices, 0, 1, MPI.OBJECT, master);
		
		if(rank == master ) {
			for(MiMatriz m : lasmatrices) System.out.println(m);
		}
		
		//System.out.println("Hola # " + rank + " de " + size);
		MPI.Finalize();
	}

	private static double[][] matriz() {
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
		return matriz;
	}
	
}

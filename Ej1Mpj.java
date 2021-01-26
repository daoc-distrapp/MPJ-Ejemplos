import mpi.*;

public class Ej1Mpj {

	public static void main(String args[]) throws Exception {
		MPI.Init(args);
		int me = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		System.out.println("Hi from <"+me+">");
		MPI.Finalize();
	}
	
} 
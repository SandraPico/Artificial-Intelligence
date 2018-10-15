public class NextGen {
	
		private Matrix A;
		private Matrix B;
		private Matrix Initial;
	
		public NextGen(Matrix A, Matrix B, Matrix Initial) {
			this.A = A;
			this.B = B;
			this.Initial = Initial;
		}
		
		public void Calculus() {
			 
			Matrix current;
			Matrix nextgen;
			
			current = Initial.multiply(A);
			System.out.println("A * Pi:" );
			System.out.print(current.getRows());
			System.out.print(" ");
			System.out.print(current.getColumns());
			for(int i = 0; i< current.getRows(); i++ ) {
				for(int j = 0; j< current.getColumns(); j++ ) {
					System.out.print(" ");
					System.out.print(current.getMatrix()[i][j]);
					
				}
			}
			
			
			nextgen = current.multiply(B);
			System.out.print("\n\n");
			System.out.println("(A * Pi)* B:" );
			System.out.print(nextgen.getRows());
			System.out.print(" ");
			System.out.print(nextgen.getColumns());
			for(int i = 0; i< nextgen.getRows(); i++ ) {
				for(int j = 0; j< nextgen.getColumns(); j++ ) {
					System.out.print(" ");
					System.out.print(nextgen.getMatrix()[i][j]);
					
				}
			}
			
		}
}

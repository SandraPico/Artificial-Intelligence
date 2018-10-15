import java.util.List;

public class Matrix {
	
	private int rows, columns;
	private double[][] matrix;
	
	 public Matrix() {
		 
	 }
	 
	 public Matrix(int rows, int columns) {
		 this.rows = rows;
		 this.columns = columns;
		 matrix = new double[rows][columns];
		 for ( int i = 0; i < rows; i ++ ) {
			 for (int j = 0; j< columns; j++) {
				 matrix[i][j] = 0.0;
			 }
		 }
	}

	public double[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(double[][] matrix) {
		this.matrix = matrix;
	}
	 
	public void AddElements(List<Double> data) {
		int num = 0;
		Matrix aux =  this;
		for(int i= 0; i< rows ; i++ ) {
			for(int j = 0; j < columns; j++ ) {
				matrix[i][j] = data.get(num);
				num = num + 1;
			}
		}	
	}
	
	
	//Multiply 2 vectors. C = A*B
	public Matrix multiplyVector(Matrix B) {
		Matrix A = this;
		Matrix aux = new Matrix(A.getColumns(),1);

		for (int i = 0; i< A.getColumns(); i++) {
			aux.getMatrix()[i][0] = B.getMatrix()[i][0] * A.getMatrix()[0][i];
		}
		return aux;
	}
	
	
	public Matrix multiplycolumnVector(Matrix B) {
		Matrix A = this;
		Matrix aux = new Matrix(A.getRows(),1);
		for (int i = 0; i< A.getRows(); i++) {
			aux.getMatrix()[i][0] = B.getMatrix()[i][0] * A.getMatrix()[i][0];
		}
		return aux;
	}
	
	
	// multiply 2 matrix. C = A * B
	public Matrix multiply(Matrix B) {
       Matrix A = this;
       Matrix C = new Matrix(A.getRows(), B.getColumns());
       for (int i = 0; i < C.getRows(); i++) {
           for (int j = 0; j < C.getColumns(); j++) {
               for (int k = 0; k < A.getColumns(); k++) {
            	   	 
                   C.getMatrix()[i][j] += (A.getMatrix()[i][k] * B.getMatrix()[k][j]);
               }
           }
		}	    
       return C;
   }
	
	
	
	

	
	// return an specific column for a Matrix
	public Matrix column(int numColumn) {
		Matrix auxB = this;
		Matrix vector = new Matrix(auxB.getRows(),1);
		for (int i = 0; i < auxB.getRows(); i++) {
			vector.getMatrix()[i][0] = auxB.getMatrix()[i][numColumn];
		}
		return vector;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}
	
	
	
}
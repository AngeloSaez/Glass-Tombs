package utilmisc;

public class Vect2D {

	public double i;
	public double j;
	
	public Vect2D() {
		i = 0;
		j = 0;
	}
	
	public Vect2D(double i) {
		this.i = i;
		this.j = i;
	}
	
	public Vect2D(double i, double j) {
		this.i = i;
		this.j = j;
	}
	
	public void set(double i, double j) { 
		this.i = i;
		this.j = j;
	}
	
	public void set(Vect2D vec) { 
		this.i = vec.i;
		this.j = vec.j;
	}
	
	
}

package java_statistics;

public class ArithmeticAverage extends Thread {

	private double[] doubleVec;
	
	private int limit_low, limit_high;
	
	private double sum;
	
	
	public ArithmeticAverage(double[] doubleVec, int limit_low, int limit_high){
		this.doubleVec = doubleVec;
		this.limit_low = limit_low;
		this.limit_high = limit_high;
		this.sum = 0.0;
	}
	
	public void run(){
		
		for(int i = limit_low; i < limit_high; i++){
			sum += doubleVec[i]; 
		}
	}
	
	public double getSum(){
		return this.sum;
	}

}

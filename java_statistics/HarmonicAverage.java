package java_statistics;

public class HarmonicAverage extends Thread {

	private double[] doubleVec;
	
	private int limit_low, limit_high;
	
	private double inv_sum;
	
	
	public HarmonicAverage(double[] doubleVec, int limit_low, int limit_high){
		this.doubleVec = doubleVec;
		this.limit_low = limit_low;
		this.limit_high = limit_high;
		this.inv_sum = 0.0;
	}
	
	public void run(){

		for(int i = limit_low; i < limit_high; i++){
			inv_sum += 1.0/doubleVec[i]; 
		}
	}
	
	public double getInvSum(){
		return this.inv_sum;
	}

}

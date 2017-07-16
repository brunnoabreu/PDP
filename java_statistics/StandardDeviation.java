package java_statistics;

public class StandardDeviation extends Thread {

	private double[] doubleVec;

	private double avg;
	
	private int limit_low, limit_high;
	
	private double sumdiff_square;
	
	
	public StandardDeviation(double[] doubleVec, int limit_low, int limit_high){
		this.doubleVec = doubleVec;
		this.avg = 0.0;
		this.limit_low = limit_low;
		this.limit_high = limit_high;
		this.sumdiff_square = 0.0;
	}
	
	public void setAvg(double avg){
		this.avg = avg;
	}
	
	public void run(){
		
		for(int i = limit_low; i < limit_high; i++){
			sumdiff_square += Math.pow(doubleVec[i] - avg, 2); 
		}
	}
	
	public double getSumdiff_square(){
		return this.sumdiff_square;
	}

}

import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java_statistics.*;
import java.util.Scanner;
import java.util.Locale;

public class CallOps {


	public static void main(String[] args){
		
		double[] doubleVec;
		int nr_elem, nr_threads, prog, model;
		
		int low_rand = 1;
		int high_rand = 100;

		double avg = 0.0;
		double std_dev = 0.0;

		double startTime = 0;
		double stopTime = 0;
		double elapsedTime = 0;

		if(args.length < 4){
			System.out.println("Minimo 4 args");
		}else{
	

			//Pega os parametros de execucao		
			nr_elem = Integer.parseInt(args[0]);
			nr_threads = Integer.parseInt(args[1]);
			prog = Integer.parseInt(args[2]);
			model = Integer.parseInt(args[3]);
			
			int chunk_size = (int) nr_elem / nr_threads;
			int rest_chunk = nr_elem % nr_threads;


			//Leitura do arquivo			
			doubleVec = new double[nr_elem];
			File in_f = new File("files_mat/mat_" + args[0]  + ".txt");
			Scanner sc = null;
/*
			for(int i = 0; i < doubleVec.length; i++){
				double rand = new Random().nextDouble();
				doubleVec[i] = low_rand + (rand * (high_rand - low_rand));
			}
*/

			try {
				sc = new Scanner(new FileReader(in_f));
				sc.useLocale(Locale.US);
				String test;
				int j = 0;

				while(sc.hasNext()){
					doubleVec[j] = sc.nextDouble();
					//System.out.println(doubleVec[i]);
					j++;
				}
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}finally{
				if(sc != null){
					sc.close();
				}
			}
/*
			for(double elem : doubleVec){
				System.out.println(elem);
			}
*/
			if(model == 1){
				if(prog == 0){
					startTime = System.currentTimeMillis();
					for(int i = 0; i < doubleVec.length; i++){
						avg += doubleVec[i];
					}
					avg /= nr_elem;
					stopTime = System.currentTimeMillis();
				}else{
					if(prog == 1){

					}else{
						if(prog == 2){
							startTime = System.currentTimeMillis();
							for(int i = 0; i < doubleVec.length; i++){
								avg += 1.0/doubleVec[i];
							}
							avg = nr_elem / avg;
							stopTime = System.currentTimeMillis();
						}else{
							startTime = System.currentTimeMillis();
							for(int i = 0; i < doubleVec.length; i++){
								avg += doubleVec[i];
							}
							avg /= nr_elem;
							
							for(int i = 0; i < doubleVec.length; i++){
								std_dev += Math.pow(doubleVec[i] - avg,2);
							}
							std_dev /= nr_elem;
							std_dev = Math.sqrt(std_dev);
							stopTime = System.currentTimeMillis();
						}
					}
				}
			}else{
				if(prog == 0){
					ArithmeticAverage[] part_values = new ArithmeticAverage[nr_threads];
					
					for(int i = 0; i < nr_threads; i++){

//						System.out.println(i*chunk_size+((i!=0)?rest_chunk:0));
//						System.out.println((i+1)*chunk_size+rest_chunk);
//						System.out.println();

						part_values[i] = new ArithmeticAverage(doubleVec, i * chunk_size + ((i != 0) ? rest_chunk : 0), (i+1)*chunk_size + rest_chunk);
					}

					startTime = System.nanoTime();

					for(int i = 0; i < nr_threads; i++){
						part_values[i].start();
					}

					try{
						for(ArithmeticAverage partial : part_values){
							partial.join();
						}
					}catch(InterruptedException ex){ }


					for(ArithmeticAverage partial : part_values){
						avg += partial.getSum();
					}

					avg /= nr_elem;
					
					stopTime = System.nanoTime();

				}else{
					if(prog == 1){

					}else{
						if(prog == 2){
							HarmonicAverage[] part_values = new HarmonicAverage[nr_threads];
			
							for(int i = 0; i < nr_threads; i++){
								part_values[i] = new HarmonicAverage(doubleVec, i * chunk_size + ((i != 0) ? rest_chunk : 0), (i+1)*chunk_size + rest_chunk);
							}

							startTime = System.currentTimeMillis();

							for(int i = 0; i < nr_threads; i++){
								part_values[i].start();
							}

							try{
								for(HarmonicAverage partial : part_values){
									partial.join();
								}
							}catch(InterruptedException ex){

							}

							for(HarmonicAverage partial : part_values){
								avg += partial.getInvSum();
							}

							avg = nr_elem/avg;

							stopTime = System.currentTimeMillis();

						}else{


							ArithmeticAverage[] part_values = new ArithmeticAverage[nr_threads];
							StandardDeviation[] part_devs = new StandardDeviation[nr_threads];

							for(int i = 0; i < nr_threads; i++){
								part_values[i] = new ArithmeticAverage(doubleVec, i * chunk_size + ((i != 0) ? rest_chunk : 0), (i+1)*chunk_size + rest_chunk);
								part_devs[i] = new StandardDeviation(doubleVec, i * chunk_size + ((i != 0) ? rest_chunk : 0), (i+1)*chunk_size + rest_chunk);
							}

							startTime = System.currentTimeMillis();

							for(int i = 0; i < nr_threads; i++){
								part_values[i].start();
							}

							try{
								for(ArithmeticAverage partial : part_values){
									partial.join();
								}
							}catch(InterruptedException ex){

							}

							for(ArithmeticAverage partial : part_values){
								avg += partial.getSum();
							}

							avg /= nr_elem;

							for(int i = 0; i < nr_threads; i++){
								part_devs[i].setAvg(avg);
								part_devs[i].start();
							}

							try{
								for(StandardDeviation partial : part_devs){
									partial.join();
								}
							}catch(InterruptedException ex){

							}

							for(StandardDeviation partial : part_devs){
								std_dev += partial.getSumdiff_square();
							}

							std_dev = Math.sqrt((std_dev/nr_elem));

							stopTime = System.currentTimeMillis();

						}
					}
				}
			}
/*
			System.out.println("Valores:");

			for(double elem : doubleVec){
				System.out.print(String.valueOf(elem) + ", ");
			}
			System.out.println();

			System.out.println("Media: " + String.valueOf(avg));
			System.out.println("Std_dev: " + String.valueOf(std_dev));
*/
			elapsedTime = stopTime - startTime;
			System.out.println(elapsedTime/1000000000.0);
			
		}
	
	}

}

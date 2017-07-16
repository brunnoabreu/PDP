#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>
#include <omp.h>

#define MIN_ARGS 4

double calculateArithmeticAverage(double *vector, int vectorSize);
double calculateHarmonicAverage(double *vector, int vectorSize);
double calculateStandardDev(double *vector, int vectorSize);
double seqCalculateArithmeticAverage(double *vector, int vectorSize);
double seqCalculateHarmonicAverage(double *vector, int vectorSize);
double seqCalculateStandardDev(double *vector, int vectorSize);
void printError();

int main (int argc, char *argv[])
{
	//main variables
	int i=0;
	double startTime; double endTime; double timeSpent; double value;
	FILE* f_in;
	time_t start, end;
	//ORDER: SIZE, NR_THREADS, PROG, MODEL (PARALLEL OR SEQ)

	if(argc<=MIN_ARGS)
		printError();
	else
	{


		char *vectorSizeString = (argv[1]);
		int vectorSize = atoi(vectorSizeString);

		char *numberOfThreadsString = (argv[2]);
		int numberOfThreads = atoi(numberOfThreadsString);

		char *progString = (argv[3]);
		int prog = atoi(progString);

		char *modelString = (argv[4]);
		int model = atoi(modelString);

		double *doubleVector;

		doubleVector = malloc(vectorSize*sizeof(double));

		omp_set_dynamic(0);
		omp_set_num_threads(numberOfThreads);

		char name_file[40] = "files_mat/mat_";

		strcat(name_file,argv[1]);
		strcat(name_file,".txt");

		//printf("%s\n",name_file);

		f_in = fopen(name_file,"r");
		for(i=0; i<vectorSize; i++)
			fscanf(f_in, "%lf", &doubleVector[i]);
		
		fclose(f_in);

		if(model == 0){
			if(prog == 0){
				startTime = omp_get_wtime();
				value = calculateArithmeticAverage(doubleVector, vectorSize);
				endTime = omp_get_wtime();
			}else{
				if(prog == 2){
					startTime = omp_get_wtime();
					value = calculateHarmonicAverage(doubleVector, vectorSize);
					endTime = omp_get_wtime();
				}else{
					startTime = omp_get_wtime();
					value = calculateStandardDev(doubleVector, vectorSize);
					endTime = omp_get_wtime();
				}
			}
			timeSpent = ((double)(endTime-startTime));
		}else{
			if(prog == 0){
				startTime = omp_get_wtime();
				value = seqCalculateArithmeticAverage(doubleVector, vectorSize);
				endTime = omp_get_wtime();
			}else{
				if(prog == 2){
					startTime = omp_get_wtime();
					value = seqCalculateHarmonicAverage(doubleVector, vectorSize);
					endTime = omp_get_wtime();
				}else{
					startTime = omp_get_wtime();
					value = seqCalculateStandardDev(doubleVector, vectorSize);
					endTime = omp_get_wtime();
				}
			}
			timeSpent = ((double)(endTime-startTime));
		}
		printf("%f", timeSpent);
		//printf("%f\n", value);
	}
}


double calculateArithmeticAverage(double *vector, int vectorSize)
{
	int i=0;
	double average;
	double total = 0.0;
	
	#pragma omp parallel for shared(vector) reduction (+: total)
	for(i=0; i<vectorSize; i++)
		total += vector[i];
	average = total/((double)vectorSize);
	
	return average;
}

double calculateHarmonicAverage(double *vector, int vectorSize)
{
	int i = 0;
	double average;
	double total = 0.0;

	#pragma omp parallel for shared(vector) reduction (+: total)
	for(i=0; i<vectorSize; i++)
		total += 1.0/vector[i];
	average = ((double)vectorSize)/total;

	return average;
}

double calculateStandardDev(double *vector, int vectorSize)
{
        int i=0; double aux = 0.0;
        double average = calculateArithmeticAverage(vector, vectorSize); double standardDeviation = 0.0;

        #pragma omp parallel for shared(vector) reduction (+: standardDeviation)
        for(i=0; i<vectorSize; i++)
                standardDeviation += ((vector[i]-average)*(vector[i]-average));
        standardDeviation = sqrt(standardDeviation/((double)vectorSize));

        return standardDeviation;
}

double seqCalculateArithmeticAverage(double *vector, int vectorSize)
{
	int i=0;
	double average;
	double total = 0.0;
	
	for(i=0; i<vectorSize; i++)
		total += vector[i];
	average = total/((double)vectorSize);
	
	return average;
}

double seqCalculateHarmonicAverage(double *vector, int vectorSize)
{
	int i = 0;
	double average;
	double total = 0.0;

	for(i=0; i<vectorSize; i++)
		total += 1.0/vector[i];
	average = ((double)vectorSize)/total;

	return average;
}

double seqCalculateStandardDev(double *vector, int vectorSize)
{
        int i=0; double aux = 0.0;
        double average = seqCalculateArithmeticAverage(vector, vectorSize); double standardDeviation = 0.0;

        for(i=0; i<vectorSize; i++)
                standardDeviation += ((vector[i]-average)*(vector[i]-average));
        standardDeviation = sqrt(standardDeviation/((double)vectorSize));

        return standardDeviation;
}

void printError()
{
	printf("\nUSAGE IS WRONG\n");
}

package arima;

import timeSeries.Observation;
import java.util.Arrays;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public class AR {
	
	
	// Set p previous observation values
	
	public static void setPrevPValues(Observation[] observations, int p) {
		for (int i = p; i < observations.length; i++) {
			double[] prevPValues = new double[p];
			for (int j = 1; j <= p; j++) {
				prevPValues[j - 1] = observations[i - j].getValue();
				observations[i].setPrevPValues(prevPValues);
			}
		}
	}

	public static double[] createOLSData(Observation[] observations, int p){
		double data[] = new double[0];
		int j = 0;
		// For loop over all observation there index > p
		for (int i = p; i < observations.length; i++) {
			// Enhance data array
			data = Arrays.copyOf(data, data.length+p+1);
			// Store values of Observations
			data[j] = observations[i].getValue();
			j++;
			for (int k = 0; k < p; k++) {
				data[j] = observations[i].getPrevPValues()[k];
				j++;
			}

		}
		return data;
	}
	
	
	public static void setupOLS(Observation[] observations, int p) {
		double[] data = createOLSData(observations, p);

		// New Multiple Linear Regression Model, solve with OLS.
		OLSMultipleLinearRegression OLS = new OLSMultipleLinearRegression();
		OLS.newSampleData(data, observations.length-p, p);
		
		double[] para = OLS.estimateRegressionParameters();
		for(int i = 0; i < p; i++){
			observations[i].setError(0);
		}
		for(int i = p; i <observations.length; i++){
			double pred = para[0];
			for(int j = 0; j == p; j++) {
				pred += observations[i].getPrevPValues()[j]*para[j+1];
			}
		observations[i].setPrediction(pred);
		observations[i].setError();
		}
	}
	
}


	
	
	
	
	
// *********************** OLD ***********************************
/*	
	
	// Create Array for OLS Format (y1,x1[1],x1[2],...,x1[p],y2,x2[1]...)
	public static double[] OLSDataArray(Observation[] observations, int p) {
		// Initialize array of doubles to store regression-variables
		double[] data = new double[0];
		int j = 0;
		// For loop over all observation there index > p
		for (int i = p; i < observations.length; i++) {
			// Enhance data array
			data = Arrays.copyOf(data, data.length + p + 1);
			// Store values of Observations
			for (int q = 0; q <= p; q++) {
				data[j] = observations[i - q].getValue();
				j++;
			}

		}
		return data;
	}

	// Method to set starting Errors
	public static void errorSetupOLS(Observation[] observations, int p) {
		double[] data = OLSDataArray(observations, p);

		// New Multiple Linear Regression Model, solve with OLS.
		OLSMultipleLinearRegression OLS = new OLSMultipleLinearRegression();
		OLS.newSampleData(data, observations.length - p, p);
		double[] estResid = OLS.estimateResiduals();

		for (int i = 0; i < p; i++) {
			observations[i].setError(0);
		}
		for (int i = p; i < observations.length; i++) {
			observations[i].setError(estResid[i - p]);
		}

	}
}
*/
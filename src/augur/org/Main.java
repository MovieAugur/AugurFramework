package augur.org;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient;
import com.amazonaws.services.elasticmapreduce.model.AddJobFlowStepsRequest;
import com.amazonaws.services.elasticmapreduce.model.AddJobFlowStepsResult;
import com.amazonaws.services.elasticmapreduce.model.HadoopJarStepConfig;
import com.amazonaws.services.elasticmapreduce.model.StepConfig;
import com.amazonaws.services.elasticmapreduce.util.StepFactory;

public class Main {

	/**
	 * 
	 * 
	 * @param args
	 *            args[0] mode - 't'/'p' args[1] movie - if 'p' args[2] new
	 */
	public static void main(String[] args) {
		if (args.length < 1 || args.length > 2) {
			System.out
					.println("Usage: \"<exec> t\" or \"<exec> p <moviename> [new]\"");
			return;
		}
		if (args[0].equals("t")) {
			// Training mode
			/*
			 * Get a list of movies, and compute metrics for movies not in the
			 * DB.
			 * 
			 * 1. Get a list of movies (from wiki or recents). 2. Check in DB
			 * for each Moviename in list. If movieName in DB, remove it from
			 * the list. 3. Load data for the movies from various sources and
			 * write to S3. 4. Run EMR on the data from S3. 5. Aggregate the
			 * data and load it in Hive DB.
			 */
			// 1, 2 later
			
			List<String> movieList = new ArrayList<String>();
			
			movieList.add("American Hustle");
			
			// 3
			/*
			String cmd = "java -jar Youtube.jar";
			for(String movie : movieList) {
				cmd = cmd + " " + movie;
			}
			try {
				String line;
				Process p = Runtime.getRuntime().exec(cmd);
				BufferedReader bri = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				while ((line = bri.readLine()) != null) {
			        System.out.println(line);
			      }
			      bri.close();
			      p.waitFor();
			} catch (IOException e) {
				System.out.println("Error in YT: " + e.getMessage());
				return;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cmd = "java -jar RottenTomatoes.jar";
			for(String movie : movieList) {
				cmd = cmd + " " + movie;
			}
			try {
				Process p = Runtime.getRuntime().exec(cmd);
				p.waitFor();
			} catch (IOException e) {
				System.out.println("Error in RT: " + e.getMessage());
				return;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cmd = "java -jar Twitter.jar";
			for(String movie : movieList) {
				cmd = cmd + " " + movie;
			}
			try {
				Process p = Runtime.getRuntime().exec(cmd);
				p.waitFor();
			} catch (IOException e) {
				System.out.println("Error in Twitter: " + e.getMessage());
				return;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			// 4
			AWSCredentials credentials = null;
			try {
			    credentials = new PropertiesCredentials(
			        Main.class.getResourceAsStream("AwsCredentials.properties"));
			} catch (IOException e1) {
			    System.out.println("Credentials were not properly entered into AwsCredentials.properties.");
			    System.out.println(e1.getMessage());
			    System.exit(-1);
			}
			
			AmazonElasticMapReduce client = new AmazonElasticMapReduceClient(credentials);

		    // predefined steps. See StepFactory for list of predefined steps
		    StepConfig hive = new StepConfig("Hive", new StepFactory().newInstallHiveStep());

		    // A custom step
		    HadoopJarStepConfig hadoopConfig1 = new HadoopJarStepConfig()
		        .withJar("s3://augur/bin/mapreduce.jar")
		        .withArgs("s3://augur/mapreduceInput", "s3://augur/mapReduceOutput"); // optional list of arguments
		    StepConfig customStep = new StepConfig("Step1", hadoopConfig1);

		    AddJobFlowStepsResult result = client.addJobFlowSteps(new AddJobFlowStepsRequest()
		        .withJobFlowId("j-1HTE8WKS7SODR")
		        .withSteps(hive, customStep));
		    System.out.println(result.getStepIds());
			
			
		} else if (args[0].equals("p")) {
			if (args.length > 3) {
				System.out
						.println("Usage: \"<exec> t\" or \"<exec> p <moviename> [new]\"");
				return;
			}
			String movieName = args[1];
			boolean rePredict = args.length == 3;
			// Predict mode
			/*
			 * Predict the movie box office collections
			 * 
			 * 1. If movie is in DB, load the already calculated collection and
			 * display the result if rePrediction is not requested. ELse, go to
			 * step 2. 2. Load data for the movie from various sources and write
			 * to S3. 3. Run EMR on the data from S3. 4. Aggregate the data, and
			 * load it in Hive DB, along with the prediction info. Display the
			 * prediction.
			 */
		} else {
			System.out
					.println("Usage: \"<exec> t\" or \"<exec> p <moviename> [new]\"");
		}
	}

}

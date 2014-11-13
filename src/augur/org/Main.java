package augur.org;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
			String cmd = "java -jar YouTube.jar";
//			List<String> cmd = new ArrayList<String>(Arrays.asList("/bin/bash", "java", "-jar ", "YouTube.jar"));
//			List<String> cmd = new ArrayList<String>(Arrays.asList("/bin/bash", "-c", "touch", "asdasdasd.txt"));
			for(String movie : movieList) {
				cmd = cmd + " " + movie;
			}
			try {
				String line;
				Process p = Runtime.getRuntime().exec(cmd);
				BufferedReader bri = new BufferedReader
				        (new InputStreamReader(p.getErrorStream()));
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
			} catch (IOException e) {
				System.out.println("Error in RT: " + e.getMessage());
				return;
			}
			cmd = "java -jar Twitter.jar";
			for(String movie : movieList) {
				cmd = cmd + " " + movie;
			}
			try {
				Process p = Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				System.out.println("Error in Twitter: " + e.getMessage());
				return;
			}
			
			// 4
			
			
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

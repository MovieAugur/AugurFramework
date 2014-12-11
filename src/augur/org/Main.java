package augur.org;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient;
import com.amazonaws.services.elasticmapreduce.model.HadoopJarStepConfig;
import com.amazonaws.services.elasticmapreduce.model.JobFlowInstancesConfig;
import com.amazonaws.services.elasticmapreduce.model.RunJobFlowRequest;
import com.amazonaws.services.elasticmapreduce.model.RunJobFlowResult;
import com.amazonaws.services.elasticmapreduce.model.StepConfig;
import com.amazonaws.services.elasticmapreduce.util.StepFactory;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class Main {

	/**
	 * 
	 * 
	 * @param args
	 *            args[0] mode - 't'/'p' args[1] movie - if 'p' args[2] new
	 */
	public static void main(String[] args) {
		runDemo(args);
		return;
		// if (args.length < 1) {
		// System.out
		// .println("Usage: \"<exec> <t> [<year><skip>]\" or \"<exec> p <moviename> [new]\"");
		// return;
		// }
		// if (args[0].equals("t")) {
		// // Training mode
		// /*
		// * Get a list of movies, and compute metrics for movies not in the
		// * DB, and store in the DB.
		// *
		// * 1. Get a list of movies (from wiki or recents). 2. Check in DB
		// * for each Moviename in list. If movieName in DB, remove it from
		// * the list. 3. Load data for the movies from various sources and
		// * write to S3. 4. Run EMR on the data from S3. 5. Aggregate the
		// * data and load it in Hive DB.
		// */
		// // 1, 2
		// int year = 2013;
		// int skip = 0;
		// if (args.length > 1) {
		// year = Integer.parseInt(args[1]);
		// skip = Integer.parseInt(args[2]);
		// }
		// List<String> movieList = new ArrayList<String>();
		//
		// AWSCredentials credentials = null;
		// try {
		// credentials = new ProfileCredentialsProvider().getCredentials();
		// } catch (Exception e) {
		// throw new AmazonClientException(
		// "Cannot load credentials from the file", e);
		// }
		//
		// AmazonS3Client s3 = new AmazonS3Client(credentials);
		//
		// System.out.println("Loading RT JAR...");
		// File rtJarFile = new File("RottenTomatoes.jar");
		// s3.getObject(new GetObjectRequest("augurframework",
		// "bin/RottenTomatoes.jar"), rtJarFile);
		//
		// System.out.println("Loading Twitter JAR...");
		// File twitJarFile = new File("twitter.jar");
		// s3.getObject(new GetObjectRequest("augurframework",
		// "bin/twitter.jar"), twitJarFile);
		//
		// File file = new File("movieList.txt");
		//
		// String s3filename = "trainingmovielist/movies" + year;
		// System.out.println("Retrieving: " + s3filename);
		// GetObjectRequest objReq;
		// try {
		// objReq = new GetObjectRequest("augurframework", s3filename);
		// } catch (Exception e) {
		// System.out.println("No year data. Aborting!");
		// return;
		// }
		//
		// ObjectMetadata meta = s3.getObject(objReq, file);
		// movieList.clear();
		// try {
		// List<String> fullMovieList = Files.readAllLines(file.toPath(),
		// Charset.defaultCharset());
		// movieList = fullMovieList.subList(skip, fullMovieList.size());
		// System.out.println(movieList);
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// return;
		// }
		// if (meta != null) {
		// // List<String> rtList = new ArrayList<String>(movieList);
		// // List<String> twitList = new ArrayList<String>(movieList);
		// Iterator<String> rtIter = movieList.iterator();
		// Iterator<String> twIter = movieList.iterator();
		// while (twIter.hasNext()) {
		// String twitCmd =
		// "java -jar twitter.jar augurframework mapreduceInput/train";
		// for (int i = 0; i < 3 && twIter.hasNext(); i++) {
		// String movie = twIter.next();
		// twitCmd = twitCmd + " " + movie.replaceAll("\\s", "_");
		// System.out.println(movie);
		// }
		// try {
		// boolean fault = false;
		// String line;
		// System.out.println(twitCmd);
		// Process p = Runtime.getRuntime().exec(twitCmd);
		// BufferedReader bri = new BufferedReader(
		// new InputStreamReader(p.getInputStream()));
		// BufferedReader bre = new BufferedReader(
		// new InputStreamReader(p.getErrorStream()));
		// while ((line = bri.readLine()) != null) {
		// System.out.println(line);
		// String error;
		// if ((error = bre.readLine()) != null) {
		// System.out.println(error);
		// fault = true;
		// break;
		// }
		// }
		// if (fault) {
		// return;
		// }
		// bre.close();
		// bri.close();
		// p.waitFor();
		// } catch (IOException e) {
		// System.out.println("Error in Twitter: "
		// + e.getMessage());
		// return;
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// return;
		// }
		// // Sleep 600,000 ms
		// try {
		// System.out.println("Done 1.");
		// Thread.sleep(300000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// return;
		// }
		//
		// String rtCmd =
		// "java -jar RottenTomatoes.jar augurframework mapreduceInput/train";
		// for (int i = 0; i < 5 && rtIter.hasNext(); i++) {
		// String movie = rtIter.next();
		// rtCmd = rtCmd + " " + movie.replaceAll("\\s", "_");
		// System.out.println(movie);
		// }
		// try {
		// boolean fault = false;
		// String line;
		// System.out.println(rtCmd);
		// Process p = Runtime.getRuntime().exec(rtCmd);
		// BufferedReader bri = new BufferedReader(
		// new InputStreamReader(p.getInputStream()));
		// BufferedReader bre = new BufferedReader(
		// new InputStreamReader(p.getErrorStream()));
		// while ((line = bri.readLine()) != null) {
		// System.out.println(line);
		// String error;
		// if ((error = bre.readLine()) != null) {
		// System.out.println(error);
		// fault = true;
		// break;
		// }
		// }
		// if (fault) {
		// return;
		// }
		// bre.close();
		// bri.close();
		// p.waitFor();
		// } catch (IOException e) {
		// System.out.println("Error in RT: " + e.getMessage());
		// return;
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// return;
		// }
		// // Sleep 600,000 ms
		// try {
		// System.out.println("Done 1.");
		// Thread.sleep(300000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// return;
		// }
		// }
		//
		// }
		// System.exit(0);
		// }
		// // 3
		// /*
		// * String cmd = "java -jar Youtube.jar"; for(String movie :
		// * movieList) { cmd = cmd + " " + movie; } try { String line;
		// * Process p = Runtime.getRuntime().exec(cmd); BufferedReader bri =
		// * new BufferedReader(new InputStreamReader(p.getErrorStream()));
		// * while ((line = bri.readLine()) != null) {
		// * System.out.println(line); } bri.close(); p.waitFor(); } catch
		// * (IOException e) { System.out.println("Error in YT: " +
		// * e.getMessage()); return; } catch (InterruptedException e) { //
		// * TODO Auto-generated catch block e.printStackTrace(); } cmd =
		// * "java -jar RottenTomatoes.jar"; for(String movie : movieList) {
		// * cmd = cmd + " " + movie; } try { Process p =
		// * Runtime.getRuntime().exec(cmd); p.waitFor(); } catch (IOException
		// * e) { System.out.println("Error in RT: " + e.getMessage());
		// * return; } catch (InterruptedException e) { // TODO Auto-generated
		// * catch block e.printStackTrace(); } cmd = "java -jar Twitter.jar";
		// * for(String movie : movieList) { cmd = cmd + " " + movie; } try {
		// * Process p = Runtime.getRuntime().exec(cmd); p.waitFor(); } catch
		// * (IOException e) { System.out.println("Error in Twitter: " +
		// * e.getMessage()); return; } catch (InterruptedException e) { //
		// * TODO Auto-generated catch block e.printStackTrace(); }
		// */
		//
		// // 4
		// if(args[0].equals("m")) {
		// AWSCredentials credentials = null;
		// try {
		// credentials = new ProfileCredentialsProvider().getCredentials();
		// } catch (Exception e) {
		// throw new AmazonClientException(
		// "Cannot load credentials from the file", e);
		// }
		// AmazonElasticMapReduce client = new AmazonElasticMapReduceClient(
		// credentials);
		//
		// // predefined steps. See StepFactory for list of predefined steps
		// StepConfig hive = new StepConfig("Hive",
		// new StepFactory().newInstallHiveStep());
		//
		// StepConfig debug = new StepConfig("Debug",
		// new StepFactory().newEnableDebuggingStep());
		//
		// // A custom step
		// HadoopJarStepConfig hadoopConfig1 = new HadoopJarStepConfig()
		// .withJar("s3://augurframework/bin/AugurMapreduce.jar")
		// .withArgs("s3://augurframework/" + args[1],
		// "s3://augurframework/outputs/" + args[2]); // optional
		// // list of
		// // arguments
		// StepConfig customStep = new StepConfig("augurTrain", hadoopConfig1);
		//
		// RunJobFlowResult result = client.runJobFlow(new RunJobFlowRequest()
		// .withSteps(debug, hive, customStep)
		// .withAmiVersion("3.3.0")
		// .withInstances(
		// new JobFlowInstancesConfig().withInstanceCount(3)
		// .withMasterInstanceType("m1.medium")
		// .withSlaveInstanceType("m1.medium")
		// .withKeepJobFlowAliveWhenNoSteps(false)
		// .withEc2KeyName("KeyPair")
		// .withHadoopVersion("2.4.0"))
		// .withName("augurmr").withLogUri("s3://augurframework/log/")
		// .withServiceRole("EMR_DefaultRole")
		// .withJobFlowRole("EMR_EC2_DefaultRole"));
		// // AddJobFlowStepsResult result = client.addJobFlowSteps(new
		// // AddJobFlowStepsRequest()
		// // .withJobFlowId()
		// // .withSteps(hive, customStep));
		//
		// System.out.println(result.getJobFlowId());
		// } else if (args[0].equals("p")) {
		// if (args.length > 3) {
		// System.out
		// .println("Usage: \"<exec> t\" or \"<exec> p <moviename> [new]\"");
		// return;
		// }
		// // String movieName = args[1];
		// // boolean rePredict = args.length == 3;
		// // Predict mode
		// /*
		// * Predict the movie box office collections
		// *
		// * 1. If movie is in DB, load the already calculated collection and
		// * display the result if rePrediction is not requested. ELse, go to
		// * step 2. 2. Load data for the movie from various sources and write
		// * to S3. 3. Run EMR on the data from S3. 4. Aggregate the data, and
		// * load it in Hive DB, along with the prediction info. Display the
		// * prediction.
		// */
		// } else {
		// System.out
		// .println("Usage: \"<exec> t\" or \"<exec> p <moviename> [new]\"");
		// }
	}

	private static void runDemo(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: <exec> movieName");
			return;
		}
		String movieName = new String(args[0]);

		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider().getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load credentials from the file", e);
		}

		AmazonS3Client s3 = new AmazonS3Client(credentials);

//		System.out.println("Loading RT JAR...");
//		File rtJarFile = new File("RottenTomatoes.jar");
//		s3.getObject(new GetObjectRequest("augurframework",
//				"bin/RottenTomatoes.jar"), rtJarFile);
//
//		System.out.println("Loading Twitter JAR...");
//		File twitJarFile = new File("Twitter.jar");
//		s3.getObject(new GetObjectRequest("augurframework", "bin/Twitter.jar"),
//				twitJarFile);
//		
//		System.out.println("Loading Youtube JAR...");
//		File ytJarFile = new File("Youtube.jar");
//		s3.getObject(new GetObjectRequest("augurframework", "bin/Youtube.jar"),
//				ytJarFile);
//
//		// Rotten Tomatoes
//		String rtCmd = "java -jar RottenTomatoes.jar -info augurframework mapreduceInput/Demo "
//				+ movieName.replaceAll("\\s", "_");
//		try {
//			boolean fault = false;
//			String line;
//			System.out.println(rtCmd);
//			Process p = Runtime.getRuntime().exec(rtCmd);
//			BufferedReader bri = new BufferedReader(new InputStreamReader(
//					p.getInputStream()));
//			BufferedReader bre = new BufferedReader(new InputStreamReader(
//					p.getErrorStream()));
//			while ((line = bri.readLine()) != null) {
//				System.out.println(line);
//				String error;
//				if ((error = bre.readLine()) != null) {
//					System.out.println(error);
//					fault = true;
//					break;
//				}
//			}
//			bre.close();
//			bri.close();
//			p.waitFor();
//			if (fault) {
//				return;
//			}
//		} catch (IOException e) {
//			System.out.println("Error in RT: " + e.getMessage());
//			return;
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			return;
//		}
//
//		// Twitter
//		String twitCmd = "java -jar Twitter.jar augurframework mapreduceInput/Demo "
//				+ movieName.replaceAll("\\s", "_");
//		try {
//			boolean fault = false;
//			String line;
//			System.out.println(twitCmd);
//			Process p = Runtime.getRuntime().exec(twitCmd);
//			BufferedReader bri = new BufferedReader(new InputStreamReader(
//					p.getInputStream()));
//			BufferedReader bre = new BufferedReader(new InputStreamReader(
//					p.getErrorStream()));
//			while ((line = bri.readLine()) != null) {
//				System.out.println(line);
//				String error;
//				if ((error = bre.readLine()) != null) {
//					System.out.println(error);
//					fault = true;
//					break;
//				}
//			}
//			bre.close();
//			bri.close();
//			p.waitFor();
//			if (fault) {
//				return;
//			}
//		} catch (IOException e) {
//			System.out.println("Error in Twitter: " + e.getMessage());
//			return;
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			return;
//		}
//
//		// YouTube here
//		String ytCmd = "java -jar Youtube.jar augurframework mapreduceInput/Demo "
//				+ movieName.replaceAll("\\s", "_");
//		try {
//			boolean fault = false;
//			String line;
//			System.out.println(ytCmd);
//			Process p = Runtime.getRuntime().exec(ytCmd);
//			BufferedReader bri = new BufferedReader(new InputStreamReader(
//					p.getInputStream()));
//			BufferedReader bre = new BufferedReader(new InputStreamReader(
//					p.getErrorStream()));
//			while ((line = bri.readLine()) != null) {
//				System.out.println(line);
//				String error;
//				if ((error = bre.readLine()) != null) {
//					System.out.println(error);
//					fault = true;
//					break;
//				}
//			}
//			bre.close();
//			bri.close();
//			p.waitFor();
//			if (fault) {
//				return;
//			}
//		} catch (IOException e) {
//			System.out.println("Error in YouTube: " + e.getMessage());
//			return;
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			return;
//		}

		// EMR
//		AmazonElasticMapReduce client = new AmazonElasticMapReduceClient(
//				credentials);
//
//		// predefined steps. See StepFactory for list of predefined steps
////		StepConfig hive = new StepConfig("Hive",
////				new StepFactory().newInstallHiveStep());
//
//		StepConfig debug = new StepConfig("Debug",
//				new StepFactory().newEnableDebuggingStep());
//
//		// A custom step
//		HadoopJarStepConfig hadoopConfig1 = new HadoopJarStepConfig().withJar(
//				"s3://augurframework/bin/AugurMapreduce.jar").withArgs(
//				"s3://augurframework/include/",
//				"s3://augurframework/mapreduceInput/Demo",
//				"s3://augurframework/mapreduceOutput/");
//
//		StepConfig customStep = new StepConfig("augurTrain", hadoopConfig1);
//
//		RunJobFlowResult result = client.runJobFlow(new RunJobFlowRequest()
//				.withSteps(debug, /*hive,*/ customStep)
//				.withAmiVersion("3.3.0")
//				.withInstances(
//						new JobFlowInstancesConfig().withInstanceCount(2)
//								.withMasterInstanceType("m1.medium")
//								.withSlaveInstanceType("m1.medium")
//								.withKeepJobFlowAliveWhenNoSteps(false)
//								.withEc2KeyName("KeyPair")
//								.withHadoopVersion("2.4.0"))
//				.withName("augurmr").withLogUri("s3://augurframework/log/")
//				.withServiceRole("EMR_DefaultRole")
//				.withJobFlowRole("EMR_EC2_DefaultRole"));
//
//		System.out.println(result.getJobFlowId());
//
//		GetObjectMetadataRequest isSuccess = new GetObjectMetadataRequest(
//				"augurframework", "mapreduceOutput/_SUCCESS");
//		GetObjectMetadataRequest isFail = new GetObjectMetadataRequest(
//				"augurframework", "mapreduceOutput/_FAIL");
//		while (true) {
//			try {
//				// s3.getObjectMetadata(isSuccess);
//				s3.getObjectMetadata(isSuccess);
//			} catch (AmazonServiceException e) {
//				continue;
//			}
//			break;
//		}
//
//		System.out.println("Mapreduce Done.");
//		
//		//Copy output to local folder
//		ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
//		.withBucketName("augurframework")
//		.withPrefix("mapreduceOutput/part");
//		ObjectListing objectListing;
//
//		System.out.println("Loading MapReduce Output Data...");
//		File mrFile = new File("db/mrOutput.txt");
//		do {
//			objectListing = s3.listObjects(listObjectsRequest);
//			for (S3ObjectSummary objectSummary : 
//				objectListing.getObjectSummaries()) {
//				if(objectSummary.getSize() > 0) {
//					s3.getObject(new GetObjectRequest("augurframework", objectSummary.getKey()),
//							mrFile);
//				}
//				System.out.println( " - " + objectSummary.getKey() + "  " +
//		                "(size = " + objectSummary.getSize() + 
//						")");
//			}
//			listObjectsRequest.setMarker(objectListing.getNextMarker());
//		} while (objectListing.isTruncated());
//		
		//Load Db
//		System.out.println("Loading DB JAR...");
//		File dbJarFile = new File("DB.jar");
//		s3.getObject(new GetObjectRequest("augurframework",
//				"bin/DB.jar"), dbJarFile);
//		// exec load "fulpathoffolder"
//		String dbCmd = "java -jar DB.jar load /Users/shastri/Documents/workspace/AugurFramework/db";
//		try {
//			boolean fault = false;
//			String line;
//			System.out.println(dbCmd);
//			Process p = Runtime.getRuntime().exec(dbCmd);
//			BufferedReader bri = new BufferedReader(new InputStreamReader(
//					p.getInputStream()));
//			BufferedReader bre = new BufferedReader(new InputStreamReader(
//					p.getErrorStream()));
//			while ((line = bri.readLine()) != null) {
//				System.out.println(line);
//				String error;
//				if ((error = bre.readLine()) != null) {
//					System.out.println(error);
//					fault = true;
//					break;
//				}
//			}
//			bre.close();
//			bri.close();
//			p.waitFor();
//			if (fault) {
//				return;
//			}
//		} catch (IOException e) {
//			System.out.println("Error in DB: " + e.getMessage());
//			return;
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			return;
//		}
		
//		Rotten Tomatoes Cast Info
//		String rtCastCmd = "java -jar RottenTomatoes.jar -cast augurframework cast "
//				+ movieName.replaceAll("\\s", "_");
//		try {
//			boolean fault = false;
//			String line;
//			System.out.println(rtCastCmd);
//			Process p = Runtime.getRuntime().exec(rtCastCmd);
//			BufferedReader bri = new BufferedReader(new InputStreamReader(
//					p.getInputStream()));
//			BufferedReader bre = new BufferedReader(new InputStreamReader(
//					p.getErrorStream()));
//			while ((line = bri.readLine()) != null) {
//				System.out.println(line);
//				String error;
//				if ((error = bre.readLine()) != null) {
//					System.out.println(error);
//					fault = true;
//					break;
//				}
//			}
//			bre.close();
//			bri.close();
//			p.waitFor();
//			if (fault) {
//				return;
//			}
//		} catch (IOException e) {
//			System.out.println("Error in RT: " + e.getMessage());
//			return;
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			return;
//		}
//		
		//Load Starcast
//		System.out.println("Loading Starcast JAR...");
//		File starcastFile = new File("Starcast.jar");
//		s3.getObject(new GetObjectRequest("augurframework",
//				"bin/Starcast.jar"), starcastFile);
//		
//		ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
//		.withBucketName("augurframework")
//		.withPrefix("cast/");
//		ObjectListing objectListing;
//		
//		System.out.println("Loading Starcast Data...");
//		File mrFile = new File("cast/starcast.txt");
//		do {
//			objectListing = s3.listObjects(listObjectsRequest);
//			for (S3ObjectSummary objectSummary : 
//				objectListing.getObjectSummaries()) {
//				if(objectSummary.getSize() > 0) {
//					s3.getObject(new GetObjectRequest("augurframework", objectSummary.getKey()),
//							mrFile);
//				}
//				System.out.println( " - " + objectSummary.getKey() + "  " +
//		                "(size = " + objectSummary.getSize() + 
//						")");
//			}
//			listObjectsRequest.setMarker(objectListing.getNextMarker());
//		} while (objectListing.isTruncated());
//		
//		ListObjectsRequest listObjectsRequestStarMap = new ListObjectsRequest()
//		.withBucketName("augurframework")
//		.withPrefix("starmap/");
//		
//		//Load starmap data
//		System.out.println("Loading Starmap Data...");
//		File starMapFile = new File("starmap");
//		do {
//			objectListing = s3.listObjects(listObjectsRequestStarMap);
//			for (S3ObjectSummary objectSummary : 
//				objectListing.getObjectSummaries()) {
//				if(objectSummary.getSize() > 0) {
//					s3.getObject(new GetObjectRequest("augurframework", objectSummary.getKey()),
//							starMapFile);
//				}
//				System.out.println( " - " + objectSummary.getKey() + "  " +
//		                "(size = " + objectSummary.getSize() + 
//						")");
//			}
//			listObjectsRequestStarMap.setMarker(objectListing.getNextMarker());
//		} while (objectListing.isTruncated());
//		
//		//Write starpower of movies to file
//		String starpowerCmd = "java -jar Starcast.jar /Users/shastri/Documents/workspace/AugurFramework/cast "
//				+ "/Users/shastri/Documents/workspace/AugurFramework/starmap";
//		try {
//			boolean fault = false;
//			String line;
//			System.out.println(starpowerCmd);
//			Process p = Runtime.getRuntime().exec(starpowerCmd);
//			BufferedReader bri = new BufferedReader(new InputStreamReader(
//					p.getInputStream()));
//			BufferedReader bre = new BufferedReader(new InputStreamReader(
//					p.getErrorStream()));
//			while ((line = bri.readLine()) != null) {
//				System.out.println(line);
//				String error;
//				if ((error = bre.readLine()) != null) {
//					System.out.println(error);
//					fault = true;
//					break;
//				}
//			}
//			bre.close();
//			bri.close();
//			p.waitFor();
//			if (fault) {
//				return;
//			}
//		} catch (IOException e) {
//			System.out.println("Error in Starpower: " + e.getMessage());
//			return;
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			return;
//		}
		
		//Load starpower in DB
//		String starpowerloadCmd = "java -jar DB.jar starpower /Users/shastri/Documents/workspace/AugurFramework/starpower.txt";
//		try {
//			boolean fault = false;
//			String line;
//			System.out.println(starpowerloadCmd);
//			Process p = Runtime.getRuntime().exec(starpowerloadCmd);
//			BufferedReader bri = new BufferedReader(new InputStreamReader(
//					p.getInputStream()));
//			BufferedReader bre = new BufferedReader(new InputStreamReader(
//					p.getErrorStream()));
//			while ((line = bri.readLine()) != null) {
//				System.out.println(line);
//				String error;
//				if ((error = bre.readLine()) != null) {
//					System.out.println(error);
//					fault = true;
//					break;
//				}
//			}
//			bre.close();
//			bri.close();
//			p.waitFor();
//			if (fault) {
//				return;
//			}
//		} catch (IOException e) {
//			System.out.println("Error in Starpower Uploader: " + e.getMessage());
//			return;
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			return;
//		}
		
		//Load classification JAR
		System.out.println("Loading Classifier JAR...");
		File starcastFile = new File("Classification.jar");
		s3.getObject(new GetObjectRequest("augurframework",
				"bin/Classification.jar"), starcastFile);
		
		String wekaCmd = "java -jar Classification.jar";
		try {
			boolean fault = false;
			String line;
			System.out.println(wekaCmd);
			Process p = Runtime.getRuntime().exec(wekaCmd);
			BufferedReader bri = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			BufferedReader bre = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));
			while ((line = bri.readLine()) != null) {
				System.out.println(line);
				String error;
				if ((error = bre.readLine()) != null) {
					System.out.println(error);
					fault = true;
					break;
				}
			}
			bre.close();
			bri.close();
			p.waitFor();
			if (fault) {
				return;
			}
		} catch (IOException e) {
			System.out.println("Error in Starpower Uploader: " + e.getMessage());
			return;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
	}
}

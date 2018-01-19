package com.tkt.samples.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class SampleProcessor {
	static Logger logger = LoggerFactory.getLogger(SampleProcessor.class);
	
	public static void main(String[] args) throws IOException {
		if (args.length != 4) {
			logger.error("usage: SampleProcessor <bucket> <folder> <inputs3file> <outputs3file>");
			System.out.println("usage: SampleProcessor <bucket> <folder> <inputs3file> <outputs3file>");
			return;
		}
		
		// create a client connection based on credentials
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).build();

		process(args[0], args[1] + "/" + args[2], args[1] + "/" + args[3], s3client);
	}

	public static void process(String bucketName, String fileS3PathTag, String outputFileS3PathTag, AmazonS3 s3client) throws IOException {

		List<S3ObjectSummary> fileList = s3client.listObjects(bucketName, fileS3PathTag).getObjectSummaries();

		File tmp = File.createTempFile("s3test", "");
		FileOutputStream is = new FileOutputStream(tmp);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(is));

		
		// Create a request to download content for computing the sum for these files
		for (S3ObjectSummary file : fileList) {
			GetObjectRequest getRequest = new GetObjectRequest(bucketName, file.getKey());

			// Download content
			S3Object obj = s3client.getObject(getRequest);
			InputStream inputStream = obj.getObjectContent();
			InputStreamReader inputStreamReader = null;
			BufferedReader reader = null;
			try {
				String line = null;
				SampleData sd = null;
				inputStreamReader = new InputStreamReader(inputStream);
				reader = new BufferedReader(inputStreamReader);

				//skip the header
				line = reader.readLine();
				bw.write(SampleData.header());
				bw.newLine();
				
				// process for downloaded content
				while ((line = reader.readLine()) != null) {
					try {
						sd = new SampleData(line);
						bw.write(sd.dataLine());
						bw.newLine();
						
						System.out.println(" - " + sd.dataLine());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			} finally {
				if (reader != null)
					reader.close();
				if (inputStreamReader != null)
					inputStreamReader.close();
				if (inputStream != null)
					inputStream.close();
				if(bw != null)
					bw.close();
				if(is != null)
					is.close();
			}
		}
		
		S3FileLoader.uploadFile(bucketName, outputFileS3PathTag, tmp.getPath(), s3client);
		
		if(tmp != null)
			tmp.delete();
	}
}
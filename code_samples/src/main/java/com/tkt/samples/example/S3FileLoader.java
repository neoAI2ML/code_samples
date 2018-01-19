package com.tkt.samples.example;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3FileLoader {
	
	private static final String SUFFIX = "/";
	static Logger logger = LoggerFactory.getLogger(S3FileLoader.class);
	
	public static void main(String[] args) {
		if (args.length != 4) {
			logger.error("usage: S3FileLoader <bucket> <folder> <s3file> <localFilePath>");
			System.out.println("usage: S3FileLoader <bucket> <folder> <s3file> <localFilePath>");
			return;
		}
		
		// create a client connection based on credentials
		AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider())
                .build();
		
		// create bucket - name must be unique for all S3 users
		String bucketName = args[0];
		createBucket(bucketName, s3client);
		
		// create folder into bucket
		String folderName = args[1];
		createFolder(bucketName, folderName, s3client);
		
		// upload file to folder and set it to public
		String fileName = folderName + SUFFIX + args[2];
		uploadFile(bucketName, fileName, args[3], s3client);
		
		return;
	}
	
	/**
	 * This method upload a file into the s3
	 */
	public static void uploadFile(String bucketName, String fileS3PathName, String fileLocalPathName, AmazonS3 client) {
		client.putObject(new PutObjectRequest(bucketName, fileS3PathName, 
				new File(fileLocalPathName)).withCannedAcl(CannedAccessControlList.PublicRead));
	}
	
	/**
	 * This method first checks if the bucket exists, and than creates the
	 * bucket if the bucket does not exist
	 */
	public static void createBucket(String bucketName, AmazonS3 client) {
		for (Bucket bucket : client.listBuckets()) {
			if(bucket.getName().compareTo(bucketName) == 0) {
				logger.info("The bucket: "+ bucketName + " exists");
				return;
			};
		}
		client.createBucket(bucketName);
	}

	/**
	 * This method first deletes all the files in given folder and than the
	 * folder itself
	 */
	public static void deleteBucket(String bucketName, AmazonS3 client) {
		client.deleteBucket(bucketName);
	}
	
	/**
	 * This method first checks if the folder exists, and than creates the
	 * folder if the folder does not exist
	 */
	public static void createFolder(String bucketName, String folderName, AmazonS3 client) {
		List<S3ObjectSummary> fileList = 
				client.listObjects(bucketName, folderName).getObjectSummaries();
		if(!fileList.isEmpty()) {
			logger.info("The folder: "+ folderName + " exists");
			return;
		}
		
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);

		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
				folderName + SUFFIX, emptyContent, metadata);

		// send request to S3 to create folder
		client.putObject(putObjectRequest);
	}

	/**
	 * This method first deletes all the files in given folder and than the
	 * folder itself
	 */
	public static void deleteFolder(String bucketName, String folderName, AmazonS3 client) {
		List<S3ObjectSummary> fileList = 
				client.listObjects(bucketName, folderName).getObjectSummaries();
		for (S3ObjectSummary file : fileList) {
			client.deleteObject(bucketName, file.getKey());
		}
		client.deleteObject(bucketName, folderName);
	}
}
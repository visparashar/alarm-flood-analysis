package com.siemens.daac.poc.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.siemens.daac.poc.constant.ProjectConstants;

@Service
public class StorageService {

	@Value("${user-upload-test-file-location}")
	String userTestFileUploadPath;
	
	@Value("${prefilter-input-folder}")
	String predictionOutputFolder;


	private  Path rootLocation = Paths.get("data/input_data");

	public boolean store(MultipartFile file,String fileName,boolean forTraining){
		try {
			if(!forTraining) {
				rootLocation = Paths.get(userTestFileUploadPath);

			}else {
				rootLocation = Paths.get("data/input_data");
			}
			if(Files.copy(file.getInputStream(), this.rootLocation.resolve(fileName),StandardCopyOption.REPLACE_EXISTING)>0)
				return true;
		} catch (Exception e) {
			throw new RuntimeException("FAIL!" +e.getMessage());

		}
		return false;
	}

	public Resource loadFile(String filename) {
		try {
			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if(resource.exists() || resource.isReadable()) {
				return resource;
			}else{
				throw new RuntimeException("FAIL!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("FAIL!");
		}
	}

	public void deleteAll(String path) {
		if(path!=null && !path.isEmpty())
		{
			FileSystemUtils.deleteRecursively(Paths.get(path).toFile());
			
		}
		else
			FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}


	public void archievAllFiles() throws IOException {
		copyFolder(new File("data"), new File("dumb1"));

	}
	public void deleteAllFiles() {
		File f = new File("data");
		try {
			FileUtils.deleteDirectory(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public void deleteOldPreRequesFiles() throws IOException {
		File f = new File(ProjectConstants.FILE_FLAG_FOR_MSW_DONE);
		if(f.exists())
			f.delete();
		File f1 = new File(ProjectConstants.FILE_FLAG_FOR_PREDICTION_DONE);
		if(f1.exists())
			f1.delete();
		File f2 = new File(ProjectConstants.FILE_FLAG_FOR_MSW_DONE_ONUI);
		if(f2.exists())
			f2.delete();
		File forClusterReco = new File(ProjectConstants.FILE_FLAF_FOR_CLUSTERRECO_DONE);
		if(forClusterReco.exists())
			forClusterReco.delete();
		File forClusterReco1 = new File(ProjectConstants.FILE_FLAF_FOR_CLUSTERRECO_DONE1);
		if(forClusterReco1.exists())
			forClusterReco1.delete();
		File forClusterReco2 = new File(ProjectConstants.FILE_FLAF_FOR_CLUSTERRECO_DONE2);
		if(forClusterReco2.exists())
			forClusterReco2.delete();
		File testRcaFlag = new File(ProjectConstants.FILE_FLAG_FOR_TEST_RCA_DONE);
		if(testRcaFlag.exists())
			testRcaFlag.delete();
		File trueFloodDir = new File(predictionOutputFolder+"/"+ProjectConstants.TRUE_FLOOD_PATH);
		if(trueFloodDir.exists()) {
			FileUtils.cleanDirectory(trueFloodDir);
		}
		File falseFloodDir = new File(predictionOutputFolder+"/"+ProjectConstants.FALSE_FLOOD_PATH);
		if(falseFloodDir.exists())
			FileUtils.cleanDirectory(falseFloodDir);
	}

	private void copyFolder(File sourceFolder, File destinationFolder) throws IOException
	{
		FileUtils.copyDirectoryToDirectory(sourceFolder, destinationFolder);
		/*//Check if sourceFolder is a directory or file
		//If sourceFolder is file; then copy the file directly to new location
		if (sourceFolder.isDirectory())
		{
			//Verify if destinationFolder is already present; If not then create it
			if (!destinationFolder.exists())
			{
				destinationFolder.mkdir();
				System.out.println("Directory created :: " + destinationFolder);
			}

			//Get all files from source directory
			String files[] = sourceFolder.list();

			//Iterate over all files and copy them to destinationFolder one by one
			for (String file : files)
			{
				File srcFile = new File(sourceFolder, file);
				File destFile = new File(destinationFolder, file);

				//Recursive function call
				copyFolder(srcFile, destFile);
			}
		}
		else
		{
			//Copy the file content from one place to another
			Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("File copied :: " + destinationFolder);
		}*/
	}

	public void init() {
		try {
			Files.createDirectories(rootLocation);
			Files.createDirectories(Paths.get(userTestFileUploadPath));
			//            File f= new File(userTestFileUploadPath);
			//            if(!f.exists())
			//            	f.mkdirs();
			//            .createDirectory(rootLocation);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize storage!");
		}
	}
}

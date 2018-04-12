package com.siemens.daac.poc.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.siemens.daac.poc.constant.ProjectConstants;

@Service
public class StorageService {
	
	@Value("{notSureFilePath}")
	String testDataPath;
	
	private  Path rootLocation = Paths.get("data/input_data");
 
	public void store(MultipartFile file,String fileName,boolean forTraining){
		try {
			if(!forTraining) {
				rootLocation = Paths.get(testDataPath);
			}
           Files.copy(file.getInputStream(), this.rootLocation.resolve(fileName),StandardCopyOption.REPLACE_EXISTING);
          
        } catch (Exception e) {
        	throw new RuntimeException("FAIL!" +e.getMessage());
        }
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
    
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
    
    
    public void deleteOldPreRequesFiles() {
    	File f = new File(ProjectConstants.FILE_FLAG_FOR_MSW_DONE);
    	if(f.exists())
    		f.delete();
    	File f1 = new File(ProjectConstants.FILE_FLAG_FOR_PREDICTION_DONE);
    	if(f1.exists())
    		f1.delete();
    	File f2 = new File(ProjectConstants.FILE_FLAG_FOR_MSW_DONE_ONUI);
    	if(f2.exists())
    		f2.delete();
    }
 
    public void init() {
        try {
            Files.createDirectories(rootLocation);
//            .createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }
}

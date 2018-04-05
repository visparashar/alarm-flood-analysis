package com.siemens.daac.poc.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {
	
	private final Path rootLocation = Paths.get("data/input_data");
 
	public void store(MultipartFile file){
		try {
           Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()),StandardCopyOption.REPLACE_EXISTING);
          
        } catch (Exception e) {
        	throw new RuntimeException("FAIL!");
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
 
    public void init() {
        try {
            Files.createDirectories(rootLocation);
//            .createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }
}

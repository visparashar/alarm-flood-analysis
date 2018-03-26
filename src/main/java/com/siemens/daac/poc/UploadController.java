package com.siemens.daac.poc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.siemens.daac.poc.constant.ProjectConstants;
import com.siemens.daac.poc.model.RInput;
import com.siemens.daac.poc.service.CSVFileProcessorService;
import com.siemens.daac.poc.service.RManager;

@Controller
public class UploadController {
	
	@Autowired
	CSVFileProcessorService csvFileProcessorService;
	
	@Value("${r-prediction-output-folder-location}")
	String rOutputFolderLocation;
	
	@Autowired
	RManager rManager;

	//Save the uploaded file to this folder

	@PostMapping("/upload") // //new annotation since 4.3
	public String singleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		
		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}

		try {
			String defaultWorkspace=System.getProperty("user.dir");
			defaultWorkspace +="/data/input_data/";
			defaultWorkspace = defaultWorkspace.replace(File.separator, "/");
			String UPLOADED_FOLDER = defaultWorkspace;
			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);
			
// Calling CSVReading Service TO read and extract the data ;
			if(csvFileProcessorService.read(UPLOADED_FOLDER + file.getOriginalFilename()))
			{
				String trainingSetInitialFilePath =defaultWorkspace+"/merged_data";
				csvFileProcessorService.merge(trainingSetInitialFilePath);
				callRProcess(trainingSetInitialFilePath);
			}
			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded '" + file.getOriginalFilename() + "'");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/uploadStatus";
	}
	
	
	private void callRProcess(String trainingSetPath) {
		RInput rInput = new RInput();
		trainingSetPath +="/"+ProjectConstants.TRAINING_SET_DIR_NAME;
		rInput.setStatus(ProjectConstants.FALSE);
		rInput.setInputFilePath(trainingSetPath);
		rInput.setOutputFilePath(rOutputFolderLocation);
		rInput.setAlgorithmType(ProjectConstants.R_PREDICTION_ALGO);
		rManager.sendToQueueForRExecution(rInput);
	}


	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "AlarmHomePage";
	}

}

package com.siemens.daac.poc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
	
	@Value("${r-prediction-folder-location}")
	String rPredictionFolderLocation;
	
	@Value("${r-prediction-output-folder-location}")
	String rOutputFolderLocation;
	
	@Value("${mergedFilePath}")
	String mergedFilePath;
	
	@Value("${archived-file-path}")
	String archievedFileLocation;
	
	
	@Autowired
	RManager rManager;

	String defaultWorkspace=System.getProperty("user.dir");
	//Save the uploaded file to this folder
	@PostMapping("/upload") // //new annotation since 4.3
	public String singleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		
		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}

		try {
			String UploadedFolderLocation =defaultWorkspace+"/data/input_data/";
			UploadedFolderLocation = UploadedFolderLocation.replace(File.separator, "/");
			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			File directory = new File(UploadedFolderLocation);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			Path path = Paths.get(UploadedFolderLocation + file.getOriginalFilename());
			Files.write(path, bytes);
			
// Calling CSVReading Service TO read and extract the data ;
			if(csvFileProcessorService.read(UploadedFolderLocation +"/"+ file.getOriginalFilename()))
			{
				
//				CSVMergeUtil.moveFileToDestination(UploadedFolderLocation, archievedFileLocation);
				String trainingSetInitialFilePath =mergedFilePath;
				csvFileProcessorService.merge(trainingSetInitialFilePath);
//				setup r prequesities for R to be run
				setUpPreRequesiteisForR();
				callRProcess(defaultWorkspace+"/"+trainingSetInitialFilePath);
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
		
		trainingSetPath +=ProjectConstants.TRAINING_SET_DIR_NAME;
		trainingSetPath=trainingSetPath.replaceAll(File.separator, "/");
		rInput.setInputFilePath(trainingSetPath);
		rInput.setOutputFilePath(rPredictionFolderLocation);
		rInput.setAlgorithmType(ProjectConstants.R_PREDICTION_ALGO);
		rManager.sendToQueueForRExecution(rInput);
	}
	
	private void setUpPreRequesiteisForR() {
		
		File file = new File(rOutputFolderLocation);
		if(!file.exists())
			file.mkdir();
	}


/*	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "AlarmHomePage";
	}*/


    @GetMapping("/uploadStatus")
    public String uploadStatus(ModelMap m) {
    	Integer trueflood=50;
    	Integer falseflood=10;
    	m.addAttribute("trueflood",trueflood);
    	m.addAttribute("falseflood",falseflood);
        return "AlarmHomePage";
    }

}

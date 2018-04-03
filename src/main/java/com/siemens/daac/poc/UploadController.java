package com.siemens.daac.poc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
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


	private static org.apache.logging.log4j.Logger logger = LogManager.getLogger();

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

	@Value("${user-upload-file-location}")
	String userInputFileLocation;


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
			String UploadedFolderLocation =defaultWorkspace+userInputFileLocation+File.separator;
			UploadedFolderLocation=UploadedFolderLocation.replaceAll("\\\\", "/");
			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			File directory = new File(UploadedFolderLocation);
			if (!directory.exists()) {
				if(!directory.mkdirs()){
					logger.error("[singleFileUpload] There is a problem occurred while creating the upload directory ");
					redirectAttributes.addFlashAttribute("message",
							"There is some problem occured please check logs ");
					return "redirect:/uploadStatus";
				}
			}
			Path path = Paths.get(UploadedFolderLocation+file.getOriginalFilename());
			Files.write(path, bytes);

			// Calling CSVReading Service TO read and extract the data ;
			if(csvFileProcessorService.read(UploadedFolderLocation +file.getOriginalFilename()))
			{
				//				CSVMergeUtil.moveFileToDestination(UploadedFolderLocation, archievedFileLocation);
				String trainingSetInitialFilePath =mergedFilePath;
				csvFileProcessorService.merge(trainingSetInitialFilePath);
				//				setup r prequesities for R to be run
				setUpPreRequesiteisForR();
				callRProcess(defaultWorkspace+"/"+trainingSetInitialFilePath);
			}else{
				logger.error("Please Upload Valid Zip");
				redirectAttributes.addFlashAttribute("message",
						"Please Upload Valid Zip ");
				return "redirect:/uploadStatus";
			}
			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded '" + file.getOriginalFilename() + "'");
		} catch (IOException e) {
			logger.error("Some Problem Occurred "+e.getMessage());
			redirectAttributes.addFlashAttribute("message",
					"There is some problem occured please check logs ");
			return "redirect:/uploadStatus";
		}
		return "redirect:/uploadStatus";
	}


	private void callRProcess(String trainingSetPath) {
		RInput rInput = new RInput();

		trainingSetPath +=ProjectConstants.TRAINING_SET_DIR_NAME;
		trainingSetPath=trainingSetPath.replaceAll("\\\\", "/");
		rInput.setInputFilePath(trainingSetPath);
		rInput.setOutputFilePath(rPredictionFolderLocation);
		rInput.setAlgorithmType(ProjectConstants.R_PREDICTION_ALGO);
		rManager.sendToQueueForRExecution(rInput);
	}

	private void setUpPreRequesiteisForR() {

		File file = new File(rOutputFolderLocation);
		if(!file.exists())
			file.mkdirs();
	}


	@GetMapping("/uploadStatus")
	public String uploadStatus(ModelMap m) {
		Integer trueflood=50;
		Integer falseflood=10;
		m.addAttribute("trueflood",trueflood);
		m.addAttribute("falseflood",falseflood);
		return "AlarmHomePage";
	}

}

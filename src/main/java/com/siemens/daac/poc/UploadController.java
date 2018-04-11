package com.siemens.daac.poc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.siemens.daac.poc.constant.ProjectConstants;
import com.siemens.daac.poc.model.RInput;
import com.siemens.daac.poc.service.CSVFileProcessorService;
import com.siemens.daac.poc.service.RManager;
import com.siemens.daac.poc.service.StorageService;
import com.siemens.daac.poc.utility.CSVReaderUtil;
import com.siemens.daac.poc.utility.CommonUtils;
import com.siemens.daac.poc.utility.RUtil;

@Controller
public class UploadController {
	File file = new File("Check_For_R_Call_Get_Completed.txt");
	private boolean status = false;

	private static org.apache.logging.log4j.Logger logger = LogManager.getLogger();

	@Autowired
	CSVFileProcessorService csvFileProcessorService;

	@Value("${r-prediction-folder-location}")
	String rPredictionFolderLocation;

	@Value("${r-prediction-output-folder-location}")
	String rOutputFolderLocation;

	@Value("${mergedFilePath}")
	String mergedFilePath;

	@Value("${user-upload-file-location}")
	String userInputFileLocation;

	@Autowired
	RManager rManager;

	@Autowired
	StorageService storageService;

	String defaultWorkspace = System.getProperty("user.dir");
	private final Path rootLocation = Paths.get("data/input_data");

	// Save the uploaded file to this folder
	@PostMapping("/upload")
	public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}
		try {
			String UploadedFolderLocation = defaultWorkspace + userInputFileLocation + File.separator;
			String fileName = null;
			String pattern = Pattern.quote(System.getProperty("file.separator"));
			String[] str = file.getOriginalFilename().split(pattern);
			if (str.length > 0)
				fileName = str[str.length - 1];
			else
				fileName = str[0];
			storageService.store(file, fileName);

			// Calling CSVReading Service TO read and extract the data ;
			if (csvFileProcessorService.read(UploadedFolderLocation + fileName ,true)) {
				// CSVMergeUtil.moveFileToDestination(UploadedFolderLocation,
				// archievedFileLocation);
				String trainingSetInitialFilePath = mergedFilePath;
				csvFileProcessorService.merge(trainingSetInitialFilePath);
				// setup r prequesities for R to be run
				setUpPreRequesiteisForR();
				callRProcess(defaultWorkspace + "/" + trainingSetInitialFilePath);
				status =true;
			} else {
				status =false;
				logger.error("Please Upload Valid Zip");
				redirectAttributes.addFlashAttribute("message", "Please Upload Valid Zip ");
				redirectAttributes.addFlashAttribute("status", "false");
				return "redirect:/uploadStatus";
			}
			redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + fileName + "'");
			redirectAttributes.addFlashAttribute("status", "true");
		} catch (IOException e) {
			status=false;
			logger.error("Some Problem Occurred " + e.getMessage());
			redirectAttributes.addFlashAttribute("message", "There is some problem occured "+e.getMessage());
			redirectAttributes.addFlashAttribute("status", "false");
			return "redirect:/uploadStatus";
		}

		return "redirect:/uploadStatus";
	}

	private void callRProcess(String trainingSetPath) {
		RInput rInput = new RInput();

		trainingSetPath += "/" + ProjectConstants.TRAINING_SET_DIR_NAME;
		trainingSetPath = trainingSetPath.replaceAll("\\\\", "/");
		rInput.setInputFilePath(trainingSetPath);
		rInput.setOutputFilePath(rOutputFolderLocation + "/");
		rInput.setAlgorithmType(ProjectConstants.R_PREDICTION_ALGO);
		rInput.setRunForTraining(true);
		String workSpace = CommonUtils.getRWorkspacePath();
		workSpace = workSpace.replaceAll("\\\\", "/");
		rInput.setrWorkSpacePath(workSpace);
		rManager.sendToQueueForRExecution(rInput);
		rInput.setRunForTraining(false);
		rManager.sendToQueueForRExecution(rInput);
	}

	private void setUpPreRequesiteisForR() {

		File file1 = new File(rOutputFolderLocation + "/" + ProjectConstants.TRUE_FLOOD_PATH);
		if (!file1.exists())
			file1.mkdirs();

		File file2 = new File(rOutputFolderLocation + "/" + ProjectConstants.FALSE_FLOOD_PATH);
		if (!file2.exists())
			file2.mkdirs();

	}

	@GetMapping("/uploadStatus")
	public String uploadStatus(ModelMap m) {
		// Integer trueflood=50;
		// Integer falseflood=10;
		if (status) {
			while (!file.exists()) {
				// logger.info("waiting for r call get completed in uploadStatus method");
			}
		} else {
			return "AlarmHomePage";
		}
		file.delete();
		status = false;
		m.addAttribute("trueflood", CSVReaderUtil.trueCount);
		m.addAttribute("falseflood", CSVReaderUtil.falseCount);
		return "AlarmHomePage";

	}

	@GetMapping("/training")
	public String trainModel(ModelMap m) {
		System.out.println("/training called");
		try {
			callSequenceExecutor(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "AlarmHomePage";

	}

	private void callSequenceExecutor(boolean isTrainingRun) {
		// call prefilter
		if (RUtil.doPrefilterPrerequistes()) {
			// prepare rInput
			RInput rinput = null;
			try {
				rinput = RUtil.prepareRInputForAlgo(ProjectConstants.CONST_PREFILTER_ALGO, isTrainingRun);
				if (rinput != null) {
					if (rManager.sendToQueueForRExecution(rinput)) {
						// call for mswCluster
						rinput = null;
						if (RUtil.doMSWClusterPrerequistes()) {
							rinput = RUtil.prepareRInputForAlgo(ProjectConstants.CONST_MSW_CLUSTER_ALSO, isTrainingRun);
							rManager.sendToQueueForRExecution(rinput);
						}
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@GetMapping("/getCsv")
	public @ResponseBody String getCSVFile() {
		int cnt = 0;
		String csvFile = "result.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String retStrt="";
		String separator="#~#";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				if (cnt != 0) {
					// use comma as separator
					String[] country = line.split(cvsSplitBy);
					retStrt=retStrt+country[0]+separator+country[1]+separator+country[2]+cvsSplitBy;

				}
				cnt++;

			}
			cnt=0;

		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException in method getCSVFile :"+e);
		} catch (IOException e) {
			logger.error("IOException in method getCSVFile :"+e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error("error while closing bufferReader in method getCSVFile" +e);
				}
			}
		}
		if(retStrt.contains(cvsSplitBy))
			retStrt=retStrt.substring(0, retStrt.length()-1);

		return retStrt;

	}


	@PostMapping
	public String uploadTrainData(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}
		try {
			String UploadedFolderLocation = defaultWorkspace + userInputFileLocation + File.separator;
			String fileName = null;
			String pattern = Pattern.quote(System.getProperty("file.separator"));
			String[] str = file.getOriginalFilename().split(pattern);
			if (str.length > 0)
				fileName = str[str.length - 1];
			else
				fileName = str[0];
			storageService.store(file, fileName);

			// Calling CSVReading Service TO read and extract the data ;
			if (csvFileProcessorService.read(UploadedFolderLocation + fileName ,false)) {

			}

		}catch(IOException e) {
			//		TODO: vishal
		}
		return "AlarmHomePage";

	}
}

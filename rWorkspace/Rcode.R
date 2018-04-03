
callRFunction <- function(
		input_file_path ,output_file_path,source_of_other_lib,algorithm_to_run,
		is_model_need_to_run_or_training_set){
	
	
	setwd(input_file_path)
	source(paste0(source_of_other_lib,'/','filename_constants.R'))
	source(paste0(source_of_other_lib,'/','logger.R'))
	input_path <- try(read.csv(input_file_name), silent = TRUE)
	
#	need to write function to check whether the required library are installed or not
	
	if (inherits(input_path, "try-error"))
	{
		message_string = paste("Error 1: Unable to read file")
		logEvent(message_string, "Error")
		print (message_string)
		return (FALSE)
	}
	if(algorithm_to_run == CONST_PREDICTION_ALOG && is_model_need_to_run_or_training_set = TRUE)
	{
		source(paste0(source_of_other_lib,'/',CONST_PATH_LOC_PREDICTION,'/',CONST_NAIVE_BAYES))
		naive_baye_response = CustomNaiveBayesFunc(input_file_path,source_of_other_lib,output_file_path)
		
	}else{
		source(paste0(source_of_other_lib,'/',CONST_PATH_LOC_PREDICTION,'/',CONST_NAIVE_BAYES))
		naive_baye_response = CustomNaiveBayesFunc(input_file_path,source_of_other_lib,output_file_path)
	}
	if(algorithm_to_run == CONST_PREFILTER_ALGO && is_model_need_to_run_or_training_set = TRUE)
	{
		source(paste0(source_of_other_lib,'/',CONST_PATH_LOC_PREFILTER,'/',CONST_PREFILTER_FILENAME))
		naive_baye_response = PrefilterFunc(input_file_path,source_of_other_lib,output_file_path)
		
	}else{
		source(paste0(source_of_other_lib,'/',CONST_PATH_LOC_PREFILTER,'/',CONST_PREFILTER_FILENAME))
		naive_baye_response = PrefilterFunc(input_file_path,source_of_other_lib,output_file_path)
	}
	
	if(algorithm_to_run == CONST_MSW_CLUSTER_ALSO && is_model_need_to_run_or_training_set = TRUE)
	{
		source(paste0(source_of_other_lib,'/',CONST_PATH_LOC_MSW_CLUSTER,'/',CONST_MSW_CLUSTER_FILENAME))
		naive_baye_response = CalculateMSWMatrix(input_file_path,source_of_other_lib,output_file_path)
		
	}else{
		source(paste0(source_of_other_lib,'/',CONST_PATH_LOC_MSW_CLUSTER,'/',CONST_MSW_CLUSTER_FILENAME))
		naive_baye_response = CalculateMSWMatrix(input_file_path,source_of_other_lib,output_file_path)
	}
	
	
	
	
}
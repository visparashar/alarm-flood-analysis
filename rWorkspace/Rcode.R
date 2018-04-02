
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
	if(algorithm_to_run == CONST_PREDICTION_ALOG)
	{
		source(paste0(source_of_other_lib,'/',CONST_PATH_LOC_PREDICTION,'/',CONST_NAIVE_BAYES))
		
	}
	
	
	
	
	
}
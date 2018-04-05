
callRFunction <- function(
		input_file_path ,output_file_path,source_of_other_lib,algorithm_to_run,
  		is_model_need_to_run_or_training_set,rworkspace_path){
	getwd()
  setwd(rworkspace_path)
  source('filename_constants.R')
  source('logger.R')	
	# setwd(input_file_path)

	input_path <- try(read.csv(input_file_name), silent = TRUE)
	
#	need to write function to check whether the required library are installed or not
	
	# if (inherits(input_path, "try-error"))
	# {
	# 	message_string = paste("Error 1: Unable to read file")
	# 	logEvent(message_string, "Error")
	# 	print (message_string)
	# 	return (FALSE)
	# }
	if(algorithm_to_run == CONST_PREDICTION_ALOG && is_model_need_to_run_or_training_set == 'true')
	{
		source(paste0(source_of_other_lib,'/',CONST_NAIVE_BAYES))
		naive_baye_response = CustomNaiveBayesFunc(input_file_path,source_of_other_lib,output_file_path,'true')
		return(naive_baye_response)
	}else if(algorithm_to_run == CONST_PREDICTION_ALOG && is_model_need_to_run_or_training_set == 'false'){
	
	  #  neeed to right the model to be called
	  	source(paste0(source_of_other_lib,'/',CONST_NAIVE_BAYES))
		naive_baye_response = CustomNaiveBayesFunc(input_file_path,source_of_other_lib,output_file_path,'false')
		return(naive_baye_response)
	}
	if(algorithm_to_run == CONST_PREFILTER_ALGO && is_model_need_to_run_or_training_set == 'true')
	{
		source(paste0(source_of_other_lib,,'/',CONST_PREFILTER_FILENAME))
	  prefilter_response = PrefilterFunc(input_file_path,source_of_other_lib,output_file_path)
	  return(prefilter_response)
		
	}else if (algorithm_to_run == CONST_PREFILTER_ALGO && is_model_need_to_run_or_training_set == 'false'){
		source(paste0(source_of_other_lib,'/',CONST_PREFILTER_FILENAME))
		prefilter_response = PrefilterFunc(input_file_path,source_of_other_lib,output_file_path)
		return(prefilter_response)
	}
	
	if(algorithm_to_run == CONST_MSW_CLUSTER_ALSO && is_model_need_to_run_or_training_set == 'true')
	{
		source(paste0(source_of_other_lib,'/',CONST_MSW_CLUSTER_FILENAME))
		clasturing_response = CalculateMSWMatrix(input_file_path,source_of_other_lib,output_file_path)
		return(clasturing_response)
	}else if(algorithm_to_run == CONST_MSW_CLUSTER_ALSO && is_model_need_to_run_or_training_set == 'false'){
		source(paste0(source_of_other_lib,'/',CONST_MSW_CLUSTER_FILENAME))
	  clasturing_response = CalculateMSWMatrix(input_file_path,source_of_other_lib,output_file_path)
	  return(clasturing_response)
	}
	
	
	
	
}
# callRFunction(
#   'C:/Workspace_alarmflood/alarm-food-analysis/data/input_data/prediction_data/merged_data/training_data',
#   'C:/Workspace_alarmflood/alarm-food-analysis/data/prediction_output_data/',
#   'C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/prediction',
#   'prediction',
#   FALSE,
#   'C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace'
#  
#   
# )
# callRFunction('C:/Workspace_alarmflood/alarm-food-analysis/data/input_data/prediction_data/merged_data/training_data',
#               'C:/Workspace_alarmflood/alarm-food-analysis/data/prediction_output_data/',
#               'C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/prediction',
#               'prediction',
#               'true',
#               'C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace'
# )



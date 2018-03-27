# Naive Bayes

#Read file and get training and test set 
read_file_func <- function(inputfiledirname ,datatype){
 if(datatype == "training"){
    input_file_name =paste0(inputfiledirname,"/" ,CONST_PREDICT_FILENAME)
 }else{
    input_file_name =inputfiledirname
  }
 
  print(input_file_name)
  data_set <- try(read.csv(input_file_name), silent = TRUE)
  if (inherits(data_set, "try-error"))
  {
    message_string = paste("Error 1: Unable to read file")
    logEvent(message_string, "Error")
    print (message_string)
    return (FALSE)
  }
  trim_values=''
   if(datatype == "training")
  {
     trim_values = c(CONST_ALARMTAG_INDEX ,CONST_ALARMID_INDEX,CONST_STATUS_INDEX)
   }else{
     trim_values = c(CONST_ALARMTAG_INDEX ,CONST_ALARMID_INDEX)
  }
  data_set = data_set[trim_values]
  return(data_set)
}


# #trim the table using giving start and end index 
# trim_table <- function(dataset , trimfactor)
# {
#   trim_table =trim_table[trimfactor]
#   return(trim_table)
# }






CustomNaiveBayesFunc<-function(input_file_path ,source_of_other_files_path,output_file_path){
  #input_file_path ='C:/Workspace_alarmflood/alarm-food-analysis/data/input_data/merged_data/training_data'
  #source_of_other_files_path= 'C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/prediction'
  setwd(input_file_path)
  source(paste0(source_of_other_files_path,'/',"filename_constants.R"))
  source(paste0(source_of_other_files_path,'/',"logger.R"))
  library(e1071)
# function to read the csv of training set
# input_file_path ="C:/R_Workspace/FinalFiles"
training_data<-read_file_func(input_file_path ,CONST_TRAINING_SET)

#presetup factoring
training_data$Flood.Status = factor(training_data$Flood.Status, levels = c(0, 1))
# moving the working directory one step up
getwd()
setwd('..')
getwd()
#need to get all the files from test input folder/
test_data_file_path = paste0(getwd(),"/",CONST_TEST_SET_INPUT_DIRECTORY)
print(paste("input training data ",test_data_file_path))


filenames <- list.files(path = test_data_file_path , pattern = '*.csv')
print(filenames)
for (file in filenames) {
  new_path =paste0(test_data_file_path,"/",file)
 
  test_set<-read_file_func(new_path ,CONST_TEST_SET)
  
  # applying naive bayes algo
  classifier = naiveBayes(x = training_data[-3],
                          y = training_data$Flood.Status)
  #count for calculation of the no. of 1 in the prediction
  count =0
  # Predicting the Test set results
  y_pred = predict(classifier, newdata = test_set)
  
  for (v in y_pred) {
    if(v==1){
      count=count+1
    }
  }


  if(count/nrow(test_set)>=.5){
    test_set =cbind(test_set ,Status=1)
  }else{
    test_set =cbind(test_set ,Status=0)
  }
  #Writing the result to csv
  # outputfilepath =paste0(output_file_path,'/','prediction_data')
  outputfilepath=paste0(output_file_path,file,'.csv')
  print(outputfilepath)
  write.csv(test_set,file = outputfilepath,row.names = FALSE,quote = FALSE)
  
}

  
}
#CustomNaiveBayesFunc('C:/R_Workspace/FinalFiles')
#CustomNaiveBayesFunc('C:/Workspace_alarmflood/alarm-food-analysis/data/input_data/merged_data/training_data','C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/prediction')


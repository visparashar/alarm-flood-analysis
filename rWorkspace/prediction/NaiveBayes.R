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
    trim_values = ''
      # c(CONST_ALARMTAG_INDEX ,CONST_ALARMID_INDEX)
    return(data_set[-CONST_STATUS_INDEX])
  }
  data_set = data_set[trim_values]
  return(data_set)
}


getPredictionModel <- function(input_file_path)
{
  training_data<-read_file_func(input_file_path ,CONST_TRAINING_SET)
  #presetup factoring
  training_data$Flood.Status = factor(training_data$Flood.Status, levels = c(0, 1))
  classifier = naiveBayes(x = training_data[-3],
                          y = training_data$Flood.Status)
  return(classifier)
}





CustomNaiveBayesFunc<-function(input_file_path ,source_of_other_files_path,output_file_path,is_executed_for_training_set){
  # input_file_path ='C:/Workspace_alarmflood/alarm-food-analysis/data/input_data/prediction_data/merged_data/training_data'
  # source_of_other_files_path= 'C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/prediction'
  # output_file_path='C:/Workspace_alarmflood/alarm-food-analysis/data/output_data'
  # is_executed_for_training_set = FALSE
  setwd(input_file_path)
  library(e1071)
  
  if(is_executed_for_training_set == 'true'){
    # get prediction model
    classifier =getPredictionModel(input_file_path)
    save(classifier ,file = "my_model.rda")
    return(TRUE)
    
  }else if(is_executed_for_training_set == "false"){
    print("in else loop")
    load("my_model.rda") 
    getwd()
    setwd('..')
    getwd()
    #need to get all the files from test input folder/
    test_data_file_path = paste0(getwd(),"/",CONST_TEST_SET_INPUT_DIRECTORY)
    print(paste("input training data ",test_data_file_path))
    
    
    filenames <- list.files(path = test_data_file_path , pattern = '*.csv')
    print(filenames)
    true_count=0
    false_count=0
    # applying naive bayes algo
    
    getwd()
    for (file in filenames) {
      new_path =paste0(test_data_file_path,"/",file)
      
      test_set<-read_file_func(new_path ,CONST_TEST_SET)
      
      
      
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
        test_set =cbind(test_set ,Flood_Status=1)
        updated_output_file=paste0(output_file_path,'true_flood/',file,'.csv')
        true_count=true_count+1
      }else{
        test_set =cbind(test_set ,Flood_Status=0)
        updated_output_file=paste0(output_file_path,'false_flood/',file,'.csv')
        false_count=false_count+1
      }
      
      print(updated_output_file)
      write.csv(test_set,file = updated_output_file,row.names = FALSE,quote = FALSE)
      
    }
    return(cbind(true=true_count,false=false_count))
  }
  
}


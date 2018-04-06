library(dplyr)
library(tm)
library(NLP)
library(textreuse)
library(cluster)
CalculateMSWMatrix <-  function(input_file_path ,
                                msw_text_result_path,
                                similarity_index_path,
                                source_of_other_files_path   ) {
  # source(paste0(source_of_other_files_path,'/',"logger.R"))
  setwd(input_file_path)
  filenames <- list.files(input_file_path, pattern = '*.csv')
  len = length(filenames)
  
  for (i in 1:len) {
    f1 = filenames[i]
    data_set <- try(read.csv(f1, na.strings = c("", "NA")),silent = TRUE)
    if (inherits(data_set1, "try-error"))
    {
      message_string = paste("Error 1: Unable to read file")
      logEvent(message_string, "Error")
      print (message_string)
      return (FALSE)
    }
    data_set = data_set %>% na.omit()
    alarm_seq = paste0(data_set$Alarm.Tag, data_set$Alarm.Id)
	#writing data to csv for knn calculation
    test_set =cbind(Alarm_Seq=alarm_seq ,FloodNum=i)
    write.csv(test_set,file = paste0(knn_path, "/", "Flood_Type", i,".csv"),row.names = FALSE,quote = FALSE)
    # Writing csv data to text files
    write.table(
      alarm_seq,
      file = paste0(msw_text_result_path, "/", "Flood", i, ".txt"),
      quote = FALSE,
      row.names = FALSE,
      col.names = FALSE
    )
  }
  
  #similarity matrix calculation
  dir <- Corpus(DirSource(msw_text_result_path))
  corpus <-
    TextReuseCorpus(dir = msw_text_result_path, minhash_func = minhash)
  #pairwise comparison method
  scores <- pairwise_compare(corpus,jaccard_similarity)
  scores_pair=pairwise_candidates(scores)
  scores_frame = as.data.frame(scores_pair)
  #distance matrix for hierarchical clustering
  scores_matrix = as.data.frame.matrix(scores)
  d <- dist(scores_matrix, method = "euclidean")
  #saving similarity matrix in csv
  setwd(similarity_index_path)
  write.csv(
    scores_frame,
    file = paste0("result", ".csv"),
    row.names = FALSE,
    quote = FALSE
  )
}


CalculateMSWMatrix(
 "C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/msw_cluster/msw_csvdataset",
 "C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/msw_cluster/msw_textdataset",
 "C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/msw_cluster/similarityindex_matrix",
 ""
)
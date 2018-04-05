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
    # if (inherits(data_set1, "try-error"))
    # {
    #   message_string = paste("Error 1: Unable to read file")
    #   logEvent(message_string, "Error")
    #   print (message_string)
    #   return (FALSE)
    # }
    data_set = data_set %>% na.omit()
    alarm_seq = paste0(data_set$Alarm.Tag, data_set$Alarm.Id)
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
  #need to check for best minhash value
  minhash <- minhash_generator(10, seed = 235)
  ats <-
    TextReuseCorpus(dir = msw_text_result_path, minhash_func = minhash)
  buckets <- lsh(ats, bands = 10, progress = FALSE)
  candidates <- lsh_candidates(buckets)
  scores <-
    lsh_compare(candidates, ats, jaccard_similarity, progress = FALSE)
  #creating the matrix
  scores_frame = as.data.frame(scores)
  scores_matrix = as.data.frame.matrix(scores_frame)
  #need to check,which method will be perfect here
  d <- dist(scores_matrix, method = "euclidean")
  #saving similarity matrix in csv
  setwd(similarity_index_path)
  write.csv(
    scores,
    file = paste0("result", ".csv"),
    row.names = FALSE,
    quote = FALSE
  )
  #Agglomerative Hierarchical Clustering
  #need to check,which method will be perfect here
  hc1 <- agnes(scores_frame, method = "complete")
  # hc_frame=as.data.frame(hc1)
  # hc_frame
  plot(hc1)
}


CalculateMSWMatrix(
 "C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/msw_cluster/msw_csvdataset",
 "C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/msw_cluster/msw_textdataset",
 "C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/msw_cluster/similarityindex_matrix",
 ""
)
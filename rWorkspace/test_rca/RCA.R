library(dplyr)
library(tools)
library(textreuse)
library(tm)

#function to compare test data with cluster data
compare_to_cluster <-
  function(textform_test_data_path,
           recommendation_file_path)
  {
    setwd(textform_test_data_path)
    dir <- Corpus(DirSource(textform_test_data_path))
    corpus <-
      TextReuseCorpus(dir = textform_test_data_path)
    bind = NULL
    maxm = -1
    similar_file = ""
    for (j in 1:3) {
      similarity = jaccard_similarity(corpus[[paste0("Cluster", j)]], corpus[["Test"]])
      similarity_frame = as.data.frame(similarity)
      if (maxm < similarity)
      {
        maxm = similarity_frame
        similar_file = paste0("Cluster", j)
        res = c(similar_file, maxm)
        res_df = as.data.frame(res)
        colnames(res_df)[CONST_CLUSTER_FIRST_COL] <- CONST_CLUSTER
        colnames(res_df)[CONST_CLUSTER_SECOND_COL] <-
          CONST_SIMILARITY
      }
      
    }
    #writing recommendation result to a file
    setwd(recommendation_file_path)
    write.table(
      res_df,
      file = paste0(recommendation_file_path, "Recommendation", ".csv"),
      row.names = FALSE,
      quote = FALSE,
      sep = ","
    )
  }

#main function for root cause analysis
RootCauseAnalysis <-
  function(test_data_path,
           textform_test_data_path,
           recommendation_file_path) {
    #read test file
    setwd(test_data_path)
    filenames <- list.files(test_data_path, pattern = '*.csv')
    data_set <-
      try(read.csv(filenames[1], na.strings = c("", "NA")), silent = TRUE)
    if (inherits(data_set, "try-error"))
    {
      message_string = paste("Error 1: Unable to read file")
      logEvent(message_string, "Error")
      print (message_string)
      return (FALSE)
    }
    data_set = data_set %>% na.omit()
    alarm_seq = paste0(data_set[, CONST_ALARMTAG_INDEX], data_set[, CONST_ALARMID_INDEX])
    # Writing csv data to text files
    write.table(
      alarm_seq,
      file = paste0(textform_test_data_path, "/", "Test", ".txt"),
      quote = FALSE,
      row.names = FALSE,
      col.names = FALSE
    )
    
    #compare  test file with cluster files
    compare_to_custer(textform_test_data_path, recommendation_file_path)
    
    
  }

RootCauseAnalysis(
  "C:/Users/Khushboo/Documents/test_rca/testdata",
  "C:/Users/Khushboo/Documents/test_rca/testdata_text",
  "C:/Users/Khushboo/Documents/test_rca/recommendation/"
)
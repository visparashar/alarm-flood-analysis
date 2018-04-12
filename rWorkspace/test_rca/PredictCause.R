#setwd("C:/Users/Khushboo/Documents/test_rca")
#setwd("C:/Users/Khushboo/Documents/freq_ptrn_mining/merged_result")
library(dplyr)
library(tools)
library(textreuse)
library(tm)

setwd("C:/Users/Khushboo/Documents/test_rca/testdata")
filenames <-list.files("C:/Users/Khushboo/Documents/test_rca/testdata", pattern = '*.csv')



  data_set <- try(read.csv(filenames[1], na.strings = c("", "NA")),silent = TRUE)
  if (inherits(data_set, "try-error"))
  {
    message_string = paste("Error 1: Unable to read file")
    logEvent(message_string, "Error")
    print (message_string)
    return (FALSE)
  }
  data_set = data_set %>% na.omit()
  alarm_seq = paste0(data_set[,5], data_set[,6])
  # Writing csv data to text files
  write.table(
    alarm_seq,
    file = paste0("C:/Users/Khushboo/Documents/test_rca/testdata_text", "/", "Test", ".txt"),
    quote = FALSE,
    row.names = FALSE,
    col.names = FALSE
  )
  
  #compare with test data with clusters
 
  setwd("C:/Users/Khushboo/Documents/test_rca/testdata_text")
  dir <- Corpus(DirSource("C:/Users/Khushboo/Documents/test_rca/testdata_text"))
  corpus <-
    TextReuseCorpus(dir = "C:/Users/Khushboo/Documents/test_rca/testdata_text")
   bind=NULL
   maxm=-1
   similar_file=""
  for(j in 1:3){
     similarity=jaccard_similarity(corpus[[paste0("Merged",j)]],corpus[["Test"]])
     similarity_frame=as.data.frame(similarity)
    if(maxm<similarity)
    {
      maxm=similarity_frame
      similar_file =paste0("Cluster",j)
      res=c(similar_file,maxm)
      res_df=as.data.frame(res)
      colnames(res_df)[1] <- "Cluster"
      colnames(res_df)[2]<-"Similarity"
    }
     
  }
   setwd("C:/Users/Khushboo/Documents/test_rca/recommendation")
   write.table(res_df,file=paste0("C:/Users/Khushboo/Documents/test_rca/recommendation/","Recommendation",".csv"),row.names=FALSE,quote=FALSE,sep=",")
   
  
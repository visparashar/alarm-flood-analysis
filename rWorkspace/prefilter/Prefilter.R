

PrefilterFunc <-
  function(input_file_path ,
           prefilter_result_path,
           msw_result_path) {
    # source(paste0(source_of_other_files_path,'/',"logger.R"))
	
    #assuming a thresold value
	#changed the thresold value using property file - vishal
    thresold = 1.0
	#defining a dataframe for prefilter similarity
    df_total = data.frame()
    library(magrittr)
    setwd(input_file_path)
    
    #List csv files of the directory
    filenames <- list.files(input_file_path, pattern = '*.csv')
    len = length(filenames)
    
    #process each file of the list
    for (i in 1:len) {
      j = i + 1
      while (j <= len) {
        if (i != j) {
          setwd(input_file_path)
          f1 = filenames[i]
          f2 = filenames[j]
          
          #calculating prefilter similarity index for pair of files at once
          data_set1 <- try(read.csv(f1, na.strings = c("", "NA")), silent = TRUE)
          if (inherits(data_set1, "try-error"))
          {
            message_string = paste("Error 1: Unable to read file")
            logEvent(message_string, "Error")
            print (message_string)
            return (FALSE)
          }
          data_set1 = data_set1%>%na.omit()
          nrow_f1 = nrow(data_set1)
          data_set2 <-try(read.csv(f2, na.strings = c("", "NA")), silent = TRUE)
          if (inherits(data_set2, "try-error"))
          {
            message_string = paste("Error 1: Unable to read file")
            logEvent(message_string, "Error")
            print (message_string)
            return (FALSE)
          }
          data_set2 = data_set2%>%na.omit()
          nrow_f2 = nrow(data_set2)
          max_nrow = max(nrow_f1, nrow_f2)
          #Check for id column name
          alarm_seq1 = paste0(data_set1[,CONST_ALARMTAG_INDEX], data_set1[,CONST_ALARMID_INDEX])
          alarm_seq2 = paste0(data_set2[,CONST_ALARMTAG_INDEX], data_set2[,CONST_ALARMID_INDEX])
          common_seq = intersect(alarm_seq1, alarm_seq2)
          len1 = length(common_seq)
          si = len1 / max_nrow
          if (si >= thresold)
          {
            #writing prefilter similarity to a file
            setwd(prefilter_result_path)
            a = f1
            b = f2
            score = si
            sm = data.frame(a, b, score)
            df_total <- rbind(df_total, sm)
            write.csv(
              df_total,
              file = paste0("prefilter_result", ".csv"),
              row.names = FALSE,
              quote = FALSE
            )
          }
          else
          {
            setwd(msw_result_path)
            write.csv(
              data_set1,
              file = paste0(f1),
              row.names = FALSE,
              quote = FALSE
            )
            write.csv(
              data_set2,
              file = paste0(f2),
              row.names = FALSE,
              quote = FALSE
            )
            
          }
        }
        j = j + 1
      }
    }
    
  }

#PrefilterFunc(
#  "C:/Users/Khushboo/Documents/prefilter/alarm_dataset",
#  "C:/Users/Khushboo/Documents/msw_cluster/similarity_index_matrix",
#  "C:/Users/Khushboo/Documents/msw_cluster/msw_csvdataset",
#  "C:/Users/Khushboo/Documents/prediction"
#)

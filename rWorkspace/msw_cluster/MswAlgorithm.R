library(dplyr)
library(tm)
library(NLP)
library(textreuse)
library(cluster)
CalculateMSWMatrix <-  function(input_file_path ,
		msw_text_result_path,
		similarity_index_path
) {
	# source(paste0(source_of_other_files_path,'/',"logger.R"))
	setwd(input_file_path)
	filenames <- list.files(input_file_path, pattern = '*.csv')
	len = length(filenames)
	
	for (i in 1:len) {
		f1 = filenames[i]
		data_set <- try(read.csv(f1, na.strings = c("", "NA")),silent = TRUE)
		# if (inherits(data_set, "try-error"))
		# {
		#   message_string = paste("Error 1: Unable to read file")
		#   logEvent(message_string, "Error")
		#   print (message_string)
		#   return (FALSE)
		# }
		data_set = data_set %>% na.omit()
		alarm_seq = paste0(data_set[,CONST_ALARMTAG_INDEX], data_set[,CONST_ALARMID_INDEX])
		
		# Writing csv data to text files
		write.table(
				alarm_seq,
				file = paste0(msw_text_result_path, "/", f1, ".txt"),
				quote = FALSE,
				row.names = FALSE,
				col.names = FALSE
		)
	}
	
	#similarity matrix calculation
	dir <- Corpus(DirSource(msw_text_result_path))
	corpus <-
			TextReuseCorpus(dir = msw_text_result_path)
	#pairwise comparison method
	scores <- pairwise_compare(corpus,jaccard_similarity)
	scores_pair=pairwise_candidates(scores)
	scores_frame = as.data.frame(scores_pair)		
	#merge similarity index along with prefilter values 
	setwd(similarity_index_path)
	write.csv(
        scores_frame,
        file = paste0("msw_result", ".csv"),
        row.names = FALSE,
        quote = FALSE
      )
	
	minvalue = apply(scores_frame,2,min)
	minvalue =minvalue[3];    
	maxvalue = apply(scores_frame,2,max)
	maxvalue = maxvalue[3]
	diffvalue = (as.numeric(maxvalue)-as.numeric(minvalue))/3
	value1 =as.numeric(minvalue)+diffvalue
	value2 =value1+diffvalue
	clusterpath=paste0(similarity_index_path,"/",'clusters/')
	out = split(scores_frame,cut(scores_frame$score,c(minvalue,value1,value2, maxvalue),include.lowest = TRUE))
	# print(out)
	count=0
	lapply(names(out), function(x) {
	count =count+1
	write.table(out[[x]],file=paste0(clusterpath,"cluster",'_',count,'~~',x,'~~.csv'),quote = FALSE,row.names = FALSE,sep=",")});
	return(out)
}




#CalculateMSWMatrix(
# "C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/msw_cluster/msw_csvdataset",
# "C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/msw_cluster/msw_textdataset",
# "C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/msw_cluster/similarityindex_matrix",
# ""
#)
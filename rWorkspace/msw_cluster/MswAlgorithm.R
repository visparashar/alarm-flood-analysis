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
		alarm_seq = paste0(data_set[,5], data_set[,6])
		
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
			TextReuseCorpus(dir = msw_text_result_path)
	#pairwise comparison method
	scores <- pairwise_compare(corpus,jaccard_similarity)
	scores_pair=pairwise_candidates(scores)
	scores_frame = as.data.frame(scores_pair)
	#distance matrix for hierarchical clustering
	scores_matrix = as.data.frame.matrix(scores)
	d <- dist(scores_matrix, method = "euclidean")
	#merge similarity index along with prefilter values 
	setwd(similarity_index_path)
	res_file <- paste0(similarity_index_path,"/","result", ".csv") 
	if(file.exists(res_file)){
		write.table(
				scores_frame,
				file = paste0("result", ".csv"),
				append = TRUE,
				sep=',',
				quote = FALSE,
				col.names = FALSE,
				row.names=FALSE
		)
	}
	else
	{
		write.csv(
				scores_frame,
				file = paste0("result", ".csv"),
				row.names = FALSE,
				quote = FALSE
		)
	}
	
	minvalue = apply(scores_frame,2,min)
	minvalue =minvalue[3];    
	maxvalue = apply(scores_frame,2,max)
	maxvalue = maxvalue[3]
	diffvalue = (as.numeric(maxvalue)-as.numeric(minvalue))/3
	value1 =as.numeric(minvalue)+diffvalue
	value2 =value1+diffvalue
	out = split(scores_frame,cut(scores_frame$score,c(minvalue,value1,value2, maxvalue),include.lowest = TRUE))
	# print(out)
	lapply(names(out), function(x) {write.table(out[[x]],file=paste0("cluster",x,".csv"),quote = FALSE,row.names = FALSE,sep=",")});
	return(out)
}




#CalculateMSWMatrix(
# "C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/msw_cluster/msw_csvdataset",
# "C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/msw_cluster/msw_textdataset",
# "C:/Workspace_alarmflood/alarm-food-analysis/rWorkspace/msw_cluster/similarityindex_matrix",
# ""
#)
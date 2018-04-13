library(tools)
library(dplyr)
library(readtext)
library(tm)

#function to compare files from text dataset
compare_files <- function(input_file_path, file_compare_comb_path)
{  
  setwd(input_file_path)
  filenames <- list.files(input_file_path, pattern = '*.txt')
  len = length(filenames)
  
  for (i in 1:len) {
    j = i + 1
    while (j <= len) {
      if (i != j) {
        setwd(input_file_path)
        f1 = filenames[i]
        f2 = filenames[j]
        f1_rm = file_path_sans_ext(filenames[i])
        f2_rm = file_path_sans_ext(filenames[j])
        textset1 = readLines(f1)
        textset2 = readLines(f2)
        common_seq1 = textset1[textset1 %in% intersect(textset1, textset2)]
        common_seq2 = textset2[textset2 %in% intersect(textset1, textset2)]
        merge = c(common_seq1, common_seq2) 
		getwd()
        setwd(file_compare_comb_path)
        write.table(
          merge,
          file = paste0(file_compare_comb_path, "/", f1_rm, "_", f2_rm, ".txt"),
          quote = FALSE,
          row.names = FALSE,
          col.names = FALSE
        )
      }
      j = j + 1
    }
  }
}

#function to merge alarm sequences cluster wise
merge_cluster_sequences <-
  function(cluster_result_path,
           file_compare_comb_path,
           merged_cluster_path,
           test_data_text_path)
  {    
    setwd(cluster_result_path)
    clus_filenames <-
      list.files(cluster_result_path, pattern = '*.csv')
    clus_len = length(clus_filenames)
    
    for (i in 1:clus_len) {
      #read file from cluster_result folder
      setwd(cluster_result_path)
      clus_f1 = clus_filenames[i]
      clus_dataset <- try(read.csv(clus_f1, na.strings = c("", "NA")))
      clus_dataset = clus_dataset %>% na.omit()
      #find unique flood names of each cluster
      uni = unique(clus_dataset[, CONST_CLUSTER_FIRST_COL])
      uni1 = unique(clus_dataset[, CONST_CLUSTER_SECOND_COL])
      union = as.data.frame(union(uni, uni1))
      nrow = nrow(union)
      merg = NULL
      #evaluating files cluster wise
      for (k in 1:nrow)
      {
        l = k + 1
        while (l <= nrow) {
          if (k != l) {
            setwd(cluster_result_path)
            cf1 = paste0(union[k, ])
            cf2 = paste0(union[l, ])
            bind_cfname = paste0(cf1, "_", cf2)
			bind_cfname2 = paste0(cf2, "_", cf1)
            setwd(file_compare_comb_path)
            #getting filenames from pattern mining folder
            pattern_filenames <-
              list.files(file_compare_comb_path, pattern = '*.txt')
            p_len = length(pattern_filenames)
            p_fname = paste0(bind_cfname, ".txt")
			if(file.exists(p_fname)){
            patern_read = readLines(p_fname)
            patern_read_frame = as.data.frame(patern_read)
            merg = rbind(merg, patern_read_frame)
			}
			else{
			p_fname2 = paste0(bind_cfname2, ".txt")	
             patern_read = readLines(p_fname2)	
             patern_read_frame = as.data.frame(patern_read)	
             merg = rbind(merg, patern_read_frame)}
			}
          }
          
          l = l + 1
        }
        setwd(merged_cluster_path)
        write.table(
          merg,
          file = paste0("Cluster", i, ".txt"),          
          row.names = FALSE,
          col.names = FALSE,
          quote = FALSE
          
        )
        setwd(test_data_text_path)
        write.table(
          merg,
          file = paste0("Cluster", i, ".txt"),          
          row.names = FALSE,
          col.names = FALSE,
          quote = FALSE
          
        )
        
      }
      
    }
  }

#find most frequent alarm sequence
find_most_frquent_sequence <-
  function(merged_cluster_path,
           frequency_file_path)
  {    
    #find most frequent pattern in each cluster
    setwd(merged_cluster_path)
    merged_filenames <-
      list.files(merged_cluster_path, pattern = '*.txt')
    merged_len = length(merged_filenames)
    for (t in 1:merged_len) {
      setwd(merged_cluster_path)
      freq_text_set <- readLines(merged_filenames[t])
      freq_textset_df <- data_frame(Text = freq_text_set)
      freq_text_wordcounts <-
        freq_textset_df %>% count(freq_textset_df$Text, sort = TRUE)
      freq_text_wordcounts_df = as.data.frame(freq_text_wordcounts)
      #change column names
      colnames(freq_text_wordcounts_df)[colnames(freq_text_wordcounts_df) == 'freq_textset_df$Text'] <-
        CONST_ALARM_SEQUENCE
      colnames(freq_text_wordcounts_df)[colnames(freq_text_wordcounts_df) == 'n'] <-
        CONST_FREQUENCY
      
      top_frq_count = head(freq_text_wordcounts_df, CONST_MOST_FREQUENT_SEQUENCE)
      setwd(frequency_file_path)
      write.csv(
        top_frq_count,
        file = paste0(
          frequency_file_path,
          "/",
          "Cluster",
          t,
          "_Frequency",
          ".csv"
        ),
        row.names = FALSE,
        quote = FALSE
      )
    }
	return(top_frq_count)
  }
CalculateFrequentPattern <-
  function(input_file_path,
           file_compare_comb_path,
           cluster_result_path,
           merged_cluster_path,
           test_data_text_path,
           frequency_file_path) {    
    # function to compare files from msw text dataset
    compare_files(input_file_path, file_compare_comb_path)
    
    #cluster wise alarm sequence merging
    merge_cluster_sequences(
      cluster_result_path,
      file_compare_comb_path,
      merged_cluster_path,
      test_data_text_path
    )
    
    #find most frequent pattern in each cluster
   return( find_most_frquent_sequence(merged_cluster_path, frequency_file_path))
  }

CalculateFrequentPattern(
  "C:/Users/Khushboo/Documents/msw_cluster/msw_textdataset",
  "C:/Users/Khushboo/Documents/freq_ptrn_mining/pattern_mining",
  "C:/Users/Khushboo/Documents/msw_cluster/cluster_result",
  "C:/Users/Khushboo/Documents/freq_ptrn_mining/merged_result",
  "C:/Users/Khushboo/Documents/test_rca/testdata_text",
  "C:/Users/Khushboo/Documents/freq_ptrn_mining/frequency_count"
)

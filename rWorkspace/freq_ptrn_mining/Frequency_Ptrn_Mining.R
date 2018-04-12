library(tools)
library(dplyr)
library(readtext)
library(tm)
setwd("C:/Users/Khushboo/Documents/msw_cluster/msw_textdataset")
filenames <- list.files("C:/Users/Khushboo/Documents/msw_cluster/msw_textdataset", pattern = '*.txt')
len = length(filenames)

for (i in 1:len) {
  j = i + 1
  while (j <= len) {
    if (i != j) {
      setwd("C:/Users/Khushboo/Documents/msw_cluster/msw_textdataset")
      f1 =filenames[i]
      f2 = filenames[j]
      f1_rm =file_path_sans_ext(filenames[i])
      f2_rm = file_path_sans_ext(filenames[j])
      textset1=readLines(f1)
      textset2=readLines(f2)
      #textset1[textset1 %in% intersect(textset1, textset2)]
      common_seq1 = textset1[textset1 %in% intersect(textset1, textset2)]
      common_seq2 = textset2[textset2 %in% intersect(textset1, textset2)]
      merge= c(common_seq1,common_seq2)
      setwd("C:/Users/Khushboo/Documents/freq_ptrn_mining/pattern_mining")
      write.table(
        merge,
        file = paste0("C:/Users/Khushboo/Documents/freq_ptrn_mining/pattern_mining", "/", f1_rm,"_",f2_rm,".txt"),
        quote = FALSE,
        row.names = FALSE,
        col.names = FALSE
      )
    }
    j=j+1
    }
}


#now moving to cluster folder for further calculation
setwd("C:/Users/Khushboo/Documents/msw_cluster/cluster_result")
clus_filenames <- list.files("C:/Users/Khushboo/Documents/msw_cluster/cluster_result", pattern = '*.csv')
clus_len = length(clus_filenames)

for(i in 1:clus_len){
  #read file from cluster_result folder
  setwd("C:/Users/Khushboo/Documents/msw_cluster/cluster_result")
  clus_f1 = clus_filenames[i]
  
  clus_dataset <- try(read.csv(clus_f1, na.strings = c("", "NA")))
  clus_dataset = clus_dataset %>% na.omit()
  #find unique flood names of each cluster
  uni = unique(clus_dataset[,1])
  uni1=unique(clus_dataset[,2])
  union= as.data.frame(union(uni,uni1))
  nrow =nrow(union)
  merg=NULL
  #evaluating files cluster wise
  for(k in 1:nrow)
  {
    l=k+1
    while (l <= nrow) {
      if (k != l) {
      setwd("C:/Users/Khushboo/Documents/msw_cluster/cluster_result")
      cf1=paste0(union[k,])
      cf2=paste0(union[l,])
      bind_cfname=paste0(cf1,"_",cf2)
      setwd("C:/Users/Khushboo/Documents/freq_ptrn_mining/pattern_mining")
      #getting filenames of pattern mining folder
      pattern_filenames <- list.files("C:/Users/Khushboo/Documents/freq_ptrn_mining/pattern_mining", pattern = '*.txt')
      p_len = length(pattern_filenames)
      p_fname=paste0(bind_cfname,".txt")
          patern_read =readLines(p_fname)
          patern_read_frame=as.data.frame(patern_read)
          merg = rbind(merg,patern_read_frame)
      }
      
      l=l+1
    }
    setwd("C:/Users/Khushboo/Documents/freq_ptrn_mining/merged_result")
    write.table(merg,file=paste0("Merged",i,".txt"),row.names=FALSE,col.names = FALSE,quote=FALSE)
    setwd("C:/Users/Khushboo/Documents/test_rca/testdata_text")
    write.table(merg,file=paste0("Merged",i,".txt"),row.names=FALSE,col.names = FALSE,quote=FALSE)
    
  }
 
}

#find most frequent pattern in each cluster
setwd("C:/Users/Khushboo/Documents/freq_ptrn_mining/merged_result")
merged_filenames <- list.files("C:/Users/Khushboo/Documents/freq_ptrn_mining/merged_result", pattern = '*.txt')
merged_len = length(merged_filenames)
for(t in 1:merged_len){
setwd("C:/Users/Khushboo/Documents/freq_ptrn_mining/merged_result")
freq_text_set <- readLines(merged_filenames[t])
freq_textset_df <- data_frame(Text = freq_text_set)
freq_text_wordcounts <- freq_textset_df %>% count(freq_textset_df$Text, sort = TRUE)
freq_text_wordcounts_df=as.data.frame(freq_text_wordcounts)
#change column names
colnames(freq_text_wordcounts_df)[colnames(freq_text_wordcounts_df) == 'freq_textset_df$Text'] <- 'AlarmSequence'
colnames(freq_text_wordcounts_df)[colnames(freq_text_wordcounts_df) == 'n'] <- 'Frequency'

top_frq_count=head(freq_text_wordcounts_df,10)
setwd("C:/Users/Khushboo/Documents/freq_ptrn_mining/frequency_count")
write.csv(top_frq_count,file = paste0("C:/Users/Khushboo/Documents/freq_ptrn_mining/frequency_count", "/", "Cluster",t,"_Frequency",".csv"),row.names = FALSE,quote = FALSE)
}

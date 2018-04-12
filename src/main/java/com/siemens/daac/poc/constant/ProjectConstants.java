package com.siemens.daac.poc.constant;

public class ProjectConstants {


	public static final String R_QUEUE ="r-request-queue";
	public static final String R_RESPONSE_QUEUE="r-response-queue";

	public static final String R_PREDICTION_ALGO="prediction";
	public static final String CONST_PREFILTER_ALGO="prefilter";
	public static final String CONST_MSW_CLUSTER_ALSO="cluster";
	
	
	public static final String R_WORKSPACE_FOLDER_NAME="rWorkspace";



	//	common constants
	public static final String TRUE ="true";
	public static final String FALSE="false";
	public static final String TRAINING_SET_DIR_NAME="training_data";
	public static final String TRAINING_SET_CSV_NAME="training_set.csv";
	public static final String TRUE_FLOOD_PATH="true_flood";
	public static final String FALSE_FLOOD_PATH="false_flood";
	
	public static final String KEY_INPUT_FILE ="input_file_path";
	public static final String KEY_OUTPUT_FILE ="output_file_path";
	public static final String KEY_OTHER_LIB ="source_of_other_lib";
	public static final String KEY_ALGO_TYPE ="algorithm_to_run";
	public static final String KEY_ISRUN_FOR_TRAINING ="is_model_need_to_run_or_training_set";
	public static final String KEY_RWORKSPACE ="rworkspace_path";
	
	
//	global variable
	public static boolean isTrueFloodStatusUpdated =false;
	
	public static final String FILE_FLAG_FOR_PREDICTION_DONE="Check_For_R_Call_Get_Completed.txt";
	public static final String FILE_FLAG_FOR_MSW_DONE="check_for_mswcluster_complete.txt";
	public static final String FILE_FLAG_FOR_MSW_DONE_ONUI="check_for_mswcluster_created.txt";
	









}

LOGGER = TRUE
LOG_FILE_NAME = "./temp.log"

logEvent <- function (event_string, event_type)
{
  valid_event_types = c("DEBUG", "INFO", "WARN", "ERROR", "FATAL", "OTHER")
  
  # return if LOGGER variable is not set
  if (!exists("LOGGER"))
    return
  if (is.na(LOGGER))
    return
  
  # return if LOG_FILE_NAME variable is not set
  if (!exists("LOG_FILE_NAME"))
    return
  if (is.na(LOG_FILE_NAME))
    return
  
  # if event_type is not valid, then replaced it with "OTHER"
  if (!(event_type %in% valid_event_types))
    event_type = "OTHER"
  
  # prepare and store the log
  if (LOGGER)
  {
    event_type_text = paste("[", event_type, "]", sep="")
    time_text = paste("[", Sys.time(), "]", sep="")
    log_base_text = paste("[", "R operator", "]", sep="")
    log_text = paste("=", event_type_text, ":", time_text, ":", log_base_text, ":", event_string, sep="")
    write(log_text, LOG_FILE_NAME, append=TRUE)
  }
}
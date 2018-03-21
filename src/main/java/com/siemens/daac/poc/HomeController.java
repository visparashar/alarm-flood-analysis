package com.siemens.daac.poc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	
@RequestMapping("/flood1")  
public String displayHomePageForAlarm() {
	return "AlarmHomePage1";
}

@RequestMapping("/train")  
public String displayTrainPage() {
	return "train";
}
}

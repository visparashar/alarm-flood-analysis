package com.siemens.daac.poc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
@RequestMapping("/flood")  
public String displayHomePageForAlarm() {
	return "AlarmHomePage";
}

@RequestMapping("/train")  
public String displayTrainPage(ModelMap m) {
	List<Flood> floodList=new ArrayList<Flood>();
	for(int i=1;i<=10;i++)
	{
	floodList.add(new Flood("Flood"+i,true));
	}
	for(int i=11;i<=22;i++)
	{
	floodList.add(new Flood("Flood"+i,false));
	}
	m.addAttribute(floodList);
	Integer trueflood=50;
	Integer falseflood=10;
	m.addAttribute("trueflood",trueflood);
	m.addAttribute("falseflood",falseflood);
	return "train";
}
}

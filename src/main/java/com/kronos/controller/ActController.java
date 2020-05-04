package com.kronos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kronos.model.Act;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.kronos.service.ActService;

//is a controller works with the Act's CRUD

@Controller
@RequestMapping(value="/act")
public class ActController {
	
	private Act oldAct = new Act();
	@Value("${kronos.path.folder}")
	private String uploadFolder;
	
	@Autowired
	private ActService actServiceRepo;
	
	@GetMapping("/addAct")
	public String addAct( Act act, Model model) {
		model.addAttribute("act", act);
		return "act/actForm";
	}
	
	@PostMapping("/saveAct")
	public String saveAct(Act act, @RequestParam("pdf") MultipartFile[] uploadingFiles) {
		act.setActive(true);
		act.setPublc(true);
		try {
		for (MultipartFile uploadFile : uploadingFiles) {
			String url = uploadFolder + uploadFile.getOriginalFilename();
			File file = new File(url);
			uploadFile.transferTo(file);
			act.setUrl(url);
			
		}
		actServiceRepo.addAct(act);
		}catch(Exception e) {
			
		}
		return "redirect:/accords/list";
	}
	
	
	@GetMapping("/listAct")
	public String listAct(Model model, HttpSession session, @SessionAttribute("roleName") String roleName ) {
		if(roleName!= null && roleName.equals("Concejo Municipal")) {
			model.addAttribute("listActs", actServiceRepo.findAll());
		}else {
			
		}
		return "act/actList";
	}
	
	@GetMapping("/edit/{sessionDate}")
	public String gotoEdit(@PathVariable("sessionDate") Date sessionDate, Model model,
			RedirectAttributes attributes,@SessionAttribute("roleName") String roleName) {
		try {
			Optional<Act> opt= this.actServiceRepo.getAct(sessionDate);
			if (!opt.isPresent())
				throw new Exception("No se encontro el acuerdo");
			Act act = opt.get();
			model.addAttribute("act", act);
			this.oldAct=act;
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return "act/editAct";
	}
	
	@PostMapping("/saveEdit")
	public String editAct(Act act, Model model) {
		Act oldAct= this.oldAct;
		if(oldAct.getSessionType()!=act.getSessionType()||oldAct.getActive()!=act.getActive()||oldAct.getPublc()!=act.getPublc()) {
		try {
			this.actServiceRepo.updateAct(act);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		}else {
			return"redirect:/act/listAct";
		}
		return "redirect:/act/listAct";
	} 
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	@ModelAttribute
	public void setGenericos(Model model) {
		model.addAttribute("sessions", actServiceRepo.findSessionDates());
	}
	

}

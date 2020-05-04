package com.kronos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.kronos.service.ActService;
import com.kronos.model.Act;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/act")
public class ActRestController {
	
	/*
	 * Rest Controller
	 * Some actions are easier with restful services
	 * And get better user experience avoiding the server to write the html code 
	 * */
	
	
	
	@Value("${kronos.path.folder}")
	private String uploadFolder;
	
	@Autowired
	private ActService actRepo;
	
	@GetMapping("/allActs")
	public List<Act> searchAllAct() {
		
		try {

			return this.actRepo.findAll();

		}

		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
	}
	
	@GetMapping("/deactivatedActs")
	public List<Act> deactivatedActs() {
		
		try {

			return this.actRepo.deactivatedActs();

		}

		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
	}
	
	@GetMapping("/sessionDate/{date}")
	public List<Act> searchActsbySessionDate(@PathVariable("date") String date){
		try {
			// System.out.println(date);
			DateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
			Date dateDb = dateformat.parse(date);
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date2 = sdf.format(dateDb);
			Date db2 = sdf.parse(date2);
			return this.actRepo.searchBySessionDate(db2);

		}

		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
	}
	
	@GetMapping("/getAct/{date}")
	public Act getAct(@PathVariable("date") String date ) {
		
		try {
			DateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
			Date dateDb = dateformat.parse(date);
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date2 = sdf.format(dateDb);
			Date db2 = sdf.parse(date2);
			Optional<Act> opt = this.actRepo.getAct(db2);
			if (!opt.isPresent())
				throw new Exception();
			return opt.get();

		}

		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
	}
	
	@GetMapping("/sessionType/{type}")
	public List<Act> searchActbySessionType(@PathVariable("type") String type){
		try {
			return this.actRepo.searchByType(type);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
	}
	
	@GetMapping("/getPdf")
	public ResponseEntity<Resource> getPdf(@RequestParam("path") String filepath) {
		Path path = Paths.get(filepath);
		Resource resource = null;
		try {
			resource = new UrlResource(path.toUri());
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.body(resource);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}

	}
	
	@RequestMapping(value = "/uploadPdf/{date}", method = RequestMethod.POST)
	public ResponseEntity<Act> uploadPdf(@PathVariable("date") String date,
			@RequestParam("act") MultipartFile[] uploadingFiles

			) {
		
		try {
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date db2 = sdf.parse(date);
			Optional<Act> opt=this.actRepo.getAct(db2);
			if(!opt.isPresent())
				throw new Exception();
			
			Act act=opt.get();
			for (MultipartFile uploadFile : uploadingFiles) {
				if (!uploadFile.isEmpty()) {
					String url = uploadFolder + uploadFile.getOriginalFilename();
					File file = new File(url);
					uploadFile.transferTo(file);
					this.actRepo.updateActPdf(db2, url);
				}

			}
			Optional<Act> optAct=this.actRepo.getAct(db2);
			if(!optAct.isPresent())
				throw new Exception();
			
			Act _act=optAct.get();
			return ResponseEntity.ok(_act);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}

	}
}

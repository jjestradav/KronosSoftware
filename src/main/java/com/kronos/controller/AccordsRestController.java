package com.kronos.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.kronos.model.Accord;
import com.kronos.model.Department;
import com.kronos.model.Pdf;
import com.kronos.model.TempUser;
import com.kronos.model.User;
import com.kronos.pushNotification.FcmClient;
import com.kronos.service.AccordService;
import com.kronos.service.DeparmentService;
import com.kronos.service.NotificationService;
import com.kronos.service.TempUserService;
import com.kronos.service.UserService;

@RestController
@RequestMapping("/api/accords")
public class AccordsRestController {

	/*
	 * Rest Controller Some actions are easier with restful services And get better
	 * user experience avoiding the server to write the html code
	 */

	@Value("${kronos.path.folder}")
	private String uploadFolder;

	@Autowired
	private AccordService accordRepo;

	@Autowired
	private TempUserService tempUserRepo;

	@Autowired
	private FcmClient pushService;

	@Autowired
	private NotificationService notiRepo;

	@Autowired
	private DeparmentService depRepo;

	@Autowired
	private UserService userRepo;

	// List of all accords
	@GetMapping("/allAccords")
	public List<Accord> searchAll() {
		try {

			return this.accordRepo.searchAllAccords();

		}

		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
	}

	// accord by session date
	@GetMapping("/sessionDate/{date}")
	public List<Accord> searchBySessionDate(@PathVariable("date") String date) {
		try {
			// System.out.println(date);
			DateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
			Date dateDb = dateformat.parse(date);
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date2 = sdf.format(dateDb);
			Date db2 = sdf.parse(date2);
			return this.accordRepo.searchBySessionDate(db2);

		}

		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
	}

	// accord by incorporatedDate
	@GetMapping("/incorDate/{date}")
	public List<Accord> searchByIncorDate(@PathVariable("date") String date) {
		try {
			// System.out.println(date);
			DateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
			Date dateDb = dateformat.parse(date);
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date2 = sdf.format(dateDb);
			Date db2 = sdf.parse(date2);
			return this.accordRepo.searchByIncorDate(db2);

		}

		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
	}

	// accords by type
	@GetMapping("/type/{type}")
	public List<Accord> searchByType(@PathVariable("type") int type) {
		try {

			return this.accordRepo.searchByType(type);

		}

		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
	}

	// accord pending by departement
	@GetMapping("/get/pendingDepart/{type}")
	public List<Accord> searchByPendingDepart(@PathVariable("type") char type) {
		try {

			return this.accordRepo.searchByPendingAccordsDepartment(type);

		}

		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
	}

	@GetMapping("/accNumber/{number}")
	public List<Accord> searchByAccNumber(@PathVariable("number") String accNumber) {
		try {

			return this.accordRepo.searchByAccNumber(accNumber);

		}

		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.valueOf(500), "Server Error");
		}
	}

	@GetMapping("/get/allAccords")
	public List<Accord> searchAllAccordsApi() {
		try {

			return this.accordRepo.searchAllAccords();

		}

		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
	}

	// get and accord by its accord number
	@GetMapping("/get/{accNumber}")
	public Accord getAccord(@PathVariable("accNumber") String accNumber) {

		try {
			Optional<Accord> opt = this.accordRepo.getAccord(accNumber);
			if (!opt.isPresent())
				throw new Exception();

			return opt.get();

		}

		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Accord not found");
		}
	}

	// get a single pdf document by its path
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

	// delete pdf by its accord
	@GetMapping("/deletePdf/{accNumber}")
	public ResponseEntity deletePdf(@PathVariable("accNumber") String accNumber,
			@RequestParam("path") String filepath) {

		try {
			this.accordRepo.deleteAccPdf(accNumber, filepath);
			return ResponseEntity.ok().build();
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
			throw new ResponseStatusException(HttpStatus.resolve(500), "entity not found");
		}
	}

	// upload pdf to the accord
	@RequestMapping(value = "/uploadPdf/{accNumber}", method = RequestMethod.POST)
	public ResponseEntity<Accord> uploadPdf(@PathVariable("accNumber") String accNumber,
			@RequestParam(value="accord", required=false) MultipartFile[] uploadingFiles,
			@RequestParam(value = "finalResponse", required = false) String finalResponse,
			@SessionAttribute("roleName") String roleName, @SessionAttribute("user") User user) {

		try {
			
			if(roleName.equals("Secretaria de Alcaldia")) {
				
				return ResponseEntity.ok().build();
			}
			
			else {
			List<String> responsables = this.notiRepo.getResponsablesUserName(accNumber);
			// System.out.println(accNumber);
			Optional<Accord> optAcc = this.accordRepo.getAccord(accNumber);
			if (!optAcc.isPresent())
				throw new Exception();

			Accord acc = optAcc.get();
			if (finalResponse != null && !finalResponse.isEmpty()) {
				Pdf pdf = new Pdf(uploadFolder + finalResponse, true);
				// Verify if any of the pdf is the final response
				Optional<Pdf> opt = this.accordRepo.haveFinalResponse(accNumber);
				if (opt.isPresent()) {
					Pdf pdfOpt = opt.get();

					if (!pdf.getURL().equals(pdfOpt.getURL())) {

						pdfOpt.setFinalResponse(false);
						this.accordRepo.updatePDF(accNumber, pdfOpt);
						this.accordRepo.updatePDF(accNumber, pdf);
					}

				} else {
					this.accordRepo.updatePDF(accNumber, pdf);
				}

				acc.setUser(user);
				if (acc.getState().getId() != 0) {
					acc.getState().setId(0);
					acc.getState().setDescription("Cumplido");
					this.accordRepo.updateAccord(acc);
					String body = "Se le notifica que el Concejo Municipal ha dado por cumplido " + "el acuerdo "
							+ accNumber;

					Department dep = null;
					if (!responsables.isEmpty()) {
						Optional<Department> optDep = this.depRepo.getDepartmentByEmail(responsables.get(0));
						if (optDep.isPresent())
							dep = optDep.get();
					}

					User userBoss = null;
					if (dep != null) {
						Optional<User> optBoss = this.userRepo.getUserBossByDepartment(dep.getId());
						if (opt.isPresent()) {

							String bodyBoss = "Se le notifica que el acuerdo " + acc.getAccNumber()
									+ " en el que es responsable su departamento," + "ha sido dado por cumplido";
							userBoss = optBoss.get();
							// notify the department boss if any pdf is the final response
							this.pushService.send(userBoss.getTempUser().getEmail(),
									"Ha finalizado el acuerdo " + acc.getAccNumber(), bodyBoss);
						}

					}
					for (String userName : responsables) {
						this.pushService.send(userName, "Acuerdo " + accNumber + " Finalizado", body);

					}

					this.pushService.send("alcaldia@sanpablo.go.cr", "Acuerdo " + accNumber + " Finalizado", body);
				}

			}
			// get the pdf files
			for (MultipartFile uploadFile : uploadingFiles) {
				if (!uploadFile.isEmpty()) {
					String url = uploadFolder + uploadFile.getOriginalFilename();
					File file = new File(url);
					uploadFile.transferTo(file);
					Pdf pdf = new Pdf(url);
					this.accordRepo.insertActPdf(accNumber, pdf);
				}

			}
			String body = "Se ha agregado una nueva respuesta al acuerdo  " + accNumber + "\n";
			if (roleName != null && !roleName.equals("Concejo Municipal")) {

				// notify the concejo
				this.pushService.send("concejomunicipal@sanpablo.go.cr", "Respuesta Recibida", body);
			}

			if (roleName != null && !roleName.equals("Concejo Municipal")) {
				for (String userName : responsables) {
					this.pushService.send(userName, "Acuerdo " + accNumber + " Respuesta Recibida", body);
				}
			}

			return ResponseEntity.ok(acc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}

	}

	// get the secretary accords
	@GetMapping("/secretary/searchAllAccords")
	public List<Accord> searchAllAccordsSecretary() {
		try {
			return this.accordRepo.getAccordsSecretary();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
	}

	// get the secretary not asigned accords
	@GetMapping("/secretary/notAssignedAccords")
	public List<Accord> searchNotAssignedAccords() {
		try {
			return this.accordRepo.NotAssignedAccords();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
	}

	// get the secretary not asigned accords
	@GetMapping("/secretary/alreadyAssignedAccords")
	public List<Accord> searchAlreadyAssignedAccords() {
		try {
			return this.accordRepo.alreadyAssignedAccords();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
	}

	// get the responsable of the accord
	@GetMapping("/getResponsablesByAccord/{accNumber}")
	public List<TempUser> getResponsablesByAccord(@PathVariable("accNumber") String accNumber) {
		try {
			return this.tempUserRepo.getTempUsersByAccord(accNumber);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.valueOf(500), "Server Error");
		}

	}

	@RequestMapping(value = "/uploadPdf/permission/{accNumber}", method = RequestMethod.POST)
	public ResponseEntity<String> uploadPdf_RequestPermission(@PathVariable("accNumber") String accNumber,
			@RequestParam("accord") MultipartFile[] uploadingFiles, @SessionAttribute("roleName") String roleName) {

		try {
			if (!roleName.equals("Secretaria de Alcaldia") &&
					!roleName.equals("Concejo Municipal")) {

				for (MultipartFile uploadFile : uploadingFiles) {
					if (!uploadFile.isEmpty()) {
						String url = uploadFolder + uploadFile.getOriginalFilename();
						File file = new File(url);
						uploadFile.transferTo(file);
						Pdf pdf=new Pdf(url,Pdf.PENDING);
						this.accordRepo.insertActPdf(accNumber, pdf);
					}

				}
				String body= "Ha recibido una nueva respuesta por verificar para el acuerdo "+accNumber;
				this.pushService.send("alcaldia@sanpablo.go.cr","Respuesta por verificar",body);
				return ResponseEntity.ok(accNumber);
			}
			else
				throw new Exception("User does not have permission");

		} catch (Exception e) {

			throw new ResponseStatusException(HttpStatus.valueOf(500), e.getMessage());
		}

	}
	
	@GetMapping("/updateUrl/{accNumber}/{url}/{value}")
	public ResponseEntity<String> updatePdfStatus(@PathVariable("accNumber") String accNumber,
			@PathVariable("url") String url, @PathVariable("value") int value){
		try {
			String filePath=this.uploadFolder+url;
			this.accordRepo.updatePdfState(accNumber, filePath, value);
			
			List<String> responsables=this.notiRepo.getResponsablesUserName(accNumber);
			
			if(value==2) {
				
				String body= "Ha recibido una nueva respuesta para el acuerdo "+accNumber;
				this.pushService.send("concejomunicipal@sanpablo.go.cr","Respuesta Recibida",body);
			}
			
			
			String status=(value==2)?"Aprobada": "Rechazada";
			String resBody="La respuesta para el acuerdo "+accNumber+ " ha sido"+status; 
			
			
				for(String us :responsables) 
					this.pushService.send(us, "Respuesta "+status,resBody);
				
			
			return ResponseEntity.ok().build();
		}
		catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.valueOf(500), e.getMessage());
		}
	}
	

}

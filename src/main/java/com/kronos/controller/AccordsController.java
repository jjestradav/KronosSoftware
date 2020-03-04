package com.kronos.controller;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kronos.model.Accord;
import com.kronos.model.AccordTempUser;
import com.kronos.model.DepUserDTO;
import com.kronos.model.Department;
import com.kronos.model.Pdf;
import com.kronos.model.State;
import com.kronos.model.TempUser;
import com.kronos.model.User;
import com.kronos.pushNotification.FcmClient;
import com.kronos.service.AccordService;
import com.kronos.service.AccordTempUserService;
import com.kronos.service.ActService;
import com.kronos.service.DeparmentService;
import com.kronos.service.NotificationService;
import com.kronos.service.StateService;
import com.kronos.service.TempUserService;
import com.kronos.service.TypeService;
import com.kronos.service.UserService;

@Controller
@RequestMapping(value = "/accords")
public class AccordsController {

	private Accord oldAccord=new Accord();
	
	@Value("${kronos.path.folder}")
	private String uploadFolder;
	
	@Autowired
	private TypeService typesRepo;

	@Autowired
	private StateService statesRepo;

	@Autowired
	private ActService actRepo;

	@Autowired
	private TempUserService tempUserRepo;

	@Autowired
	private AccordService accordRepo;

	@Autowired
	private AccordTempUserService tUserAccRepo;

	@Autowired
	private DeparmentService deptService;
	
	@Autowired
	private NotificationService notiService;
	
	@Autowired
	private UserService userService; 
	@Autowired
	private FcmClient pushService;
	
	
	@PostMapping("/saveAccord")
	public String saveAccord(Accord accord, @RequestParam(value = "username", required = false) String username,

			@RequestParam(value = "email", required = false) String email,

			@RequestParam("accord") MultipartFile[] uploadingFiles, RedirectAttributes attributes,
			 @SessionAttribute("roleName") String roleName) {

		try {
			String fullAccName = "MSPH-CM-ACUER-" + accord.getAccNumber();
			accord.setAccNumber(fullAccName);
			accord.setIncorporatedDate(new Date());
			accord.setIncorporatedTime(LocalTime.now());
			accord.setState(new State(Accord.PENDING_STATE));

			// System.out.println(accord);

			if (!this.actRepo.isActInDB(accord.getSessionDate()))
				this.actRepo.insertAct(accord.getSessionDate());

			TempUser tmp = null;

			if (accord.getType().getId() != Accord.ADMIN_TYPE) {

				if (email.isEmpty() || username.isEmpty())
					throw new Exception();

				Optional<TempUser> opt = this.tempUserRepo.findByEmail(email);
				if (opt.isPresent())
					tmp = opt.get();

				else {
					tmp = new TempUser(username, email);
					this.tempUserRepo.insertTempUser(tmp);
				}

			}

			for (MultipartFile uploadFile : uploadingFiles) {
				String url = uploadFolder + uploadFile.getOriginalFilename();
				File file = new File(url);
				uploadFile.transferTo(file);
				accord.getURL().add(new Pdf(url));

			}
			User user = new User();
			user.setTempUser(new TempUser("concejomunicipal@sanpablo.go.cr"));
			accord.setUser(user);
			System.out.println(accord);
			this.accordRepo.insertAccord(accord);

			if (accord.getType().getId() != Accord.ADMIN_TYPE)
				this.tUserAccRepo.insertAccord_TempUser(accord, tmp);
			
			  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			  String body="Se ha agregado un nuevo Acuerdo\n"+
			  "Agregado por: "+roleName+"\n"+
			  "En la fecha:" + LocalDateTime.now().format(formatter)+  "\n";
			  
			pushService.send("SecretariadeAlcaldia", "Acuerdo Agregado", body);

		}

		catch (Exception e) {
			System.out.println(e.getMessage());
			attributes.addFlashAttribute("msg", "Ha Ocurrido un error");
			return "redirect:/accords/list";
		}

		attributes.addFlashAttribute("msg", "Acuerdo Agregado con Exito");
		return "redirect:/accords/list";
	}

	@GetMapping("/deleteAccord/{accNumber}")
	public String deleteAccord(@PathVariable("accNumber") String accNumber, RedirectAttributes attributes) {

		try {
			System.out.println(accNumber);
			String user = "concejomunicipal@sanpablo.go.cr";
			this.accordRepo.deleteAccord(accNumber, user);
			attributes.addFlashAttribute("msg", "Acuerdo Eliminado con exito");
		} catch (Exception e) {
			attributes.addFlashAttribute("msgError", "El Acuerdo n pudo ser eliminado correctamente");
		}
		return "redirect:/accords/list";
	}

	@GetMapping("/addAccord")
	public String createAccord(Accord accord, Model model) {

		model.addAttribute("accord", accord);
		return "accord/accordForm";
	}

	//list of all accords
	@GetMapping("/list")
	public String listAccord(Model model, HttpSession session,
			@SessionAttribute("roleName") String roleName) {

		try {
			if(roleName != null && roleName.equals("Concejo Municipal")) {
				Date today= new Date();
				model.addAttribute("listAccords", this.accordRepo.todayDeadlineAccors(today));
				
			}
			else {
			model.addAttribute("listAccords", this.accordRepo.searchAllAccords());
			}
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

		return "accord/listAccord";
	}
	
	//Department list
	@GetMapping("/listDepart")
	public String listAccordDepart(Model model) {
		try {
			System.out.println("Entramos");
			model.addAttribute("listAccordsDepart", this.accordRepo.searchAllAccords());
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}
		return "accord/listAccordDepartment";
	}

	@GetMapping("/edit/{accNumber}")

	public String goToEdit(@PathVariable("accNumber") String accNumber, Model model,
			RedirectAttributes attributes,@SessionAttribute("roleName") String roleName) {

		try {
			Optional<Accord> opt = this.accordRepo.getAccord(accNumber);
			if (!opt.isPresent())
				throw new Exception("No se encontro el acuerdo");

			Accord acc = opt.get();
			model.addAttribute("accord", acc);
			this.oldAccord=acc;
			
			
		
		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return (roleName.equals("Concejo Municipal")) ? "accord/editAccord": "accord/secretary/AccordView";
	}

	
	@PostMapping("/saveEdit")
	public String editAccord(Accord acc, Model model,
			@RequestParam(value = "username", required = false) String username,

			@RequestParam(value = "email", required = false) String email, RedirectAttributes attributes,
			 @SessionAttribute("user") User user, 
			 @SessionAttribute("roleName") String roleName,
			 @RequestParam(value = "jsonObject", required = false) Optional<String> department
			) {

		try {
			
			if(roleName.equals("Secretaria de Alcaldia")) {
				
				if(department.isPresent()) {
				String json= department.get(); 
				//  System.out.println(json);
				//ObjectMapper mapper = new ObjectMapper();
				//mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			//	DepUserDTO[] pp1 = mapper.readValue(json, DepUserDTO[].class);
				
				
				JSONArray jsonArr = new JSONArray(json);
			    List<DepUserDTO> dataList = new ArrayList<DepUserDTO>();
			    for (int i = 0; i < jsonArr.length(); i++) {
			        JSONObject jsonObj = jsonArr.getJSONObject(i);
			        DepUserDTO data = new DepUserDTO();
			        data.setNombre((jsonObj.getString("Usuario")));
			        data.setDepartamento(jsonObj.getString("Departamento"));
			        dataList.add(data);
			    }
			//	List<DepUserDTO> ppl2 = Arrays.asList(mapper.readValue(json, DepUserDTO[].class));
			  // for(DepUserDTO dto : ppl2 ) {
				   for(DepUserDTO dto : dataList) {
					   Optional<User> opt= this.userService.getUserByEmail(dto.getNombre());
					   if(opt.isPresent()) {
						   this.notiService.insertNotification(acc, dto.getNombre());
					   }
				   }
				   attributes.addFlashAttribute("msg", "Acuerdo Actualizado Correctamente");
			   //}
				}	
			}
			else {
			Accord oldAccord= this.oldAccord;
			
			System.out.println("Viejo: "+ oldAccord);
			//User user=(User) session.getAttribute("user");
			//System.out.println(hola);
			if (acc.getType().getId() != Accord.ADMIN_TYPE && oldAccord.getType().getId() == Accord.ADMIN_TYPE) {

				if (!email.isEmpty() && !username.isEmpty()) {
					System.out.println(email);
					System.out.println(username);
					this.tUserAccRepo.insertAccord_TempUser(acc, new TempUser(username,email));
				}
				
			}
			else {
				System.out.println("");
				//if temp user is in DB
				//insert in DB if not
				//update T_USERACC
			}
			
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String newDate = format.format(acc.getSessionDate());
			String oldDate = format.format(oldAccord.getSessionDate());
			
			if (!newDate.equals(oldDate)) {
				if(!this.actRepo.isActInDB(acc.getSessionDate())) {
					this.actRepo.insertAct(acc.getSessionDate());
				}
			}
			acc.setUser(user);
			System.out.println(user);
			System.out.println("Nuevo: "+ acc);
			this.accordRepo.updateAccord(acc);
			if(roleName != null && roleName.equals("Secretaria de Alcaldia")) {
				String body="El acuerdo "+acc.getAccNumber()+" ha sido editado\n";
				
				this.pushService.send("ConcejoMunicipal", "Acuerdo Actualizado", body);
			}
			attributes.addFlashAttribute("msg", "Acuerdo Editado Correctamente");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			attributes.addFlashAttribute("msgError", "Ocurrio un error al editar");
		}
		return "redirect:/accords/list";
	}

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	@ModelAttribute
	public void setGenericos(Model model) {
	
		model.addAttribute("states", this.statesRepo.findAll());
		model.addAttribute("types", this.typesRepo.findAll());
		model.addAttribute("departments", this.deptService.findAll());
	}
	

}

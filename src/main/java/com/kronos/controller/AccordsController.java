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
import com.kronos.schedule.EmailServiceImpl;
import com.kronos.service.AccordDepartmentService;
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

	private Accord oldAccord = new Accord();

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
	private AccordDepartmentService AccDprmntRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private FcmClient pushService;

	@Autowired
	private EmailServiceImpl email;

	// Save the accord in new action
	@PostMapping("/saveAccord")
	public String saveAccord(Accord accord,

			@RequestParam("accord") MultipartFile[] uploadingFiles, RedirectAttributes attributes,
			@SessionAttribute("roleName") String roleName,
			@RequestParam("responsables") Optional<String> optResponsables) {

		try {
			String fullAccName = "MSPH-CM-ACUER-" + accord.getAccNumber();
			accord.setAccNumber(fullAccName);
			accord.setIncorporatedDate(new Date());
			accord.setIncorporatedTime(LocalTime.now());
			accord.setState(new State(Accord.PENDING_STATE));
			List<String> urlPaths = new ArrayList<>();
			List<TempUser> tmpUsers = new ArrayList<>();
			// System.out.println(accord);

			if (!this.actRepo.isActInDB(accord.getSessionDate()))
				this.actRepo.insertAct(accord.getSessionDate());

			TempUser tmp = null;

			// Pdf documents in the form
			for (MultipartFile uploadFile : uploadingFiles) {
				String url = uploadFolder + uploadFile.getOriginalFilename();
				File file = new File(url);
				uploadFile.transferTo(file);
				urlPaths.add(url);
				accord.getURL().add(new Pdf(url, false, Pdf.NO_PERMISSION, true));

			}

			// if the accord isnt admin type it means it has emails in it
			// as responsables
			if (accord.getType().getId() != Accord.ADMIN_TYPE) {

				if (optResponsables.isPresent()) {
					String json = optResponsables.get();

					// get the responsables as a json array
					JSONArray jsonArr = new JSONArray(json);

					for (int i = 0; i < jsonArr.length(); i++) {
						JSONObject jsonObj = jsonArr.getJSONObject(i);
						TempUser data = new TempUser();
						data.setEmail(jsonObj.getString("email"));
						data.setName(jsonObj.getString("username"));
						tmpUsers.add(data);
					}
		
					for (TempUser tpUser : tmpUsers) {
						Optional<TempUser> opt = this.tempUserRepo.findByEmail(tpUser.getEmail());
						if (!opt.isPresent())
							this.tempUserRepo.insertTempUser(tpUser);

					}

				}

			}
			User user = new User();
			user.setTempUser(new TempUser("concejomunicipal@sanpablo.go.cr"));
			accord.setUser(user);
			this.accordRepo.insertAccord(accord);

			if (accord.getType().getId() != Accord.ADMIN_TYPE) {
				for (TempUser tpUser : tmpUsers) {
					this.tUserAccRepo.insertAccord_TempUser(accord, tpUser);
				}

			}

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			String body = "Se ha agregado un nuevo Acuerdo\n" + "Agregado por: " + roleName + "\n" + "En la fecha:"
					+ LocalDateTime.now().format(formatter) + "\n";

			// notify the secretary of the new accord
			pushService.send("alcaldia@sanpablo.go.cr", "Acuerdo Agregado", body);
			// send an email to the responsables
			DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			for (TempUser tpUser : tmpUsers) {
				for (String url : urlPaths) {

					this.email.sendMailWithAttachment(tpUser.getEmail(), "Acuerdo Municipal",
							"Se le informa que ha sido notificado del siguiente acuerdo: " + accord.getAccNumber()
									+ " con fecha límite de " + format.format(accord.getDeadline()) + ".\n"
									+ "Se adjunta el pdf del oficio",
							url);
				}
			}

		}

		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			attributes.addFlashAttribute("msgError", "Ha Ocurrido un error");
			return "redirect:/accords/list";
		}

		attributes.addFlashAttribute("msg", "Acuerdo Agregado con Exito");
		return "redirect:/accords/list";
	}

	// delete the accord with the accordNumber
	@GetMapping("/deleteAccord/{accNumber}")
	public String deleteAccord(@PathVariable("accNumber") String accNumber, RedirectAttributes attributes) {

		try {
			String user = "concejomunicipal@sanpablo.go.cr";
			// this also delete the pdfs in the database and the accord reference in other
			// tables
			this.accordRepo.deleteAccord(accNumber, user);

		} catch (Exception e) {
			attributes.addFlashAttribute("msgError", "El Acuerdo no pudo ser eliminado correctamente");
		}
		attributes.addFlashAttribute("msg", "Acuerdo Eliminado con exito");
		return "redirect:/accords/list";
	}

	// set the view to the new accord form
	@GetMapping("/addAccord")
	public String createAccord(Accord accord, Model model) {

		model.addAttribute("accord", accord);
		return "accord/accordForm";
	}

	// set the view to the list of all accords
	@GetMapping("/list")
	public String listAccord(Model model, HttpSession session, @SessionAttribute("roleName") String roleName) {

		try {
			if (roleName != null && roleName.equals("Concejo Municipal")) {
				Date today = new Date();
				model.addAttribute("listAccords", this.accordRepo.todayDeadlineAccors(today));

			} else {
				model.addAttribute("listAccords", this.accordRepo.searchAllAccords());
			}
		} catch (Exception e) {

			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return "accord/listAccord";
	}

	// set the view to the list of accords by department
	@GetMapping("/listDepart")
	public String listAccordDepart(Model model, Accord accord) {
		try {

			model.addAttribute("accord", accord);
			model.addAttribute("types", this.typesRepo.findAll());
			// model.addAttribute("listAccordsDepart",
			// this.accordRepo.searchByPendingAccordsDepartment('A'));

		} catch (Exception e) {

			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return "accord/listAccordDepartment";
	}

	// go the the edit view with the accord
	@GetMapping("/edit/{accNumber}")

	public String goToEdit(@PathVariable("accNumber") String accNumber, Model model, RedirectAttributes attributes,
			@SessionAttribute("roleName") String roleName) {

		try {
			// find the accord in the DB by its accord number
			Optional<Accord> opt = this.accordRepo.getAccord(accNumber);
			if (!opt.isPresent())
				throw new Exception("No se encontro el acuerdo");

			Accord acc = opt.get();
			model.addAttribute("accord", acc);
			// auxiliar variable
			this.oldAccord = acc;

		}

		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return (roleName.equals("Concejo Municipal")) ? "accord/editAccord" : "accord/secretary/AccordView";
	}

	// Save the accord in the edit action
	@PostMapping("/saveEdit")
	public String editAccord(Accord acc, Model model,
			@RequestParam(value = "username", required = false) String username,

			@RequestParam(value = "email", required = false) String email, RedirectAttributes attributes,
			@SessionAttribute("user") User user, @SessionAttribute("roleName") String roleName,
			@RequestParam(value = "jsonObject", required = false) Optional<String> department) {

		try {

			// if the role is the secretary we need to get the responsable(s) departement(s)
			// and put them in the DB
			if (roleName.equals("Secretaria de Alcaldia")) {

				if (department.isPresent()) {
					String json = department.get();
					// got the responsables by a json array
					JSONArray jsonArr = new JSONArray(json);
					String body = "Se le notifica que su departamento ha recibido el" + " acuerdo " + acc.getAccNumber()
							+ ". Por favor asignarle un responsable";
					List<Department> dataList = new ArrayList<>();
					for (int i = 0; i < jsonArr.length(); i++) {
						JSONObject jsonObj = jsonArr.getJSONObject(i);
						Department data = new Department();
						data.setId(jsonObj.getInt("ID"));
						data.setName(jsonObj.getString("Nombre"));
						dataList.add(data);
					}
					for (Department dto : dataList) {
						Optional<User> opt = this.userService.getBossByDepartment(dto);

						// notify the boss of the department
						if (opt.isPresent())
							this.pushService.send(opt.get().getTempUser().getEmail(), "Acuerdo Recibido", body);

						this.AccDprmntRepo.insertAccordDepartment(acc, dto);
					}
					State state = new State(Accord.RECEIVED_STATE, "Recibido");
					acc.setState(state);
					this.accordRepo.updateAccordState(acc.getAccNumber(), state.getId());
					attributes.addFlashAttribute("msg", "Acuerdo Actualizado Correctamente");
				}

				return "redirect:/townHall/homeTownHall";
			} else {
				Accord oldAccord = this.oldAccord;

				if (acc.getType().getId() != Accord.ADMIN_TYPE && oldAccord.getType().getId() == Accord.ADMIN_TYPE) {

				if (!email.isEmpty() && !username.isEmpty()) {
						this.tUserAccRepo.insertAccord_TempUser(acc, new TempUser(username, email));
					}

				} else {
					System.out.println(" TO DO");
					// if temp user is in DB
					// insert in DB if not
					// update T_USERACC
				}

				DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				String newDate = format.format(acc.getSessionDate());
				String oldDate = format.format(oldAccord.getSessionDate());

				if (!newDate.equals(oldDate)) {
					if (!this.actRepo.isActInDB(acc.getSessionDate())) {
						this.actRepo.insertAct(acc.getSessionDate());
					}
				}
				acc.setUser(user);
				this.accordRepo.updateAccord(acc);
				if (roleName != null && roleName.equals("Secretaria de Alcaldia")) {
					String body = "El acuerdo " + acc.getAccNumber() + " ha sido editado\n";

					this.pushService.send("concejomunicipal@sanpablo.go.cr", "Acuerdo Actualizado", body);
				}
				attributes.addFlashAttribute("msg", "Acuerdo Editado Correctamente");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			attributes.addFlashAttribute("msgError", "Ocurrio un error al editar");
		}
		return "redirect:/accords/list";
	}

	// Binder for the date
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	// some variables in the view
	@ModelAttribute
	public void setGenericos(Model model) {

		model.addAttribute("states", this.statesRepo.findAll());
		model.addAttribute("types", this.typesRepo.findAll());
		model.addAttribute("departments", this.deptService.findAll());
	}

}

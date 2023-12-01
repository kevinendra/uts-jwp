package com.uts.jwp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.uts.jwp.domain.Teacher;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class TeacherController {
public static Map<String, Teacher> teacherMap = new HashMap<>();

    @GetMapping("/teachers")
    public String getTeachers(Model model) {
        model.addAttribute("teachers", fetchTeachers());
        return "index";
    }

	@GetMapping("/signup")
    public String showSignUpForm(Teacher teacher) {
        return "addTeachers";
    }

    @PostMapping("/teachers")
    public String addTeachers(@Valid Teacher teacher, BindingResult bindingResult, Model model) {

       
        String errorNIP = validateNIP(teacher.getNip());
        if (errorNIP != null) {
            ObjectError error = new ObjectError("globalError", errorNIP);
            bindingResult.addError(error);
        }

        
        String errorEmail = validateEmail(teacher.getEmail());
        if (errorEmail != null) {
            //ObjectError error = new ObjectError("globalError", errorEmail);
            ObjectError error = new ObjectError("Email", errorEmail);
            bindingResult.addError(error);
        }

        
        String errorPhoneNumber = validatePhoneNumber(teacher.getPhoneNumber());
        if (errorPhoneNumber != null) {
            //ObjectError error = new ObjectError("globalError", errorPhoneNumber);
            ObjectError error = new ObjectError("PhoneNumber", errorPhoneNumber);
            bindingResult.addError(error);
        }

        String duplicateDataError = checkDuplicateData(teacher);
        if (duplicateDataError != null) {
            //ObjectError error = new ObjectError("globalError", duplicateDataError);
            ObjectError error = new ObjectError("Duplicate Data", duplicateDataError);
            bindingResult.addError(error);
        }

        log.info("bindingResult {}", bindingResult);

        if (bindingResult.hasErrors()) {
            return "addTeachers";
        }

        String nip = teacher.getNip();
        boolean exists = teacherMap.values().stream()
                .anyMatch(data -> nip.equals(data.getNip()));

        if (exists) {
            throw new IllegalArgumentException("Teacher --> NIP :" + nip + " is already exist");
        }

        teacherMap.put(nip, teacher);
        model.addAttribute("teachers", fetchTeachers());
        return "index";
    }

    private String validateNIP(String nip) {
       
        //if (!nip.startsWith("LCT") || !nip.substring(3).matches("\\d{5}")) {
            //return "NIP must start with 'LCT' and be followed by 5 digits";
        //}
        
        if(nip.length() < 3)
        {
             return "NIP minimum 3 character.";
        }

         if(nip.length() > 10)
        {
             return "NIP maximum 10 character.";
        }

        if (!nip.matches("[0-9]*")) {
            return "NIP must character number.";
        }

        return null;
    }

    private String validateEmail(String email) {
        return null; 
    }

    private String validatePhoneNumber(String phoneNumber) {
         if (!phoneNumber.matches("[0-9]*")) {
            return "Phone Number must character number.";
        }

        return null; 
    }

    private String checkDuplicateData(Teacher teacher) {
       
        boolean exists = teacherMap.values().stream()
                .anyMatch(data ->
                        teacher.getEmail().equals(data.getEmail()) ||
                        teacher.getNip().equals(data.getNip()) ||
                        teacher.getPhoneNumber().equals(data.getPhoneNumber())
                );

        if (exists) {
            return "Data Teacher NIP, Email, or Phone Number already exists";
        }

        return null;
    }

	@GetMapping(value = "/teachers/{nip}")
    public ResponseEntity<Teacher> findTeacher(@PathVariable("nip") String nip) {
        final Teacher teacher = teacherMap.get(nip);
        return new ResponseEntity<>(teacher, HttpStatus.OK);
    }

	private static List<Teacher> fetchTeachers() {
        return teacherMap.values().stream().collect(Collectors.toList());
    }

	@PostMapping(value = "/teachers/{nip}")
    public String updateTeachers(@PathVariable("nip") String nip,
                                Teacher teacher,
                                BindingResult result, Model model) {
                                    
        final Teacher teacherToBeUpdated = teacherMap.get(teacher.getNip());
        teacherToBeUpdated.setFullName(teacher.getFullName());
        teacherToBeUpdated.setEmail(teacher.getEmail());
        teacherToBeUpdated.setPhoneNumber(teacher.getPhoneNumber());
        teacherMap.put(teacher.getNip(), teacherToBeUpdated);

        model.addAttribute("teachers", fetchTeachers());
        return "redirect:/teachers";
    }

	@GetMapping("/edit/{nip}")
    public String showUpdateForm(@PathVariable("nip") String nip, Model model) {
        final Teacher teacherToBeUpdated = teacherMap.get(nip);
        if (teacherToBeUpdated == null) {
            throw new IllegalArgumentException("Teacher Update -->  NIP:" + nip + " Is Not Found.");
        }
        model.addAttribute("teacher", teacherToBeUpdated);
        return "updateTeachers";
    }

	@GetMapping(value = "/teachers/{nip}/delete")
    public String deleteTeacher(@PathVariable("nip") String nip) {
        final Teacher teacherToBeDelete = teacherMap.get(nip);
        if (teacherToBeDelete == null) {
            throw new IllegalArgumentException("Teacher Delete -->  NIP:" + nip + " Is Not Found.");
        }
        teacherMap.remove(nip);
        return "redirect:/teachers";
    }
}

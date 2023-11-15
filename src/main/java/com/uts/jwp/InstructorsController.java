package com.uts.jwp;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.uts.jwp.domain.Instructors;

@RestController
public class InstructorsController {
    public static Map<String, Instructors> instructorsMap = new HashMap<>();

    @GetMapping("/instructors")
    public List<Instructors> getInstructors() {
        return instructorsMap.values().stream().collect(Collectors.toList());
    }

        @PostMapping("/instructors")
    public ResponseEntity<String> addInstructors(@RequestBody Instructors instructors) {
        instructorsMap.put(instructors.getnip(), instructors);
        Instructors savedInstructors = instructorsMap.get(instructors.getnip());
        return new ResponseEntity<>("Instructors with nip: " + savedInstructors.getnip() +
                "has been created", HttpStatus.OK);
    }

    @GetMapping(value = "/instructors/{nip}")
    public ResponseEntity<Instructors> findInstructors(@PathVariable("nip") String nip) {
        final Instructors instructors = instructorsMap.get(nip);
        return new ResponseEntity<>(instructors, HttpStatus.OK);
    }

     @PutMapping(value = "/instructors/{nip}")
    public ResponseEntity<String> updateInstructors(@PathVariable("nip") String nip,
            @RequestBody Instructors instructors) {
        final Instructors instructorsToBeUpdated = instructorsMap.get(instructors.getnip());
        instructorsToBeUpdated.setfullName(instructors.getfullName());
        instructorsToBeUpdated.setemail(instructors.getemail());
        instructorsToBeUpdated.setnophone(instructors.getnophone());

        instructorsMap.put(instructors.getnip(), instructorsToBeUpdated);
        return new ResponseEntity<>("Instructors with nip: " + instructorsToBeUpdated.getnip() +
                "has been updated", HttpStatus.OK);
    }

    @DeleteMapping(value = "/instructors/{nip}")
    public ResponseEntity<Void> deleteInstructors(@PathVariable("nip") String nip) {
        instructorsMap.remove(nip);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

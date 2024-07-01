package ips.mannawarhussain.uk.ips.controller;


import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ips.mannawarhussain.uk.ips.dto.PatientDto;
import ips.mannawarhussain.uk.ips.helper.PatientList;
import ips.mannawarhussain.uk.ips.model.PatientEntity;
import ips.mannawarhussain.uk.ips.repository.PatientRepository;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3020")
@RestController
@RequestMapping("/fhir")
public class PatientResourceProviderlocaldb {

    @Autowired
    private PatientRepository patientRepository;


    @PostMapping("/Patientdb")
    public ResponseEntity<Map<String, String>> createPatient(@Validated @RequestBody PatientDto patientDto){
        Map<String, String> response = new HashMap<>();
        try {
            PatientEntity patient = new PatientEntity();
            patient.setName(patientDto.getName());
            patient.setBirthDate(patientDto.getBirthDate());
            patient.setGender(patientDto.getGender());
            patientRepository.save(patient);

            response.put("id", String.valueOf(patient.getId()));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch(Exception e){
            response.put("message", "An error occurred while creating the patient: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/Patientdb")
    public ResponseEntity<List<PatientEntity>> getAllPatients(){
        try {
            List<PatientEntity> patients = patientRepository.findAll();

            if(patients.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok().body(patients);

        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/Patientdb/{id}")
    public ResponseEntity<PatientEntity> getPatient(@PathVariable Long id){
        try {
            Optional<PatientEntity> patient = patientRepository.findById(id);
            if(patient.isPresent()){
                return ResponseEntity.ok().body(patient.get());
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/Patientdb/{id}")
    public ResponseEntity<Map<String, String>> updatePatient(@PathVariable Long id, @RequestBody PatientDto patientDto){
        Map<String, String> response = new HashMap<>();
        try {
            Optional<PatientEntity> patientOptional = patientRepository.findById(id);
            if(patientOptional.isPresent()){
                PatientEntity patient = patientOptional.get();
                patient.setName(patientDto.getName());
                patient.setGender(patientDto.getGender());
                patient.setBirthDate(patientDto.getBirthDate());
                patientRepository.save(patient);
                response.put("message", "Patient updated successfully");
                return ResponseEntity.ok(response);
            }else {
                response.put("message", "Patient not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            response.put("message", "An error occurred while updating the patient: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/Patientdb/{id}")
    public ResponseEntity<Map<String, String>> deletePatient(@PathVariable Long id){
        Map<String, String> response = new HashMap<>();
        try {
            Optional<PatientEntity> patientOptional = patientRepository.findById(id);
            if(patientOptional.isPresent()){
                patientRepository.deleteById(id);
                response.put("message", "Patient deleted successfully");
                return ResponseEntity.ok(response);
            }else{
                response.put("message", "Patient not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            response.put("message", "An error occurred while deleting the patient: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/Patientdb/search")
    public ResponseEntity<List<PatientEntity>> searchPatient(@RequestParam(required=false) Long id,
                                                @RequestParam(required=false) String name){
        try{
            List<PatientEntity> patients;
            if(id != null){
                Optional<PatientEntity> patientOptional = patientRepository.findById(id);
                patients = patientOptional.map(List::of).orElseGet(List::of);
            }else if(name != null){
                patients = patientRepository.findByNameContainingIgnoreCase(name);
            }else {
                patients = patientRepository.findAll();
            }

            if(patients.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok().body(patients);

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

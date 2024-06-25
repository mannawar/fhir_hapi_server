package ips.mannawarhussain.uk.ips.controller;

import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ips.mannawarhussain.uk.ips.helper.PatientList;
import org.hl7.fhir.r5.model.Bundle;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Patient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fhir")
public class PatientResourceProvider {

    @Autowired
    private FhirContext fhirContext;

    private static final String SERVER_BASE = "https://hapi.fhir.org/baseR5";

    @PostMapping("/Patient")
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody String patientJson){
        Patient patient = fhirContext.newJsonParser().parseResource(Patient.class, patientJson);
        IGenericClient client = fhirContext.newRestfulGenericClient(SERVER_BASE);
        MethodOutcome outcome = client.create().resource(patient).execute();
        Map<String, String> response = new HashMap<>();

        if(outcome.getCreated()){
            response.put("id", outcome.getId().getIdPart());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/Patient")
    public ResponseEntity<String> getAllPatients(){
        IGenericClient client = fhirContext.newRestfulGenericClient(SERVER_BASE);
        try {
            Bundle results = client.search().forResource(Patient.class).returnBundle(Bundle.class).execute();
            if(results.getEntry().isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"No patients found.\"}");
            }
            List<Patient> patients = results.getEntry().stream().map(entry -> (Patient) entry.getResource())
                    .collect(Collectors.toList());

            PatientList patientList = new PatientList();
            patientList.setPatients(patients);

            IParser jsonParser= fhirContext.newJsonParser();
            jsonParser.setPrettyPrint(true);

            String patientJson = jsonParser.encodeResourceToString(results);

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(patientJson);
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"An error occurred while fetching " +
                    "patients" +
                    ".\"}");
        }
    }

    @GetMapping("Patient/{id}")
    public ResponseEntity<String> getPatient(@PathVariable String id){
        IGenericClient client = fhirContext.newRestfulGenericClient(SERVER_BASE);

        try {
            Patient patient = client.read().resource(Patient.class).withId(id).execute();
            if(patient != null){
                String patientJson = fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body(patientJson);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("Patient/{id}")
    public ResponseEntity<String> updatePatient(@PathVariable String id, @RequestBody String patientJson){
        IGenericClient client = fhirContext.newRestfulGenericClient(SERVER_BASE);
        Patient patient = fhirContext.newJsonParser().parseResource(Patient.class, patientJson);
        patient.setId(new IdType("Patient", id));
        MethodOutcome outcome = client.update().resource(patient).execute();

        String updatedPatientJson = fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
        return new ResponseEntity<>(updatedPatientJson, HttpStatus.OK);
    }

    @DeleteMapping("/Patient/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable String id){
        IGenericClient client = fhirContext.newRestfulGenericClient(SERVER_BASE);
        client.delete().resourceById(new IdType("Patient", id)).execute();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

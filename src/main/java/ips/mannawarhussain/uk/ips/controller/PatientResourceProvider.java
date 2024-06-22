package ips.mannawarhussain.uk.ips.controller;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Patient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fhir")
public class PatientResourceProvider {

    @Autowired
    private FhirContext fhirContext;

    private static final String SERVER_BASE = "https://hapi.fhir.org/baseR5";

    @PostMapping("/Patient")
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient){
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

    @GetMapping("Patient/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable String id){
        IGenericClient client = fhirContext.newRestfulGenericClient(SERVER_BASE);

        try {
            Patient patient = client.read().resource(Patient.class).withId(id).execute();
            if(patient != null){
                return new ResponseEntity<>(patient, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("Patient/{id}")
    public ResponseEntity<MethodOutcome> updatePatient(@PathVariable String id, @RequestBody Patient patient){
        IGenericClient client = fhirContext.newRestfulGenericClient(SERVER_BASE);
        patient.setId(new IdType("Patient", id));
        MethodOutcome outcome = client.update().resource(patient).execute();
        return new ResponseEntity<>(outcome, HttpStatus.OK);
    }

    @DeleteMapping("/Patient/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable String id){
        IGenericClient client = fhirContext.newRestfulGenericClient(SERVER_BASE);
        client.delete().resourceById(new IdType("Patient", id)).execute();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

package ips.mannawarhussain.uk.ips.helper;

import org.hl7.fhir.r5.model.Patient;

import java.util.List;

public class PatientList {
    private List<Patient> patients;

    public List<Patient> getPatient(){
        return patients;
    }

    public void setPatients(List<Patient> patients){
        this.patients = patients;
    }
}

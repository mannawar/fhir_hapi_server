package ips.mannawarhussain.uk.ips.repository;

import ips.mannawarhussain.uk.ips.model.PatientEntity;;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PatientRepository extends JpaRepository<PatientEntity, Long>{
    List<PatientEntity> findByNameContainingIgnoreCase(String name);
    Optional<PatientEntity> findById(Long id);
}


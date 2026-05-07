package ra.edu.ptit_cntt2_it210_project.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.ptit_cntt2_it210_project.model.entity.Labs;

import java.util.List;

public interface LabService {
    List<Labs> findAll();
}

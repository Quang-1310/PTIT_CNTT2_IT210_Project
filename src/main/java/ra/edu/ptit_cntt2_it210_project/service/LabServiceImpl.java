package ra.edu.ptit_cntt2_it210_project.service;

import org.springframework.stereotype.Service;
import ra.edu.ptit_cntt2_it210_project.model.entity.Labs;
import ra.edu.ptit_cntt2_it210_project.repository.LabRepository;

import java.util.List;

@Service
public class LabServiceImpl implements LabService{
    private final LabRepository labRepository;
    public LabServiceImpl(LabRepository labRepository){
        this.labRepository = labRepository;
    }
    @Override
    public List<Labs> findAll() {
        return labRepository.findAll();
    }
}

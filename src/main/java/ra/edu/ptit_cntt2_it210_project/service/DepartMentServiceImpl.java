package ra.edu.ptit_cntt2_it210_project.service;

import org.springframework.stereotype.Service;
import ra.edu.ptit_cntt2_it210_project.model.entity.Departments;
import ra.edu.ptit_cntt2_it210_project.repository.DepartmentRepository;

import java.util.List;

@Service
public class DepartMentServiceImpl implements DepartmentService{
    private final DepartmentRepository departmentRepository;
    public DepartMentServiceImpl(DepartmentRepository departmentRepository){
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Departments> findAll() {
        return departmentRepository.findAll();
    }
}

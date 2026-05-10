package ra.edu.ptit_cntt2_it210_project.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ra.edu.ptit_cntt2_it210_project.model.entity.Departments;
import ra.edu.ptit_cntt2_it210_project.repository.DepartmentRepository;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Departments findByDeptId(Long deptId) {
        return departmentRepository.findByDeptId(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Khoa không tồn tại userId: " + deptId));
    }
}

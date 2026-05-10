package ra.edu.ptit_cntt2_it210_project.service;

import ra.edu.ptit_cntt2_it210_project.model.entity.Departments;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    List<Departments> findAll();
    Departments findByDeptId(Long deptId);
}

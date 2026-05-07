package ra.edu.ptit_cntt2_it210_project.service;

import ra.edu.ptit_cntt2_it210_project.model.entity.Lecturers;

import java.util.List;

public interface LecturerService {
    List<Lecturers> findByDepartment(Long deptId);
    List<Lecturers> findAllActive();
    Lecturers findByUserId(Long userId);
}

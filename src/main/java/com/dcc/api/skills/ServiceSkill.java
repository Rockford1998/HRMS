package com.dcc.api.skills;

import com.dcc.api.skills.utiliy.dto.CreateSkillDto;
import com.dcc.api.skills.utiliy.dto.UpdateSkillDto;
import com.dcc.api.skills.utiliy.SkillExportToExcelService;
import com.dcc.api.utiility.Exceptions.ResourceNotFoundException;
import com.dcc.api.skills.utiliy.mapper.SkillMapper;
import com.dcc.api.skills.utiliy.processQueryParams.SkillSpecification;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceSkill implements IServices<Skill> {
    private final RepositorySkill repositorySkill;
    private final SkillExportToExcelService skillExportToExcelService;
    private final SkillMapper skillMapper;

    public ServiceSkill(RepositorySkill repositorySkill, SkillExportToExcelService skillExportToExcelService, SkillMapper skillMapper) {
        this.repositorySkill = repositorySkill;
        this.skillExportToExcelService = skillExportToExcelService;
        this.skillMapper = skillMapper;
    }

    @Override
    public Optional<Skill> findByIdService(int id) {
        return repositorySkill.findById(id);
    }

    public List<Skill> searchSkill(String firstname, String description, String sortBy, String sortOrder, String page, String size) {
        Specification<Skill> spec = SkillSpecification.filterByAll(firstname, description);
        Sort sort;
        System.out.println("sortOrder:" + sortOrder);
        if (sortOrder.equals("asc")) {
            sort = Sort.by(Sort.Order.asc(sortBy));
        } else {
            sort = Sort.by(Sort.Order.desc(sortBy));
        }
        int pageNumber = Integer.parseInt(page) - 1;
        int pageSize = Integer.parseInt(size);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return repositorySkill.findAll(spec, pageRequest);
    }

    @Override
    public List<Skill> findAllService() {
        return repositorySkill.findAll();
    }

    @Override
    public Skill saveService(CreateSkillDto createSkillDto) {
        Skill skill = skillMapper.createSkillMapper(createSkillDto);
        return repositorySkill.save(skill);
    }

    @Override
    public boolean deleteService(int id) {
        if (repositorySkill.existsById(id)) {
            repositorySkill.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Skill updateService(UpdateSkillDto updateSkillDto, int id) {
        if (updateSkillDto == null) {
            throw new IllegalArgumentException("Skill and Skill ID must not be null");
        }
        Skill skill = skillMapper.updateSkillMapper(updateSkillDto);
        Skill skill1 = repositorySkill.findById(id).orElseThrow(() -> new ResourceNotFoundException("Skill not found with id:: " + id));
        skill1.setName(skill.getName());
        skill1.setDescription(skill.getDescription());
        return repositorySkill.save(skill1);
    }

    public void exportSkillsInExcel
            (String firstname, String description, String sortBy, String sortOrder, String page, String size, HttpServletResponse response) throws IOException {
        Specification<Skill> spec = SkillSpecification.filterByAll(firstname, description);
        Sort sort;
        System.out.println("sortOrder:" + sortOrder);
        if (sortOrder.equals("asc")) {
            sort = Sort.by(Sort.Order.asc(sortBy));
        } else {
            sort = Sort.by(Sort.Order.desc(sortBy));
        }
        int pageNumber = Integer.parseInt(page) - 1;
        int pageSize = Integer.parseInt(size);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        List<Skill> data = repositorySkill.findAll(spec, pageRequest);
        skillExportToExcelService.exportToExcel(response, data);

    }
}

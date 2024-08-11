package com.dcc.api.skills;

import com.dcc.api.skills.utiliy.dto.CreateSkillDto;
import com.dcc.api.skills.utiliy.dto.UpdateSkillDto;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IServices<T> {
    Optional<T> findByIdService(int id);

    List<T> findAllService();

    T saveService(CreateSkillDto createSkillDto);

    boolean deleteService(int t);

    T updateService(UpdateSkillDto updateSkillDto, int id);
}

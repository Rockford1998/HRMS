package com.dcc.api.skills.utiliy.mapper;

import com.dcc.api.skills.utiliy.dto.CreateSkillDto;
import com.dcc.api.skills.utiliy.dto.UpdateSkillDto;
import com.dcc.api.skills.Skill;
import org.springframework.stereotype.Service;

@Service
public class SkillMapper {
    public Skill createSkillMapper(CreateSkillDto createSkillDto) {
        Skill skill = new Skill();
        skill.setName(createSkillDto.getName());
        skill.setDescription(createSkillDto.getDescription());
        return skill;
    }

    public Skill updateSkillMapper(UpdateSkillDto updateSkillDto) {
        Skill skill = new Skill();
        skill.setName(updateSkillDto.getName());
        skill.setDescription(updateSkillDto.getDescription());
        return skill;
    }
}

package com.dcc.api.skills.utiliy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSkillDto {
    private String name;
    private String description;
}

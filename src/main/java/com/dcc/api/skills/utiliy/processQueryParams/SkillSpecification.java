package com.dcc.api.skills.utiliy.processQueryParams;

import com.dcc.api.skills.Skill;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class SkillSpecification {

    public static Specification<Skill> filterByFirstname(String name) {
        return (root, query, cb) ->
                StringUtils.hasText(name) ? cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%") : cb.conjunction();
    }

    public static Specification<Skill> filterByLastname(String description) {
        return (root, query, cb) ->
                StringUtils.hasText(description) ? cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%") : cb.conjunction();
    }

    public static Specification<Skill> filterByAll(String firstname, String lastname) {
        return Specification.where(filterByFirstname(firstname))
                .and(filterByLastname(lastname));
    }
}

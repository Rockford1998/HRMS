package com.dcc.api.skills;


import com.dcc.api.skills.utiliy.dto.CreateSkillDto;
import com.dcc.api.skills.utiliy.dto.UpdateSkillDto;
import com.dcc.api.utiility.ResponseHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//https://denitiawan.medium.com/create-rest-api-for-export-data-to-excel-and-pdf-using-springboot-38a2ee6c73a0
@RestController
@RequestMapping("/api/skill")
public class ControllerSkill {
    private final ServiceSkill skillService;
    private static final Logger logger = LoggerFactory.getLogger(ControllerSkill.class);

    //dependancy injection
    public ControllerSkill(ServiceSkill skillService) {

        this.skillService = skillService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") int id) {
        try {
            Optional<Skill> skill = skillService.findByIdService(id);
            if (skill.isPresent()) {
                return ResponseHandler.responseBuilder("Skill retrived Successfully", HttpStatus.OK, skill);

            }
            return ResponseHandler.responseBuilder("Skill not present in database.", HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public List<Skill> findFiltered(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @RequestParam(required = false, defaultValue = "1") String page,
            @RequestParam(required = false, defaultValue = "10") String size
    ) {
        return skillService.searchSkill(name, description, sortBy, sortDirection, page, size);
    }

    @GetMapping("/excel/all")
    public void exportToExcel(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String description,
                              @RequestParam(required = false, defaultValue = "id") String sortBy,
                              @RequestParam(required = false, defaultValue = "asc") String sortDirection,
                              @RequestParam(required = false, defaultValue = "1") String page,
                              @RequestParam(required = false, defaultValue = "10") String size
            , HttpServletResponse response) throws IOException {
        this.skillService.exportSkillsInExcel(name, description, sortBy, sortDirection, page, size, response);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createsSkill(@RequestBody CreateSkillDto payload) {
        try {
            Skill skill = skillService.saveService(payload);
            if (skill != null) {
                return ResponseHandler.responseBuilder("Skill created successfully", HttpStatus.CREATED, skill);
            } else {
                return ResponseHandler.responseBuilder("Skill creation failed", HttpStatus.BAD_REQUEST, null);
            }
        } catch (DataIntegrityViolationException e) {
            /*
             * first get root cause and then message
             * */
            String rootCauseMessage = e.getRootCause().getMessage();
            if (rootCauseMessage != null && rootCauseMessage.contains("skill_name_key")) {
                logger.error("Duplicate skill name: ", e);
                return ResponseHandler.responseBuilder("Skill name is already present in the database", HttpStatus.CONFLICT, null);
            } else {
                logger.error("Data integrity violation: ", e);
                return ResponseHandler.responseBuilder("Data integrity violation", HttpStatus.CONFLICT, null);
            }
        } catch (ConstraintViolationException e) {
            logger.error("Constraint violation: ", e);
            return ResponseHandler.responseBuilder("Constraint violation: " + e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (Exception e) {
            logger.error("An unexpected error occurred: ", e);
            return ResponseHandler.responseBuilder("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        List<Skill> skills = new ArrayList<>();
        return "File uploaded successfully";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> UpdateSkill(@PathVariable("id") int id, @RequestBody UpdateSkillDto payload) {
        try {
            Skill skill = skillService.updateService(payload, id);
            if (skill != null) {
                return ResponseHandler.responseBuilder("Skill updated Successfully", HttpStatus.OK, skill);
            }
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) {
            /*
             * first get root cause and then message
             * */
            String rootCauseMessage = e.getRootCause().getMessage();
            if (rootCauseMessage != null && rootCauseMessage.contains("skill_name_key")) {
                logger.error("Duplicate skill name: ", e);
                return ResponseHandler.responseBuilder("Skill name is already present in the database", HttpStatus.CONFLICT, null);
            } else {
                logger.error("Data integrity violation: ", e);
                return ResponseHandler.responseBuilder("Data integrity violation", HttpStatus.CONFLICT, null);
            }
        } catch (ConstraintViolationException e) {
            logger.error("Constraint violation: ", e);
            return ResponseHandler.responseBuilder("Constraint violation: " + e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (Exception e) {
            logger.error("An unexpected error occurred: ", e);
            return ResponseHandler.responseBuilder("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSkill(@PathVariable("id") int id) {
        try {
            boolean isdeleted = skillService.deleteService(id);
            if (isdeleted) {
                return ResponseHandler.responseBuilder("Skill deleted Successfully.", HttpStatus.OK, null);
            }
            return ResponseHandler.responseBuilder("Skill to delete not found.", HttpStatus.NOT_FOUND, null);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong.", HttpStatus.BAD_REQUEST);
        }
    }
}

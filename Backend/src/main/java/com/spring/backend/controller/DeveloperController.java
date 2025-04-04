package com.spring.backend.controller;

import com.spring.backend.entity.Developer;
import com.spring.backend.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/developer")
public class DeveloperController {
    private final DeveloperService developerService;

    @GetMapping("/getAllDeveloper")
    public ResponseEntity<List<Developer>> getAllDevelopers(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize
    ) {
        List<Developer> developers = developerService.getAllDevelopers(pageNumber, pageSize);
        return developers.isEmpty() 
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT) 
                : ResponseEntity.ok(developers);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Developer> getById(@PathVariable("id") Long id) {
        Developer developer = developerService.getById(id);
        return developer != null 
                ? ResponseEntity.ok(developer) 
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<HttpStatus> deleteDeveloperById(@PathVariable Long id) {
        return developerService.deleteDeveloperById(id) 
                ? ResponseEntity.ok().build() 
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createNewDeveloper(@RequestBody Developer developer) {
        return developerService.saveDeveloper(developer) 
                ? ResponseEntity.status(HttpStatus.CREATED).build() 
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/update")
    public ResponseEntity<HttpStatus> updateExistingDeveloper(@RequestBody Developer developer) {
        return developerService.updateDeveloper(developer) 
                ? ResponseEntity.ok().build() 
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

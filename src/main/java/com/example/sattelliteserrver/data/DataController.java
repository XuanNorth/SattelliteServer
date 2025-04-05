package com.example.sattelliteserrver.data;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController("/data")
public class DataController {

    @Autowired DataService dataService;

    @GetMapping
    public ResponseEntity<List<DataEntity>> getDataByDay(LocalDate date) {
        List<DataEntity> result = dataService.findByDay(date);
        return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(result);
    }
}

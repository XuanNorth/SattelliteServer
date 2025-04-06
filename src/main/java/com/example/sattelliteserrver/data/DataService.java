package com.example.sattelliteserrver.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class DataService {

    @Autowired DataRepository dataRepository;

    public DataEntity save(DataEntity dataEntity) {
        return dataRepository.save(dataEntity);
    }

    public List<DataEntity> findByDay(LocalDate date) {

        Instant start = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = date.atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
        System.out.println("start = " + start);
        System.out.println("end = " + end);

        return dataRepository.findByTimestampBetween(start, end);
    }

}

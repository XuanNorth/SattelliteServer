package com.example.sattelliteserrver.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface DataRepository extends JpaRepository<DataEntity, Long> {
  List<DataEntity> findByTimestampBetween(Instant timestampStart, Instant timestampEnd);

}
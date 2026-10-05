package com.nivor.health;
import java.time.LocalDate;import java.util.*;import org.springframework.data.jpa.repository.JpaRepository;
public interface HealthRecordRepository extends JpaRepository<HealthRecord,Long>{List<HealthRecord> findAllByUser_IdAndDateBetweenOrderByDateDesc(Long userId,LocalDate from,LocalDate to);Optional<HealthRecord> findByIdAndUser_Id(Long id,Long userId);Optional<HealthRecord> findByUser_IdAndDate(Long id,LocalDate date);}

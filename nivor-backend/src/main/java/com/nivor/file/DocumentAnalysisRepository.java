package com.nivor.file;import java.util.Optional;import org.springframework.data.jpa.repository.JpaRepository;
public interface DocumentAnalysisRepository extends JpaRepository<DocumentAnalysis,Long>{Optional<DocumentAnalysis> findFirstByFile_IdAndUser_IdOrderByCreatedAtDesc(Long fileId,Long userId);void deleteAllByFile_IdAndUser_Id(Long fileId,Long userId);}

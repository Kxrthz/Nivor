package com.nivor.note;
import java.util.*;import org.springframework.data.jpa.repository.EntityGraph;import org.springframework.data.jpa.repository.JpaRepository;
public interface NoteRepository extends JpaRepository<Note,Long>{@EntityGraph(attributePaths="tags")List<Note> findAllByUser_IdOrderByUpdatedAtDesc(Long userId);@EntityGraph(attributePaths="tags")Optional<Note> findByIdAndUser_Id(Long id,Long userId);}

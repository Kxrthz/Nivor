package com.nivor.note.dto;
import com.nivor.note.Note;import java.time.*;import java.util.List;
public record NoteResponse(Long id,String title,String content,String category,List<String> tags,boolean favorite,boolean pinned,LocalDateTime createdAt,LocalDateTime updatedAt){public static NoteResponse from(Note n){return new NoteResponse(n.getId(),n.getTitle(),n.getContent(),n.getCategory(),List.copyOf(n.getTags()),n.isFavorite(),n.isPinned(),n.getCreatedAt(),n.getUpdatedAt());}}

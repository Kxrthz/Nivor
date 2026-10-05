package com.nivor.note.dto;
import jakarta.validation.constraints.NotBlank;import jakarta.validation.constraints.Size;import java.util.List;
public record NoteRequest(@NotBlank@Size(max=180)String title,@Size(max=100000)String content,@Size(max=80)String category,List<@Size(max=40)String> tags,Boolean favorite,Boolean pinned){}

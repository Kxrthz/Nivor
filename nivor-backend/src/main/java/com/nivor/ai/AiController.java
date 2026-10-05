package com.nivor.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nivor.ai.AiDtos.*;
import com.nivor.file.FileService;
import com.nivor.finance.FinanceService;
import com.nivor.health.HealthRecordService;
import com.nivor.note.Note;
import com.nivor.note.NoteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final AiService ai;
    private final AiActionService actions;
    private final AiContextService context;
    private final AiMemoryService memories;
    private final NoteService notes;
    private final FinanceService finance;
    private final HealthRecordService health;
    private final FileService files;
    private final ObjectMapper mapper;

    public AiController(AiService ai, AiActionService actions, AiContextService context, AiMemoryService memories,
                        NoteService notes, FinanceService finance, HealthRecordService health, FileService files,
                        ObjectMapper mapper) {
        this.ai=ai; this.actions=actions; this.context=context; this.memories=memories;
        this.notes=notes; this.finance=finance; this.health=health; this.files=files; this.mapper=mapper;
    }

    @GetMapping("/availability") public Map<String,Object> availability(){return Map.of("available",ai.available(),"message",ai.available()?"AI is ready":"AI is not configured yet.");}
    @GetMapping("/conversations") public List<ConversationView> conversations(@AuthenticationPrincipal UserDetails u){return ai.conversations(u.getUsername());}
    @PostMapping("/conversations") public ResponseEntity<ConversationView> create(@AuthenticationPrincipal UserDetails u,@RequestBody(required=false)CreateConversation r){return ResponseEntity.status(HttpStatus.CREATED).body(ai.create(u.getUsername(),r==null?new CreateConversation(null):r));}
    @GetMapping("/conversations/{id}") public ConversationView get(@AuthenticationPrincipal UserDetails u,@PathVariable long id){return ai.get(u.getUsername(),id);}
    @DeleteMapping("/conversations/{id}") public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails u,@PathVariable long id){ai.delete(u.getUsername(),id);return ResponseEntity.noContent().build();}
    @GetMapping("/conversations/{id}/messages") public List<MessageView> messages(@AuthenticationPrincipal UserDetails u,@PathVariable long id){return ai.messages(u.getUsername(),id);}
    @PostMapping("/conversations/{id}/messages") public ChatResponse conversationMessage(@AuthenticationPrincipal UserDetails u,@PathVariable long id,@Valid@RequestBody ChatRequest r){return ai.chat(u.getUsername(),new ChatRequest(r.message(),id));}
    @PostMapping("/chat") public ChatResponse chat(@AuthenticationPrincipal UserDetails u,@Valid@RequestBody ChatRequest r){return ai.chat(u.getUsername(),r);}
    @PostMapping("/daily-briefing") public ChatResponse briefing(@AuthenticationPrincipal UserDetails u){return ai.dailyBriefing(u.getUsername());}
    public record PlanRequest(@NotBlank @Size(max=160)String subject,LocalDate deadline,@Min(1)@Max(16)int availableHours,@Size(max=1200)String preferences){}
    public record DayPlanRequest(LocalDate date,@Size(max=1200)String preferences,@Min(1)@Max(16)Integer availableHours){}
    @PostMapping("/plan-day") public ChatResponse planDay(@AuthenticationPrincipal UserDetails u,@Valid@RequestBody DayPlanRequest request){String prompt="Propose a schedule for my day. Date: "+(request.date()==null?LocalDate.now():request.date())+". Preferences: "+(request.preferences()==null?"none":request.preferences())+". Available hours: "+(request.availableHours()==null?"unspecified":request.availableHours())+". Return suggested actions only; do not state that calendar data changed.";return ai.prompt(u.getUsername(),prompt);}
    @PostMapping("/study-plan") public ChatResponse studyPlan(@AuthenticationPrincipal UserDetails u,@Valid@RequestBody PlanRequest r){return ai.prompt(u.getUsername(),"Create a study plan for "+r.subject()+" by "+r.deadline()+" using at most "+r.availableHours()+" hours per day. Preferences: "+(r.preferences()==null?"none":r.preferences())+". Return a plan only; do not create tasks or events.");}
    @PostMapping("/actions/{id}/confirm") public ActionView confirm(@AuthenticationPrincipal UserDetails u,@PathVariable long id){return actions.confirm(u.getUsername(),id);}
    @PostMapping("/actions/{id}/cancel") public ActionView cancel(@AuthenticationPrincipal UserDetails u,@PathVariable long id){return actions.cancel(u.getUsername(),id);}
    @GetMapping("/actions/{id}") public ActionView action(@AuthenticationPrincipal UserDetails u,@PathVariable long id){return actions.get(u.getUsername(),id);}
    @GetMapping("/preferences") public List<PreferenceView> preferences(@AuthenticationPrincipal UserDetails u){return context.preferences(u.getUsername());}
    @PutMapping("/preferences") public PreferenceView preference(@AuthenticationPrincipal UserDetails u,@Valid@RequestBody PreferenceRequest r){return context.update(u.getUsername(),r);}
    @GetMapping("/memories") public List<MemoryView> memories(@AuthenticationPrincipal UserDetails u){return memories.list(u.getUsername());}
    @PostMapping("/memories") public ResponseEntity<MemoryView> createMemory(@AuthenticationPrincipal UserDetails u,@Valid@RequestBody MemoryRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(memories.create(u.getUsername(),r));}
    @PutMapping("/memories/{id}") public MemoryView updateMemory(@AuthenticationPrincipal UserDetails u,@PathVariable long id,@Valid@RequestBody MemoryRequest r){return memories.update(u.getUsername(),id,r);}
    @DeleteMapping("/memories/{id}") public ResponseEntity<Void> deleteMemory(@AuthenticationPrincipal UserDetails u,@PathVariable long id){memories.delete(u.getUsername(),id);return ResponseEntity.noContent().build();}

    @PostMapping("/notes/{id}/summarize") public ChatResponse summarizeNote(@AuthenticationPrincipal UserDetails u,@PathVariable long id){return notePrompt(u.getUsername(),id,"Summarize these notes. Keep to information present in the source.");}
    @PostMapping("/notes/{id}/action-items") public ChatResponse noteActions(@AuthenticationPrincipal UserDetails u,@PathVariable long id){return notePrompt(u.getUsername(),id,"Extract concise actionable items from these notes. Do not claim to create tasks.");}
    @PostMapping("/notes/{id}/quiz") public ChatResponse noteQuiz(@AuthenticationPrincipal UserDetails u,@PathVariable long id){return notePrompt(u.getUsername(),id,"Create a short quiz from these notes and provide answers separately.");}
    private ChatResponse notePrompt(String email,long id,String instruction){require(email,AiContextCategory.NOTES);Note note=notes.get(email,id);String body=Jsoup.parse(note.getContent()==null?"":note.getContent()).text();return ai.prompt(email,instruction+"\nTitle: "+note.getTitle()+"\nContent:\n"+body.substring(0,Math.min(12000,body.length())));}

    @PostMapping("/finance/analysis") public ChatResponse financeAnalysis(@AuthenticationPrincipal UserDetails u){String email=u.getUsername();require(email,AiContextCategory.FINANCE);try{String safeSummary=mapper.writeValueAsString(finance.summary(email));return ai.prompt(email,"Give a general spending summary, category trends, and budget observations from these aggregate totals. Do not recommend transactions, payments, or financial products.\n"+safeSummary);}catch(IOException e){throw new IllegalStateException("Could not prepare finance summary.");}}
    @PostMapping("/health/summary") public ChatResponse healthSummary(@AuthenticationPrincipal UserDetails u){String email=u.getUsername();require(email,AiContextCategory.HEALTH);try{String safeSummary=mapper.writeValueAsString(health.summary(email));return ai.prompt(email,"Organize these user-entered wellness summaries into general observations only. Do not diagnose, prescribe, or claim medical certainty.\n"+safeSummary);}catch(IOException e){throw new IllegalStateException("Could not prepare wellness summary.");}}

    @PostMapping("/files/{id}/summary") public ChatResponse fileSummary(@AuthenticationPrincipal UserDetails u,@PathVariable long id)throws IOException{return filePrompt(u.getUsername(),id,"Summarize this user-provided document and identify its key topics.");}
    @PostMapping("/files/{id}/questions") public ChatResponse fileQuestions(@AuthenticationPrincipal UserDetails u,@PathVariable long id)throws IOException{return filePrompt(u.getUsername(),id,"Create concise study questions from this document and include their answers.");}
    @PostMapping("/files/{id}/study-plan") public ChatResponse fileStudyPlan(@AuthenticationPrincipal UserDetails u,@PathVariable long id)throws IOException{return filePrompt(u.getUsername(),id,"Create a short study plan based only on this document. Do not create calendar events or tasks.");}
    public record Flashcard(String question,String answer){}
    public record Flashcards(boolean available,List<Flashcard> cards,String response){}
    @PostMapping("/files/{id}/flashcards") public Flashcards fileFlashcards(@AuthenticationPrincipal UserDetails u,@PathVariable long id)throws IOException{String email=u.getUsername();require(email,AiContextCategory.FILES);String text=files.textForAi(email,id);ChatResponse result=ai.prompt(email,"Create up to 12 concise flashcards from this document. Return JSON only, with a cards array of question and answer strings.\n"+text);if(!result.available())return new Flashcards(false,List.of(),result.response());try{JsonNode root=mapper.readTree(result.response());List<Flashcard> cards=mapper.readerForListOf(Flashcard.class).readValue(root.path("cards"));return new Flashcards(true,cards,null);}catch(Exception ex){return new Flashcards(true,List.of(),result.response());}}
    private ChatResponse filePrompt(String email,long id,String instruction)throws IOException{require(email,AiContextCategory.FILES);return ai.prompt(email,instruction+" Do not claim to change the original file.\n"+files.textForAi(email,id));}
    private void require(String email,AiContextCategory category){if(!context.enabled(email,category))throw new AccessDeniedException("AI access to "+category.name().toLowerCase()+" is disabled in your privacy settings.");}
}

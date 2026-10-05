package com.nivor.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class AuthOwnershipIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @Test void rejectsProtectedRequestWithoutToken() throws Exception {
        mvc.perform(get("/api/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UP")).andExpect(jsonPath("$.database").value("UP"));
        mvc.perform(get("/api/tasks")).andExpect(status().isUnauthorized()).andExpect(jsonPath("$.error").value("UNAUTHORIZED"));
        mvc.perform(get("/api/tasks").header("Authorization", "Bearer invalid-token")).andExpect(status().isUnauthorized());
    }

    @Test void validatesAiPlanningInputBeforeCallingProvider() throws Exception {
        String token=register("plan-validation-"+System.nanoTime()+"@example.test","Plan User");
        String body="{\"preferences\":\""+"x".repeat(1201)+"\"}";
        mvc.perform(post("/api/ai/plan-day").header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test void registrationLoginAndCurrentUserDoNotExposePassword() throws Exception {
        String email="nora-"+System.nanoTime()+"@example.test";
        String body="""
                {"name":"Nora Example","email":"%s","password":"secure-pass-123"}
                """.formatted(email);
        String registered=mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isCreated()).andExpect(jsonPath("$.user.email").value(email)).andExpect(jsonPath("$.user.passwordHash").doesNotExist()).andReturn().getResponse().getContentAsString();
        JsonNode auth=mapper.readTree(registered);String token=auth.get("token").asText();
        mvc.perform(get("/api/auth/me").header("Authorization","Bearer "+token)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Nora Example"));
        String login="{\"email\":\"%s\",\"password\":\"secure-pass-123\"}".formatted(email);
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(login)).andExpect(status().isOk()).andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test void userCannotReadAnotherUsersTask() throws Exception {
        String suffix=Long.toString(System.nanoTime());String tokenA=register("a-"+suffix+"@example.test","User A");String tokenB=register("b-"+suffix+"@example.test","User B");
        String task="{\"title\":\"Private task\",\"priority\":\"HIGH\"}";
        String result=mvc.perform(post("/api/tasks").header("Authorization","Bearer "+tokenA).contentType(MediaType.APPLICATION_JSON).content(task)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long taskId=mapper.readTree(result).get("id").asLong();
        mvc.perform(get("/api/tasks/"+taskId).header("Authorization","Bearer "+tokenB)).andExpect(status().isNotFound());
        mvc.perform(put("/api/tasks/"+taskId).header("Authorization","Bearer "+tokenA).contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"Updated task\",\"priority\":\"LOW\"}")).andExpect(status().isOk());
        mvc.perform(patch("/api/tasks/"+taskId+"/complete").header("Authorization","Bearer "+tokenA)).andExpect(status().isOk());
        mvc.perform(delete("/api/tasks/"+taskId).header("Authorization","Bearer "+tokenA)).andExpect(status().isNoContent());
    }

    @Test void coreModulesSupportPersistentCrudAndTracking() throws Exception {
        String token=register("core-"+System.nanoTime()+"@example.test","Core User");
        String note="/api/notes";
        long noteId=create(note,token,"{\"title\":\"First note\",\"content\":\"<p>Text</p>\"}");
        mvc.perform(patch(note+"/"+noteId+"/favorite").header("Authorization","Bearer "+token)).andExpect(status().isOk());
        mvc.perform(patch(note+"/"+noteId+"/pin").header("Authorization","Bearer "+token)).andExpect(status().isOk());
        mvc.perform(put(note+"/"+noteId).header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"Revised note\",\"content\":\"Updated\"}")).andExpect(status().isOk());
        mvc.perform(delete(note+"/"+noteId).header("Authorization","Bearer "+token)).andExpect(status().isNoContent());

        long goalId=create("/api/goals",token,"{\"title\":\"Ship project\",\"progress\":10}");
        long milestoneId=create("/api/goals/"+goalId+"/milestones",token,"{\"title\":\"First milestone\"}");
        mvc.perform(patch("/api/goals/"+goalId+"/milestones/"+milestoneId+"/complete").header("Authorization","Bearer "+token)).andExpect(status().isOk());
        mvc.perform(put("/api/goals/"+goalId).header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"Ship revised project\",\"progress\":50}")).andExpect(status().isOk());
        mvc.perform(delete("/api/goals/"+goalId).header("Authorization","Bearer "+token)).andExpect(status().isNoContent());

        long habitId=create("/api/habits",token,"{\"name\":\"Read\",\"frequency\":\"DAILY\",\"targetCount\":1}");
        mvc.perform(post("/api/habits/"+habitId+"/logs").header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("{\"date\":\"2026-10-01\",\"completed\":true}")).andExpect(status().isCreated());
        mvc.perform(get("/api/habits/"+habitId+"/logs").header("Authorization","Bearer "+token)).andExpect(status().isOk());
        mvc.perform(put("/api/habits/"+habitId).header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Read daily\",\"frequency\":\"DAILY\",\"targetCount\":1}")).andExpect(status().isOk());
        mvc.perform(delete("/api/habits/"+habitId).header("Authorization","Bearer "+token)).andExpect(status().isNoContent());

        long journalId=create("/api/journal",token,"{\"title\":\"Today\",\"content\":\"Entry\",\"mood\":\"HAPPY\",\"entryDate\":\"2026-10-01\"}");
        mvc.perform(put("/api/journal/"+journalId).header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"Updated\",\"content\":\"Entry updated\",\"mood\":\"NEUTRAL\",\"entryDate\":\"2026-10-01\"}")).andExpect(status().isOk());
        mvc.perform(delete("/api/journal/"+journalId).header("Authorization","Bearer "+token)).andExpect(status().isNoContent());

        long workspaceId=create("/api/workspaces",token,"{\"name\":\"Personal\"}");
        long projectId=create("/api/projects",token,"{\"name\":\"Launch\",\"workspaceId\":"+workspaceId+",\"progress\":5}");
        mvc.perform(put("/api/projects/"+projectId).header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Launch revised\",\"workspaceId\":"+workspaceId+",\"progress\":60}")).andExpect(status().isOk());
        mvc.perform(delete("/api/projects/"+projectId).header("Authorization","Bearer "+token)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/workspaces/"+workspaceId).header("Authorization","Bearer "+token)).andExpect(status().isNoContent());

        long eventId=create("/api/calendar/events",token,"{\"title\":\"Review\",\"startTime\":\"2026-10-02T09:00:00\",\"endTime\":\"2026-10-02T10:00:00\"}");
        mvc.perform(put("/api/calendar/events/"+eventId).header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"Review updated\",\"startTime\":\"2026-10-02T09:00:00\",\"endTime\":\"2026-10-02T10:00:00\"}")).andExpect(status().isOk());
        mvc.perform(delete("/api/calendar/events/"+eventId).header("Authorization","Bearer "+token)).andExpect(status().isNoContent());
    }

    @Test void financeHealthFocusAndAiRespectOwnershipAndPersist() throws Exception {
        String suffix=Long.toString(System.nanoTime());String a=register("advanced-a-"+suffix+"@example.test","Advanced A");String b=register("advanced-b-"+suffix+"@example.test","Advanced B");
        String income="""
                {"type":"INCOME","amount":40000.00,"category":"Salary","description":"Monthly pay","transactionDate":"2026-10-01"}
                """;
        mvc.perform(post("/api/finance/transactions").header("Authorization","Bearer "+a).contentType(MediaType.APPLICATION_JSON).content(income)).andExpect(status().isCreated()).andExpect(jsonPath("$.amount").value(40000.00));
        mvc.perform(post("/api/finance/transactions").header("Authorization","Bearer "+a).contentType(MediaType.APPLICATION_JSON).content("""
                {"type":"EXPENSE","amount":15000.00,"category":"Bills","description":"Utilities","transactionDate":"2026-10-01"}
                """)).andExpect(status().isCreated());
        mvc.perform(get("/api/finance/summary").header("Authorization","Bearer "+a)).andExpect(status().isOk()).andExpect(jsonPath("$.income").value(40000.00)).andExpect(jsonPath("$.expenses").value(15000.00)).andExpect(jsonPath("$.netSavings").value(25000.00));
        mvc.perform(get("/api/finance/transactions").header("Authorization","Bearer "+b)).andExpect(status().isOk()).andExpect(jsonPath("$",org.hamcrest.Matchers.hasSize(0)));

        String record="""
                {"date":"2026-10-01","weight":68.25,"waterMl":1500,"sleepMinutes":420,"steps":8000,"exerciseMinutes":30,"notes":"Walked today"}
                """;
        String health=mvc.perform(post("/api/health/records").header("Authorization","Bearer "+a).contentType(MediaType.APPLICATION_JSON).content(record)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();long healthId=mapper.readTree(health).get("id").asLong();
        mvc.perform(get("/api/health/records/"+healthId).header("Authorization","Bearer "+b)).andExpect(status().isNotFound());
        mvc.perform(get("/api/health/summary").header("Authorization","Bearer "+a)).andExpect(status().isOk()).andExpect(jsonPath("$.latestWeight").value(68.25)).andExpect(jsonPath("$.averageWaterMl").value(1500.0));

        String session=mvc.perform(post("/api/focus/sessions").header("Authorization","Bearer "+a).contentType(MediaType.APPLICATION_JSON).content("{\"durationSeconds\":1500}")).andExpect(status().isCreated()).andExpect(jsonPath("$.status").value("IN_PROGRESS")).andReturn().getResponse().getContentAsString();long sessionId=mapper.readTree(session).get("id").asLong();
        mvc.perform(patch("/api/focus/sessions/"+sessionId+"/pause").header("Authorization","Bearer "+a)).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("PAUSED"));
        mvc.perform(patch("/api/focus/sessions/"+sessionId+"/resume").header("Authorization","Bearer "+a)).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("IN_PROGRESS"));
        mvc.perform(patch("/api/focus/sessions/"+sessionId+"/complete").header("Authorization","Bearer "+a)).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("COMPLETED"));
        mvc.perform(patch("/api/focus/sessions/"+sessionId+"/complete").header("Authorization","Bearer "+b)).andExpect(status().isNotFound());
        mvc.perform(get("/api/ai/availability").header("Authorization","Bearer "+a)).andExpect(status().isOk()).andExpect(jsonPath("$.available").value(false)).andExpect(jsonPath("$.message").value("AI is not configured yet."));
        mvc.perform(get("/api/search").param("q","").header("Authorization","Bearer "+a)).andExpect(status().isOk());
    }

    @Test void fileStorageIsOwnedAndAnalysisReportsProviderUnavailable() throws Exception {
        String suffix=Long.toString(System.nanoTime());String a=register("file-a-"+suffix+"@example.test","File A");String b=register("file-b-"+suffix+"@example.test","File B");
        MockMultipartFile unsupported=new MockMultipartFile("file","script.html","text/html","<script>alert(1)</script>".getBytes(java.nio.charset.StandardCharsets.UTF_8));
        mvc.perform(multipart("/api/files").file(unsupported).header("Authorization","Bearer "+a)).andExpect(status().isBadRequest());
        MockMultipartFile upload=new MockMultipartFile("file","private.txt","text/plain","A private document.".getBytes(java.nio.charset.StandardCharsets.UTF_8));
        String response=mvc.perform(multipart("/api/files").file(upload).header("Authorization","Bearer "+a)).andExpect(status().isCreated()).andExpect(jsonPath("$.originalName").value("private.txt")).andReturn().getResponse().getContentAsString();long fileId=mapper.readTree(response).get("id").asLong();
        mvc.perform(get("/api/files/"+fileId+"/download").header("Authorization","Bearer "+a)).andExpect(status().isOk()).andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().string("A private document."));
        mvc.perform(get("/api/files/"+fileId).header("Authorization","Bearer "+b)).andExpect(status().isNotFound());
        mvc.perform(post("/api/files/"+fileId+"/analyze").header("Authorization","Bearer "+a)).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("FAILED")).andExpect(jsonPath("$.summary").value("Document analysis is unavailable: configure an AI provider first."));
        mvc.perform(delete("/api/files/"+fileId).header("Authorization","Bearer "+a)).andExpect(status().isNoContent());
    }

    private long create(String path,String token,String json)throws Exception{
        String response=mvc.perform(post(path).header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return mapper.readTree(response).get("id").asLong();
    }
    private String register(String email,String name)throws Exception{
        String json="{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"secure-pass-123\"}".formatted(name,email);
        return mapper.readTree(mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(json)).andReturn().getResponse().getContentAsString()).get("token").asText();
    }
}

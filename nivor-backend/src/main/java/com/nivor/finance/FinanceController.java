package com.nivor.finance;
import com.nivor.finance.FinanceDtos.*;import jakarta.validation.Valid;import java.util.List;import org.springframework.http.*;import org.springframework.security.core.annotation.AuthenticationPrincipal;import org.springframework.security.core.userdetails.UserDetails;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/finance") public class FinanceController{
 private final FinanceService service;public FinanceController(FinanceService s){service=s;}
 @GetMapping("/transactions")public List<TransactionView> transactions(@AuthenticationPrincipal UserDetails u){return service.transactions(u.getUsername());}
 @PostMapping("/transactions")public ResponseEntity<TransactionView> createTransaction(@AuthenticationPrincipal UserDetails u,@Valid@RequestBody TransactionRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.createTransaction(u.getUsername(),r));}
 @PutMapping("/transactions/{id}")public TransactionView updateTransaction(@AuthenticationPrincipal UserDetails u,@PathVariable long id,@Valid@RequestBody TransactionRequest r){return service.updateTransaction(u.getUsername(),id,r);}
 @DeleteMapping("/transactions/{id}")public ResponseEntity<Void> deleteTransaction(@AuthenticationPrincipal UserDetails u,@PathVariable long id){service.deleteTransaction(u.getUsername(),id);return ResponseEntity.noContent().build();}
 @GetMapping("/budgets")public List<BudgetView> budgets(@AuthenticationPrincipal UserDetails u){return service.budgets(u.getUsername());}
 @PostMapping("/budgets")public ResponseEntity<BudgetView> createBudget(@AuthenticationPrincipal UserDetails u,@Valid@RequestBody BudgetRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.createBudget(u.getUsername(),r));}
 @PutMapping("/budgets/{id}")public BudgetView updateBudget(@AuthenticationPrincipal UserDetails u,@PathVariable long id,@Valid@RequestBody BudgetRequest r){return service.updateBudget(u.getUsername(),id,r);}
 @DeleteMapping("/budgets/{id}")public ResponseEntity<Void> deleteBudget(@AuthenticationPrincipal UserDetails u,@PathVariable long id){service.deleteBudget(u.getUsername(),id);return ResponseEntity.noContent().build();}
 @GetMapping("/savings-goals")public List<SavingsView> savings(@AuthenticationPrincipal UserDetails u){return service.savings(u.getUsername());}
 @PostMapping("/savings-goals")public ResponseEntity<SavingsView> createSavings(@AuthenticationPrincipal UserDetails u,@Valid@RequestBody SavingsRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.createSavings(u.getUsername(),r));}
 @PutMapping("/savings-goals/{id}")public SavingsView updateSavings(@AuthenticationPrincipal UserDetails u,@PathVariable long id,@Valid@RequestBody SavingsRequest r){return service.updateSavings(u.getUsername(),id,r);}
 @DeleteMapping("/savings-goals/{id}")public ResponseEntity<Void> deleteSavings(@AuthenticationPrincipal UserDetails u,@PathVariable long id){service.deleteSavings(u.getUsername(),id);return ResponseEntity.noContent().build();}
 @GetMapping("/summary")public Summary summary(@AuthenticationPrincipal UserDetails u){return service.summary(u.getUsername());}
}

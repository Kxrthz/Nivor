package com.nivor.finance;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class FinanceDtos {
 private FinanceDtos(){}
 public record TransactionRequest(@NotNull TransactionType type,@NotNull @DecimalMin(value="0.01") @Digits(integer=15,fraction=4) BigDecimal amount,@NotBlank @Size(max=80) String category,@Size(max=500) String description,@NotNull LocalDate transactionDate){}
 public record TransactionView(Long id,TransactionType type,BigDecimal amount,String category,String description,LocalDate transactionDate,LocalDateTime createdAt){static TransactionView from(Transaction x){return new TransactionView(x.getId(),x.getType(),x.getAmount(),x.getCategory(),x.getDescription(),x.getTransactionDate(),x.getCreatedAt());}}
 public record BudgetRequest(@NotBlank @Size(max=80) String category,@NotNull @DecimalMin("0.00") @Digits(integer=15,fraction=4) BigDecimal amount,@NotNull BudgetPeriod period,@NotNull LocalDate startDate,@NotNull LocalDate endDate){}
 public record BudgetView(Long id,String category,BigDecimal amount,BudgetPeriod period,LocalDate startDate,LocalDate endDate){static BudgetView from(Budget x){return new BudgetView(x.getId(),x.getCategory(),x.getAmount(),x.getPeriod(),x.getStartDate(),x.getEndDate());}}
 public record SavingsRequest(@NotBlank @Size(max=120) String name,@NotNull @DecimalMin("0.01") @Digits(integer=15,fraction=4) BigDecimal targetAmount,@NotNull @DecimalMin("0.00") @Digits(integer=15,fraction=4) BigDecimal currentAmount,LocalDate deadline){}
 public record SavingsView(Long id,String name,BigDecimal targetAmount,BigDecimal currentAmount,LocalDate deadline){static SavingsView from(SavingsGoal x){return new SavingsView(x.getId(),x.getName(),x.getTargetAmount(),x.getCurrentAmount(),x.getDeadline());}}
 public record CategoryTotal(String category,BigDecimal amount){}
 public record BudgetUsage(Long id,String category,BigDecimal budgeted,BigDecimal spent,BigDecimal remaining){}
 public record Summary(BigDecimal income,BigDecimal expenses,BigDecimal netSavings,java.util.List<CategoryTotal> categories,java.util.List<BudgetUsage> budgetUsage){}
}

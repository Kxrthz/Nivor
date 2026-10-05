package com.nivor.finance;
import java.time.LocalDate;import java.util.*;import org.springframework.data.jpa.repository.*;
interface TransactionRepository extends JpaRepository<Transaction,Long>{List<Transaction> findAllByUser_IdOrderByTransactionDateDesc(Long userId);Optional<Transaction> findByIdAndUser_Id(Long id,Long userId);}
interface BudgetRepository extends JpaRepository<Budget,Long>{List<Budget> findAllByUser_IdOrderByStartDateDesc(Long id);Optional<Budget> findByIdAndUser_Id(Long id,Long userId);}
interface SavingsGoalRepository extends JpaRepository<SavingsGoal,Long>{List<SavingsGoal> findAllByUser_IdOrderByDeadlineAsc(Long id);Optional<SavingsGoal> findByIdAndUser_Id(Long id,Long userId);}

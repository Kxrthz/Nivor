package com.nivor.finance;
import com.nivor.common.entity.AuditedEntity;import com.nivor.user.User;import jakarta.persistence.*;import java.math.BigDecimal;import java.time.LocalDate;
@Entity @Table(name="budgets",indexes=@Index(name="idx_budget_user",columnList="user_id"))
public class Budget extends AuditedEntity{
 @ManyToOne(fetch=FetchType.LAZY,optional=false)@JoinColumn(name="user_id",nullable=false)private User user;
 @Column(nullable=false,length=80)private String category;@Column(nullable=false,precision=19,scale=4)private BigDecimal amount;
 @Enumerated(EnumType.STRING)@Column(nullable=false,length=12)private BudgetPeriod period;@Column(nullable=false)private LocalDate startDate;@Column(nullable=false)private LocalDate endDate;
 protected Budget(){}public User getUser(){return user;}public void setUser(User v){user=v;}public String getCategory(){return category;}public void setCategory(String v){category=v;}public BigDecimal getAmount(){return amount;}public void setAmount(BigDecimal v){amount=v;}public BudgetPeriod getPeriod(){return period;}public void setPeriod(BudgetPeriod v){period=v;}public LocalDate getStartDate(){return startDate;}public void setStartDate(LocalDate v){startDate=v;}public LocalDate getEndDate(){return endDate;}public void setEndDate(LocalDate v){endDate=v;}
}

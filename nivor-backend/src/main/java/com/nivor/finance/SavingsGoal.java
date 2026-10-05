package com.nivor.finance;
import com.nivor.common.entity.AuditedEntity;import com.nivor.user.User;import jakarta.persistence.*;import java.math.BigDecimal;import java.time.LocalDate;
@Entity @Table(name="savings_goals",indexes=@Index(name="idx_savings_user",columnList="user_id"))
public class SavingsGoal extends AuditedEntity{
 @ManyToOne(fetch=FetchType.LAZY,optional=false)@JoinColumn(name="user_id",nullable=false)private User user;
 @Column(nullable=false,length=120)private String name;@Column(nullable=false,precision=19,scale=4)private BigDecimal targetAmount;@Column(nullable=false,precision=19,scale=4)private BigDecimal currentAmount=BigDecimal.ZERO;private LocalDate deadline;
 protected SavingsGoal(){}public User getUser(){return user;}public void setUser(User v){user=v;}public String getName(){return name;}public void setName(String v){name=v;}public BigDecimal getTargetAmount(){return targetAmount;}public void setTargetAmount(BigDecimal v){targetAmount=v;}public BigDecimal getCurrentAmount(){return currentAmount;}public void setCurrentAmount(BigDecimal v){currentAmount=v;}public LocalDate getDeadline(){return deadline;}public void setDeadline(LocalDate v){deadline=v;}
}

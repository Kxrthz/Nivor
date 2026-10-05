package com.nivor.finance;

import com.nivor.common.entity.AuditedEntity;
import com.nivor.user.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name="finance_transactions", indexes=@Index(name="idx_transaction_user_date", columnList="user_id,transaction_date"))
public class Transaction extends AuditedEntity {
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="user_id", nullable=false) private User user;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=12) private TransactionType type;
    @Column(nullable=false, precision=19, scale=4) private BigDecimal amount;
    @Column(nullable=false, length=80) private String category;
    @Column(length=500) private String description;
    @Column(name="transaction_date", nullable=false) private LocalDate transactionDate;
    protected Transaction() {}
    public User getUser(){return user;} public void setUser(User v){user=v;}
    public TransactionType getType(){return type;} public void setType(TransactionType v){type=v;}
    public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public LocalDate getTransactionDate(){return transactionDate;} public void setTransactionDate(LocalDate v){transactionDate=v;}
}

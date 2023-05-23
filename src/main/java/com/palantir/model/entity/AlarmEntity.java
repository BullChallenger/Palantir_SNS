package com.palantir.model.entity;

import com.palantir.model.AlarmArgs;
import com.palantir.model.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "alarm", indexes = {
        @Index(name = "account_id_idx", columnList = "account_id")
})
@Getter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@NoArgsConstructor
@SQLDelete(sql = "UPDATE alarm SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at IS NULL")
public class AlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

<<<<<<< HEAD
    @ManyToOne(fetch = FetchType.LAZY)
=======
    @ManyToOne
>>>>>>> main
    @JoinColumn(name = "account_id")
    private AccountEntity receiver;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Type(type = "jsonb")
    @Column(columnDefinition = "json")
    private AlarmArgs args;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Builder
    public AlarmEntity(AccountEntity receiver, AlarmType alarmType, AlarmArgs args) {
        this.receiver = receiver;
        this.alarmType = alarmType;
        this.args = args;
    }

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static AlarmEntity of(AccountEntity receiver, AlarmType alarmType, AlarmArgs args) {
        AlarmEntity theAlarmEntity = AlarmEntity.builder()
                                                .receiver(receiver)
                                                .alarmType(alarmType)
                                                .args(args)
                                             .build();
        return theAlarmEntity;
    }
}

package com.dream.pet_tinder.model.message;

import com.dream.pet_tinder.model.profile.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    private Profile firstProfile;
    @OneToOne(fetch = FetchType.EAGER)
    private Profile secondProfile;
    @OneToOne(fetch = FetchType.EAGER)
    private Profile sender;
    private String text;
    private long time;
    private Boolean isRead;
}

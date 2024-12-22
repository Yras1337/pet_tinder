package com.dream.pet_tinder.model.characteristics;

import com.dream.pet_tinder.model.profile.Profile;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "characteristics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Characteristics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private Characteristic characteristicName;
    private String value;
    @OneToOne(fetch = FetchType.EAGER)
    private Profile profile;
}

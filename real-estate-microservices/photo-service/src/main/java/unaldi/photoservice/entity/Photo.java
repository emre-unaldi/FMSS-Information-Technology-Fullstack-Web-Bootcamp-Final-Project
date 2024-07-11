package unaldi.photoservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 11.07.2024
 */
@Entity
@Table(name = "photos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Size(min = 1, max = 255)
    @Column(name = "photo_name", nullable = false, unique = true)
    private String name;

    @Column(name = "photo_type", nullable = false)
    private String type;

    @Lob
    @Column(name = "source_data", nullable = false)
    private byte[] sourceData;

    public Photo(String name, String type, byte[] sourceData) {
        this.name = name;
        this.type = type;
        this.sourceData = sourceData;
    }

}

package com.bakaibank.booking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "positions")
@Data
@NoArgsConstructor
@ToString
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY)
    private List<Employee> employees = new ArrayList<>();

    public Position(String name) {
        this.name = name;
    }
}

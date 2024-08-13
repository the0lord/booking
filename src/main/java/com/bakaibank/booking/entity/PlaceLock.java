package com.bakaibank.booking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Блокировка места для бронирования. Если запись существует,
 * то место place будет закрыто для бронирования с lockStartDate по lockEndDate
 */
@Entity
@Table(name = "place_locks")
@Data
@NoArgsConstructor
@ToString
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "PlaceLock.withPlaceAndFullEmployeeInfo",
                attributeNodes = {
                        @NamedAttributeNode("place"),
                        @NamedAttributeNode(value = "assignedEmployee", subgraph = "assignedEmployee-position-team")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "assignedEmployee-position-team",
                                attributeNodes = {
                                        @NamedAttributeNode("position"),
                                        @NamedAttributeNode("team")
                                }
                        )
                }
        )
})
public class PlaceLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "lock_start_date", nullable = false)
    private LocalDate lockStartDate;

    @Column(name = "lock_end_date", nullable = false)
    private LocalDate lockEndDate;

    /**
     * Причина, по которой место закрыто. Опционально
     */
    @Column(name = "reason")
    private String reason;

    /**
     * За каким сотрудником закреплено место (если оно закрыто по причине
     * того, что оно закреплено за сотрудником). Опционально
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_employee_id")
    private Employee assignedEmployee;

    public PlaceLock(Place place, LocalDate lockStartDate, LocalDate lockEndDate, String reason, Employee assignedEmployee) {
        this.place = place;
        this.lockStartDate = lockStartDate;
        this.lockEndDate = lockEndDate;
        this.reason = reason;
        this.assignedEmployee = assignedEmployee;
    }
}

package com.bakaibank.booking.specification;

import com.bakaibank.booking.dto.booking.places.PlaceBookingsStatsRequest;
import com.bakaibank.booking.entity.Booking;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PlaceBookingSpecification implements Specification<Booking> {
    private final PlaceBookingsStatsRequest criteria;

    public PlaceBookingSpecification(PlaceBookingsStatsRequest criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Booking> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getEmployeeId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("employee").get("id"), criteria.getEmployeeId()));
        }

        if (criteria.getFrom() != null && criteria.getTo() != null)
            predicates.add(criteriaBuilder.between(root.get("bookingDate"), criteria.getFrom(), criteria.getTo()));

        if (criteria.getFrom() != null)
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("bookingDate"), criteria.getFrom()));

        if (criteria.getTo() != null)
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("bookingDate"), criteria.getTo()));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}

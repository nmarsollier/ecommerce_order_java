package com.order.projections.orderStatus;

import com.order.projections.common.Status;
import com.order.projections.orderStatus.schema.OrderStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusRepository extends CrudRepository<OrderStatus, String> {

    @Query("{ userId : ?0 }}")
    List<OrderStatus> findByUserId(String userId, Sort sort);

    @Query("{ status : ?0 }")
    List<OrderStatus> findByStatus(Status status, Sort sort);
}
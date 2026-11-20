package com.example.shop_app.common.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private final OutboxRepository outboxRepository;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents(){
        List<OutboxEvent> pendingEvents = outboxRepository.findByStatusOrderByCreatedAtAsc(
                OutboxStatus.PENDING,
                PageRequest.of(0,50)
        );

        if(pendingEvents.isEmpty()){
            return;
        }

        log.info("Found {} pending outbox events. Publishing...", pendingEvents.size());

        for(OutboxEvent event : pendingEvents){
            try {
                log.info(">>> [DISPATCHING EVENT] Type: {} | AggregateId: {} | Payload: {}",
                        event.getEventType(), event.getAggregateId(), event.getPayload());

                event.setStatus(OutboxStatus.PROCESSED);
                event.setProcessedAt(Instant.now());
                outboxRepository.save(event);
            } catch (Exception e) {
                log.error("Failed to publish outbox event: {}", event.getId(), e);
                event.setStatus(OutboxStatus.FAILED);
                outboxRepository.save(event);
            }
        }

    }
}

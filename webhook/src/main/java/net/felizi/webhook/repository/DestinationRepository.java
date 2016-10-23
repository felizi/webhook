package net.felizi.webhook.repository;

import org.springframework.data.repository.CrudRepository;

import net.felizi.webhook.domain.Destination;

public interface DestinationRepository extends CrudRepository<Destination, Long> {
}
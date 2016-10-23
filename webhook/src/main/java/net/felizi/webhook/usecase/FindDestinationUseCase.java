package net.felizi.webhook.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.felizi.webhook.domain.Destination;
import net.felizi.webhook.repository.DestinationRepository;

@Component
public class FindDestinationUseCase {
	@Autowired
	private DestinationRepository destinationRepository;

	public Iterable<Destination> execute() {
		return destinationRepository.findAll();
	}

	public Destination execute(Long id) {
		return destinationRepository.findOne(id);
	}
}
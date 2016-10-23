package net.felizi.webhook.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.felizi.webhook.domain.Destination;
import net.felizi.webhook.repository.DestinationRepository;

@Component
public class NewDestinationUseCase {
	@Autowired
	private DestinationRepository destinationRepository;

	public Destination execute(String identifier, String url) {
		return destinationRepository.save(new Destination(identifier, url));
	}
}
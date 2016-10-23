package net.felizi.webhook.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.felizi.webhook.repository.DestinationRepository;

@Component
public class DeleteDestinationUseCase {
	@Autowired
	private DestinationRepository destinationRepository;

	public void execute(long id) {
		destinationRepository.delete(id);
	}
}
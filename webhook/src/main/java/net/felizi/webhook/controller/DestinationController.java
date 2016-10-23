package net.felizi.webhook.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.Map;

import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Iterables;

import net.felizi.webhook.controller.converter.ResourcesConverter;
import net.felizi.webhook.converter.MessageConverter;
import net.felizi.webhook.domain.Destination;
import net.felizi.webhook.domain.Message;
import net.felizi.webhook.exception.MultipleException;
import net.felizi.webhook.usecase.DeleteDestinationUseCase;
import net.felizi.webhook.usecase.FindDestinationUseCase;
import net.felizi.webhook.usecase.NewDestinationUseCase;
import net.felizi.webhook.usecase.SendMessageToDestinationUseCase;

@RestController
@RequestMapping("/destinations")
public class DestinationController {
	private final ResourcesConverter<Destination> resourcesConverter;
	private final MessageConverter messageConverter;
	private final NewDestinationUseCase newDestinationUseCase;
	private final FindDestinationUseCase findDestinationUseCase;
	private final DeleteDestinationUseCase deleteDestinationUseCase;
	private final SendMessageToDestinationUseCase sendMessageToDestinationUseCase;

	@Autowired
	public DestinationController(ResourcesConverter<Destination> resourcesConverter, MessageConverter messageConverter, NewDestinationUseCase newDestinationUseCase,
			FindDestinationUseCase findDestinationUseCase, DeleteDestinationUseCase deleteDestinationUseCase, SendMessageToDestinationUseCase sendMessageToDestinationUseCase) {
		super();
		this.resourcesConverter = resourcesConverter;
		this.messageConverter = messageConverter;
		this.newDestinationUseCase = newDestinationUseCase;
		this.findDestinationUseCase = findDestinationUseCase;
		this.deleteDestinationUseCase = deleteDestinationUseCase;
		this.sendMessageToDestinationUseCase = sendMessageToDestinationUseCase;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Resource<?>> detail(@PathVariable Long id) {
		Destination destination;
		try {
			destination = findDestinationUseCase.execute(id);
			if (destination == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Resource<?> resource = new Resource<>(destination);
		resource.add(linkTo(methodOn(DestinationController.class).detail(id)).withSelfRel());
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Resource<?>> newDestination(@RequestParam String identifier, @RequestParam String url) {
		Destination destination = newDestinationUseCase.execute(identifier, url);
		Resource<?> resource = new Resource<>(destination);
		resource.add(linkTo(methodOn(DestinationController.class).newDestination(identifier, url)).withSelfRel());
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Resources<?>> find() {
		Iterable<Destination> destinations = findDestinationUseCase.execute();
		if (Iterables.isEmpty(destinations)) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		List<Resource<Destination>> resourcesList = resourcesConverter.execute(destinations);
		Resources<?> resources = new Resources<>(resourcesList);
		return new ResponseEntity<>(resources, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Resource<?>> delete(@PathVariable Long id) {
		try {
			deleteDestinationUseCase.execute(id);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/send-message", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Resource<?>> send(@PathVariable Long id, @RequestBody Map<String, Object> body) {
		try {
			Message message = messageConverter.execute(id, body);
			sendMessageToDestinationUseCase.execute(message);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (MultipleException e) {
			return new ResponseEntity<>(new Resource<>(e.toMap()), HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (AmqpException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

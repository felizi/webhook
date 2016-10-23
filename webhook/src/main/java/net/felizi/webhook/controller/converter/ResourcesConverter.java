package net.felizi.webhook.controller.converter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

@Component
public class ResourcesConverter<BaseEntity> {
	public List<Resource<BaseEntity>> execute(Iterable<BaseEntity> entityList) {
		return StreamSupport.stream(entityList.spliterator(), false).map(e -> new Resource<BaseEntity>(e)).collect(Collectors.toList());
	}
}

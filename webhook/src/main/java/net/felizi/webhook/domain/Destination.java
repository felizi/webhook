package net.felizi.webhook.domain;

import javax.persistence.Entity;

@Entity
public class Destination extends BaseEntity<Long> {
	private String identifier;
	private String url;

	public Destination() {
		super();
	}

	public Destination(String identifier, String url) {
		super();
		this.identifier = identifier;
		this.url = url;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
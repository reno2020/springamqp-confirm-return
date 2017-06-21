package org.throwable.entity;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/6/22 1:05
 */
public class MessageTarget {

	private String id;
	private String body;

	public MessageTarget() {
	}

	public MessageTarget(String id, String body) {
		this.id = id;
		this.body = body;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}

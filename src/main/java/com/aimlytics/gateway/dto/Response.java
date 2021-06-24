package com.aimlytics.gateway.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response<T> {

	@Builder.Default
	private boolean success = true;
	@Singular
	private List<String> messages;
	private T body;
	@Singular
	private List<String> errorCodes;
	@Singular
	private List<String> messageCodes;

}
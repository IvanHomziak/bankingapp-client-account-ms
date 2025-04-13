package com.ihomziak.clientaccountms.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import com.ihomziak.clientaccountms.dto.ClientRequestDTO;

public class SanitizerUtils {

	public static String sanitize(String input) {
		if (input == null || input.isBlank()) return "";
		return Jsoup.clean(input.trim(), Safelist.none());
	}

	public static ClientRequestDTO sanitize(ClientRequestDTO dto) {
		if (dto == null) return null;

		ClientRequestDTO sanitized = new ClientRequestDTO();
		sanitized.setFirstName(sanitize(dto.getFirstName()));
		sanitized.setLastName(sanitize(dto.getLastName()));
		sanitized.setEmail(sanitize(dto.getEmail()).toLowerCase());
		sanitized.setTaxNumber(sanitize(dto.getTaxNumber()).toLowerCase());
		sanitized.setPhoneNumber(sanitize(dto.getPhoneNumber()));
		sanitized.setAddress(sanitize(dto.getAddress()));
		sanitized.setDateOfBirth(sanitize(dto.getDateOfBirth()));

		return sanitized;
	}
}

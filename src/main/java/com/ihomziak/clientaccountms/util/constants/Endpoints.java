package com.ihomziak.clientaccountms.util.constants;

public class Endpoints {

	public static final String API_VERSION_V1 = "/api/v1";

	public static class AccountEndpoints {
		public static final String API_ACCOUNT_V1 = API_VERSION_V1 + "/account";
		public static final String GET_ACCOUNT = "/{uuid}";
		public static final String GET_CLIENT_ACCOUNTS = "/list/{uuid}";
		public static final String DELETE_ACCOUNT = "/{uuid}";
	}

	public static class ClientEndpoints {
		public static final String API_CLIENT_V1 = API_VERSION_V1 + "/clients";
		public static final String GET_CLIENT = "/{uuid}";
		public static final String SEARCH_CLIENT = "/search";
		public static final String ADD_CLIENT = "/add-client";
		public static final String UPDATE_CLIENT = "/update";
		public static final String DELETE_CLIENT = "/{uuid}";
		public static final String COUNT_CLIENTS_BY_LAST_NAME = "/count";
	}

	public static class SwaggerEndpoints {
		public static final String SWAGGER_UI = "/swagger-ui/**";
		public static final String SWAGGER_UI_HTML = "/swagger-ui.html";
		public static final String SWAGGER_UI_INDEX = "/swagger-ui/index.html";
		public static final String SWAGGER_API_DOCS_V2 = "/v2/api-docs";
		public static final String SWAGGER_API_DOCS_V3 =  "/v3/api-docs/**";
		public static final String SWAGGER_RESOURCES = "/swagger-resources/**";
		public static final String SWAGGER_WEBJARS = "/webjars/**";
	}
}
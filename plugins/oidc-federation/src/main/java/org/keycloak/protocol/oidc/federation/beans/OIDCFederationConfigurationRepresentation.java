package org.keycloak.protocol.oidc.federation.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keycloak.protocol.oidc.representations.OIDCConfigurationRepresentation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OIDCFederationConfigurationRepresentation extends OIDCConfigurationRepresentation {

	
    @JsonProperty("federation_registration_endpoint")
    private String federationRegistrationEndpoint;
    
    @JsonProperty("pushed_authorization_request_endpoint")
    private String pushedAuthorizationRequestEndpoint;
    
    @JsonProperty("client_registration_types_supported")
    private List<String> clientRegistrationTypesSupported;
    
    @JsonProperty("client_registration_authn_methods_supported")
    private Map<String,List<String>> clientRegistrationAuthnMethodsSupported;

    
    
	public String getFederationRegistrationEndpoint() {
		return federationRegistrationEndpoint;
	}

	public void setFederationRegistrationEndpoint(String federationRegistrationEndpoint) {
		this.federationRegistrationEndpoint = federationRegistrationEndpoint;
	}

	public String getPushedAuthorizationRequestEndpoint() {
		return pushedAuthorizationRequestEndpoint;
	}

	public void setPushedAuthorizationRequestEndpoint(String pushedAuthorizationRequestEndpoint) {
		this.pushedAuthorizationRequestEndpoint = pushedAuthorizationRequestEndpoint;
	}

	public List<String> getClientRegistrationTypesSupported() {
		return clientRegistrationTypesSupported;
	}

	public void setClientRegistrationTypesSupported(List<String> clientRegistrationTypesSupported) {
		this.clientRegistrationTypesSupported = clientRegistrationTypesSupported;
	}

	public Map<String, List<String>> getClientRegistrationAuthnMethodsSupported() {
		return clientRegistrationAuthnMethodsSupported;
	}

	public void setClientRegistrationAuthnMethodsSupported(
			Map<String, List<String>> clientRegistrationAuthnMethodsSupported) {
		this.clientRegistrationAuthnMethodsSupported = clientRegistrationAuthnMethodsSupported;
	}
	
}

package org.keycloak.protocol.oidc.federation.op;
/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import org.keycloak.OAuth2Constants;
import org.keycloak.authentication.ClientAuthenticator;
import org.keycloak.authentication.ClientAuthenticatorFactory;
import org.keycloak.common.util.Time;
import org.keycloak.crypto.CekManagementProvider;
import org.keycloak.crypto.ClientSignatureVerifierProvider;
import org.keycloak.crypto.ContentEncryptionProvider;
import org.keycloak.crypto.KeyType;
import org.keycloak.crypto.KeyUse;
import org.keycloak.crypto.KeyWrapper;
import org.keycloak.crypto.SignatureProvider;
import org.keycloak.jose.jwk.JSONWebKeySet;
import org.keycloak.jose.jwk.JWK;
import org.keycloak.jose.jwk.JWKBuilder;
import org.keycloak.jose.jws.Algorithm;
import org.keycloak.models.ClientScopeModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.protocol.oidc.OIDCLoginProtocol;
import org.keycloak.protocol.oidc.OIDCLoginProtocolService;
import org.keycloak.protocol.oidc.OIDCWellKnownProvider;
import org.keycloak.protocol.oidc.endpoints.TokenEndpoint;
import org.keycloak.protocol.oidc.federation.beans.EntityStatement;
import org.keycloak.protocol.oidc.federation.beans.OIDCFederationConfigurationRepresentation;
import org.keycloak.protocol.oidc.federation.exceptions.InternalServerErrorException;
import org.keycloak.protocol.oidc.representations.OIDCConfigurationRepresentation;
import org.keycloak.protocol.oidc.utils.OIDCResponseType;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.representations.IDToken;
import org.keycloak.services.Urls;
import org.keycloak.services.clientregistration.ClientRegistrationService;
import org.keycloak.services.clientregistration.oidc.OIDCClientRegistrationProviderFactory;
import org.keycloak.services.resources.RealmsResource;
import org.keycloak.urls.UrlType;
import org.keycloak.wellknown.WellKnownProvider;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class OIDCFederationWellKnownProvider extends OIDCWellKnownProvider implements WellKnownProvider {

	public static final ObjectMapper om = new ObjectMapper();
	public static final Long ENTITY_EXPIRES_AFTER_SEC = 86400L; //24 hours
	
    private KeycloakSession session;

    public OIDCFederationWellKnownProvider(KeycloakSession session) {
    	super(session);
        this.session = session;
    }

    @Override
    public Object getConfig() {
    	    	
        UriInfo frontendUriInfo = session.getContext().getUri(UrlType.FRONTEND);
        UriInfo backendUriInfo = session.getContext().getUri(UrlType.BACKEND);

        RealmModel realm = session.getContext().getRealm();

    	OIDCFederationConfigurationRepresentation config;
		try {
			config = from(((OIDCConfigurationRepresentation) super.getConfig()));
		} catch (JsonProcessingException e) {
			throw new InternalServerErrorException("Could not form the configuration response");
		} 

        //additional federation-specific configuration
//        config.setFederationRegistrationEndpoint(federationRegistrationEndpoint);
//        config.setPushedAuthorizationRequestEndpoint(pushedAuthorizationRequestEndpoint);
//        config.setClientRegistrationTypesSupported(clientRegistrationTypesSupported);
//        config.setClientRegistrationAuthnMethodsSupported(clientRegistrationAuthnMethodsSupported);
        
        
        EntityStatement entityStatement = new EntityStatement();
        entityStatement.issuedFor(Urls.realmIssuer(frontendUriInfo.getBaseUri(), realm.getName()));
        entityStatement.setMetadata(config);
//        entityStatement.setAuthorityHints(authorityHints);
        entityStatement.setJwks(getKeySet());
        entityStatement.issuer(Urls.realmIssuer(frontendUriInfo.getBaseUri(), realm.getName()));
        entityStatement.issuedNow();
        entityStatement.exp(Long.valueOf(Time.currentTime()) + ENTITY_EXPIRES_AFTER_SEC);
        
        
        
        //sign entity statement
        
        
        return entityStatement;
    }

    @Override
    public void close() {
    }

    
    private JSONWebKeySet getKeySet() {
    	List<JWK> keys = new LinkedList<>();
        for (KeyWrapper k : session.keys().getKeys(session.getContext().getRealm())) {
            if (k.getStatus().isEnabled() && k.getUse().equals(KeyUse.SIG) && k.getPublicKey() != null) {
                JWKBuilder b = JWKBuilder.create().kid(k.getKid()).algorithm(k.getAlgorithm());
                if (k.getType().equals(KeyType.RSA)) {
                    keys.add(b.rsa(k.getPublicKey(), k.getCertificate()));
                } else if (k.getType().equals(KeyType.EC)) {
                    keys.add(b.ec(k.getPublicKey()));
                }
            }
        }

        JSONWebKeySet keySet = new JSONWebKeySet();

        JWK[] k = new JWK[keys.size()];
        k = keys.toArray(k);
        keySet.setKeys(k);
        return keySet;
    }
    
	public static OIDCFederationConfigurationRepresentation from(OIDCConfigurationRepresentation representation) throws JsonMappingException, JsonProcessingException {
		return om.readValue(om.writeValueAsString(representation), OIDCFederationConfigurationRepresentation.class);
	}
    
	
}

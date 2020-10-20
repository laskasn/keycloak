package org.keycloak.protocol.oidc.federation.rest.op;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.keycloak.models.KeycloakSession;

public class FederationOPService {

	private KeycloakSession session;

    public FederationOPService(KeycloakSession session) {
        this.session = session;
    }
    
    
    /**
     * This endpoint (for explicit registration) should accept an Entity Statement, wrapped up in the payload of a signed jwt  
     * @return
     */
    @POST
    @Path("fedreg")
    @Consumes("application/jose")
    public String federationRegistration(String jwt) {
    	
    	return "";
    }
	
    
    /**
     * To be used by automatic registration method, for pushed authorization requests
     * @return
     */
    @POST
    @Path("par")
    @Produces("text/plain; charset=utf-8")
    public String pushedAuthorization() {
        String name = session.getContext().getRealm().getDisplayName();
        if (name == null) {
            name = session.getContext().getRealm().getName();
        }
        return "Hello " + name;
    }
    
    
    
}

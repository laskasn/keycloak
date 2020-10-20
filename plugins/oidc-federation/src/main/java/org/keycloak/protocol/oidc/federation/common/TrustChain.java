package org.keycloak.protocol.oidc.federation.common;

import java.util.List;
import java.util.Map;

import org.keycloak.protocol.oidc.federation.beans.EntityStatement;


public class TrustChain {

	String trustAnchor;
	
	List<EntityStatement> chain;
	
	public void computeTrustChain(EntityStatement leaf) {
		
		
		//set the computed result to the this.chain variable
	}
	
	
	/**
	 * This function returns the entity statement where the discovery url points to
	 * @param discoveryUrl
	 * @return
	 */
	public static EntityStatement getEntityStatement(String discoveryUrl) {
	
		return null;
	}
	
	
	/**
	 * Get the <authorityHint,EntityStatement> pairs of the current entity as its authorityHints describe it
	 * @param es
	 * @return
	 */
	public static Map<String,EntityStatement> fetchRequestSubordinate(EntityStatement es) {
		
		return null;
	}
	
	
	
}

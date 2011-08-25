package org.saga.factions;

import java.util.ArrayList;

import org.saga.SagaPlayer;
import org.saga.chunkGroups.ChunkGroup;
import org.saga.chunkGroups.SagaChunk;
import org.saga.config.BalanceConfiguration;

public class Settlement extends ChunkGroup{

	public Settlement(String name) {
		super(name);
	}

	
	// Claiming:
	/**
	 * Gets settlement level.
	 * 
	 */
	public Short getLevel() {
		
		return 0;
		
	}
	
	/**
	 * Gets owned chunks count.
	 * 
	 * @return owned chunks
	 */
	public Short getTotalClaimed() {
		return new Integer(getGroupChunks().size()).shortValue();
	}
	
	/**
	 * Gets the claims the faction has in total.
	 * 
	 * @return claims in total.
	 */
	public Short getTotalClaims() {
		return BalanceConfiguration.getConfig().calculateSettlementClaims(getLevel()).shortValue();
	}
	
	/**
	 * Gets remaining claims.
	 * 
	 * @return remaining claims
	 */
	public Short getRemainingClaims() {
		return ( new Integer(getTotalClaims() - getTotalClaimed()) ).shortValue();
	}
	
	// Permissions:
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.chunkGroups.ChunkGroup#canClaim(org.saga.SagaPlayer)
	 */
	public boolean canClaim(SagaPlayer sagaPlayer) {
		return true;
	}

	@Override
	public boolean canAbandon(SagaPlayer sagaPlayer) {
		return true;
	}
	
	@Override
	public boolean canDelete(SagaPlayer sagaPlayer) {
		return true;
	}
	
	@Override
	public boolean canBuild(SagaPlayer sagaPlayer) {
		return isPlayerRegistered(sagaPlayer);
	}
	
	
}

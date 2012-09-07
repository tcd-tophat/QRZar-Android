package org.tophat.qrzar.sdkinterface;

import org.tophat.QRzar.mapper.KillMapper;
import org.tophat.QRzar.mapper.PlayerMapper;
import org.tophat.QRzar.models.Kill;
import org.tophat.QRzar.models.Player;
import org.tophat.android.exceptions.HttpException;
import org.tophat.android.exceptions.NotFound;
import org.tophat.android.mapping.ApiToken;
import org.tophat.android.mapping.Game;
import org.tophat.android.model.ApiTokenMapper;
import org.tophat.android.networking.ApiCommunicator;

public class SDKInterface 
{

	private ApiCommunicator apic;
	
	public SDKInterface()
	{
		apic = new ApiCommunicator(new Constants());
	}
	
	/**
	 * Test 1
	 * @return
	 */
	protected void anonymous_connect() throws HttpException
	{		
		ApiTokenMapper atm = new ApiTokenMapper(apic);
		apic.setApitoken(atm.getAnonymousToken());
	}
	
	/**
	 * Test 2
	 */
	private Player joinGame(String qrCode, Integer gameId) throws HttpException
	{
		  Game g = new Game();
		  
		  g.setId(gameId);
		  
		  Player p = new Player();
		  p.setGame(g);
		  p.setQrcode(qrCode);
		  
		  PlayerMapper pm = new PlayerMapper(apic);

		  return (Player)pm.create(p);
	}
	
	private void kill(Player killer, String victimCode) throws HttpException
	{
		  Kill k = new Kill();
		  k.setKiller(killer);
		  k.setVictimQrcode(victimCode);
		  
		  KillMapper km = new KillMapper(apic);

		  km.create(k); 
	}
	
	/**
	 * Test 4
	 * @throws HttpException 
	 */
	private void respawn(Player me) throws HttpException
	{	  
		PlayerMapper pm = new PlayerMapper(apic);
		
		me.setRespawn_code("RESPAW1");
		me.setAccessUrl("players");
		pm.update(me);
	}
	
}

package hydradfs.service;

import net.tomp2p.connection.Bindings;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class PeerService {

	private final static Integer port = 4000;
	public final static Random RND = new Random(42L);
	
	//create logger for this class
	private static Logger logger = LoggerFactory.getLogger(PeerService.class);

	public static PeerDHT createSimpleDHTPeer(Bindings bindings) throws IOException {
		
		Number160 ID = new Number160(RND);
		PeerDHT peerDHT = new PeerBuilderDHT(new PeerBuilder(ID).ports(port).bindings(bindings).start()).start();
		
		logger.info("created peerDHT and added peer ID: " + ID);
		
		return peerDHT;
	}

	public static void discoverNetwork(final Peer peer) throws UnknownHostException {
		FutureDiscover futureDiscover = peer.discover().inetAddress(InetAddress.getLocalHost()).ports(port).start();

		futureDiscover.addListener(new BaseFutureAdapter<FutureDiscover>() {
            @Override
			public void operationComplete(FutureDiscover futureDiscover) throws Exception {
				if (futureDiscover.isSuccess()) {
					logger.info("Discover success: found that my outside address is " + futureDiscover.peerAddress());

					FutureBootstrap futureBootstrap = peer.bootstrap().inetAddress(InetAddress.getLocalHost()).ports(port).start();
					futureBootstrap.addListener(new FutureBootstrapAdapter(peer));
				} else {
					logger.error("failed " + futureDiscover.failedReason());
				}
			}
		});
	}
}

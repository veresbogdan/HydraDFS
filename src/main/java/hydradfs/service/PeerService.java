package hydradfs.service;

import hydradfs.utils.MyData;
import net.tomp2p.connection.Bindings;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class PeerService {

	private final static Integer port = 4000;
	public final static Random RND = new Random();
	
	private static Logger logger = LoggerFactory.getLogger(PeerService.class);

	public static PeerDHT createSimpleDHTPeer(Bindings bindings) throws IOException {
		Number160 ID = new Number160(RND);
		PeerDHT peerDHT = new PeerBuilderDHT(new PeerBuilder(ID).ports(port).bindings(bindings).start()).start();
		
		logger.info("created peerDHT and added peer ID: " + ID);
		
		return peerDHT;
	}

	public static void discoverNetwork(final Peer peer, String ip) throws UnknownHostException {
		FutureDiscover futureDiscover = peer.discover().inetAddress(InetAddress.getByName(ip)).ports(port).start();

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

	private void putDataIntoDHT(PeerDHT peerDHT, String key, String domain, String content, String data) throws IOException {
		Number160 locationKey = Number160.createHash(key);
		Number160 domainKey = Number160.createHash(domain);
		Number160 contentKey = Number160.createHash(content);
		MyData<String> myData = new MyData<String>().key(key).domain(domain).content(content).data(data);

		FuturePut futurePut = peerDHT.put(locationKey).domainKey(domainKey).data(contentKey, new Data(myData)).start();
        futurePut.addListener(new FuturePutAdapter(peerDHT.peer(), myData));
	}

    private void getDataFromDHT(PeerDHT peerDHT, String key, String domain, String content) {
        Number160 locationKey = Number160.createHash(key);
        Number160 domainKey = Number160.createHash(domain);
        Number160 contentKey = Number160.createHash(content);

        FutureGet futureGet = peerDHT.get(locationKey).domainKey(domainKey).contentKey(contentKey).start();
        futureGet.addListener(new FutureGetAdapter(peerDHT.peer()));
    }
}

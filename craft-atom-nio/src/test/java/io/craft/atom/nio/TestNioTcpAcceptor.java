package io.craft.atom.nio;

import io.craft.atom.io.IoAcceptor;
import io.craft.atom.nio.api.NioFactory;
import io.craft.atom.test.AvailablePortFinder;
import io.craft.atom.test.CaseCounter;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mindwind
 * @version 1.0, 2011-12-20
 */
public class TestNioTcpAcceptor {
	
	
	private static final Logger LOG  = LoggerFactory.getLogger(TestNioTcpAcceptor.class);
	private static final int    PORT = AvailablePortFinder.getNextAvailable(33333)      ;
	
	
	private IoAcceptor acceptor;
	
	
	@Before
	public void before() throws IOException {
		acceptor = NioFactory.newTcpAcceptor(new NioAcceptorHandler());
		acceptor.bind(PORT);
	}
	
	@After
	public void after() {
		acceptor.shutdown();
	}
	
	@Test
    public void testDuplicateBind() throws IOException {
		Assert.assertEquals(1, acceptor.getBoundAddresses().size());
		
		try {
			acceptor.bind(PORT);
			Assert.fail();
		} catch(IOException e) {
			LOG.debug("[CRAFT-ATOM-NIO] Duplicate bind throw exception", e);
		}
		System.out.println(String.format("[CRAFT-ATOM-NIO] (^_^)  <%s>  Case -> test nio tcp acceptor duplicate bind. ", CaseCounter.incr(1)));
	}
	
	@Test
    public void testDuplicateUnbind() throws IOException {
        // this should succeed
        acceptor.unbind(PORT);

        // this shouldn't fail
        acceptor.unbind(PORT);
        Assert.assertEquals(0, acceptor.getBoundAddresses().size());
        System.out.println(String.format("[CRAFT-ATOM-NIO] (^_^)  <%s>  Case -> test nio tcp acceptor duplicate unbind. ", CaseCounter.incr(1)));
    }
    
	@Test
    public void testBindAndUnbindManyTimes() throws IOException {
        for (int i = 0; i < 16; i++) {
            acceptor.unbind(PORT);
            Assert.assertEquals(0, acceptor.getBoundAddresses().size());
            acceptor.bind(PORT);
            LOG.debug("[CRAFT-ATOM-NIO] Bind and unbind time {} ", i);
        }
        Assert.assertEquals(1, acceptor.getBoundAddresses().size());
        System.out.println(String.format("[CRAFT-ATOM-NIO] (^_^)  <%s>  Case -> test nio tcp acceptor bind & unbind many times. ", CaseCounter.incr(1)));
    }
	
}

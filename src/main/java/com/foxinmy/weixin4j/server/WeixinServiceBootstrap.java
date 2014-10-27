package com.foxinmy.weixin4j.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.util.ResourceBundle;

/**
 * 微信服务启动程序
 * 
 * @className WeixinBootstrap
 * @author jy
 * @date 2014年10月12日
 * @since JDK 1.7
 * @see
 */
public final class WeixinServiceBootstrap {

	private final static int port;
	private final static int workerThreads;
	static {
		ResourceBundle config = ResourceBundle.getBundle("netty");
		port = Integer.parseInt(config.getString("port"));
		workerThreads = Integer.parseInt(config.getString("workerThreads"));
	}

	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(workerThreads);
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.option(ChannelOption.SO_BACKLOG, 1024);
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler())
					.childHandler(new WeixinServerInitializer());
			Channel ch = b.bind(port).sync().channel();
			System.err.println("weixin server startup OK:" + port);
			ch.closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}

package hasoffer.webcommon.listener;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import hasoffer.akka.factory.AkkaActorRef;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.TimeZone;

public class ApplicationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        ActorRef defaultActorRef = AkkaActorRef.getDefaultActorRef();
        defaultActorRef.tell("Hello, AKKA Init.", ActorRef.noSender());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Web App destroyed!");
        ActorRef defaultActorRef = AkkaActorRef.getDefaultActorRef();
        defaultActorRef.tell(PoisonPill.getInstance(), ActorRef.noSender());
        ActorSystem defaultActorSystem = AkkaActorRef.getDefaultActorSystem();
        defaultActorSystem.shutdown();
    }
}


package org.carlspring.strongbox.servers.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;

/**
 * @author mtodorov
 */
public class JettyLauncher
{

    // TODO: Get this from the strongbox.xml
    private int port = 48080;

    private String basedir;

    private String contextPath = "/";

    private String war;

    private Server server;


    public JettyLauncher()
    {
    }

    public JettyLauncher(String basedir)
    {
        this.basedir = basedir;
    }

    public Server createExplodedServerInstance()
    {
        Server server = new Server(getPort());

        WebAppContext context = new WebAppContext();
        context.setDescriptor(context + "/WEB-INF/web.xml");
        context.setResourceBase(getBasedir());
        context.setContextPath(getContextPath());
        context.setParentLoaderPriority(true);

        final File dir = new File("temp");
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();

        context.setTempDirectory(dir);

        server.setHandler(context);

        return server;
    }

    public Server createWarServerInstance()
    {
        Server server = new Server(getPort());

        WebAppContext context = new WebAppContext();
        context.setDescriptor(context + "/WEB-INF/web.xml");
        context.setWar(getWar());
        context.setContextPath(getContextPath());
        context.setParentLoaderPriority(true);

        final File dir = new File("target/jetty/tmp");
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();

        context.setTempDirectory(dir);
        context.setExtractWAR(true);

        server.setHandler(context);

        return server;
    }

    public void startExplodedInstance()
            throws Exception
    {
        System.out.println("Starting Jetty with exploded webapp...");

        server = createExplodedServerInstance();
        server.start();
        server.join();
    }

    public void startWarInstance()
            throws Exception
    {
        System.out.println("Starting Jetty with WAR (" + new File(getWar()).getCanonicalFile() + ")...");

        server = createWarServerInstance();
        server.start();
        server.join();
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getBasedir()
    {
        return basedir;
    }

    public void setBasedir(String basedir)
    {
        this.basedir = basedir;
    }

    public String getContextPath()
    {
        return contextPath;
    }

    public void setContextPath(String contextPath)
    {
        this.contextPath = contextPath;
    }

    public String getWar()
    {
        return war;
    }

    public void setWar(String war)
    {
        this.war = war;
    }

    public Server getServer()
    {
        return server;
    }

    public void setServer(Server server)
    {
        this.server = server;
    }

    public void stopServer()
            throws Exception
    {
        getServer().stop();
        while (!getServer().isStopped())
        {
            Thread.sleep(500);
        }
    }

    public static void main(String[] args)
            throws Exception
    {
        final JettyLauncher launcher = new JettyLauncher();
        launcher.setBasedir("webapp");
        launcher.startExplodedInstance();
    }

}

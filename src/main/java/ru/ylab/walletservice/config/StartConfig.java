package ru.ylab.walletservice.config;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import ru.ylab.walletservice.utils.db.PropertiesPool;

import java.io.InputStream;
import java.net.URL;
/**
 * This class contains config to start web application
 */
public class StartConfig extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        try {

            URL resource = config.getServletContext().getResource("WEB-INF/classes/" +
                                                                   "database.properties");
            InputStream propertiesInputStream = resource.openStream();
            PropertiesPool.getInstance().setInputStream(propertiesInputStream);

//            MigrationLiquibase.dbMigration();

        } catch (Exception e) {
            System.out.println("Error " + e);
        }
        super.init(config);
    }
}


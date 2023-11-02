package ru.ylab.walletservice.controller.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import lombok.Cleanup;
import ru.ylab.walletservice.utils.annotation.Loggable;
import ru.ylab.walletservice.utils.db.PropertiesPool;
import ru.ylab.walletservice.utils.liquibase.MigrationLiquibase;

import java.io.InputStream;
import java.net.URL;
/**
 * This servlet contains config to start web application
 */
public class StartServlet extends HttpServlet {
    @Loggable
    @Override
    public void init(ServletConfig config) throws ServletException {
        try {

            URL resource = config.getServletContext().getResource("WEB-INF/classes/" +
                                                                   "database.properties");
            @Cleanup InputStream propertiesInputStream = resource.openStream();
            PropertiesPool.getInstance().setInputStream(propertiesInputStream);

            MigrationLiquibase.dbMigration();

        } catch (Exception e) {
            System.out.println("Error " + e);
        }
        super.init(config);
    }
}


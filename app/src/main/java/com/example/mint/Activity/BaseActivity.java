package com.example.mint.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseActivity extends AppCompatActivity {
    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Establece la conexión a PostgreSQL
        new InitializeDatabaseTask().execute();

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public class InitializeDatabaseTask extends AsyncTask<Void, Void, Connection> {
        @Override
        protected Connection doInBackground(Void... voids) {
            try {
                // Establece la conexión a PostgreSQL
                Class.forName("org.postgresql.Driver");
                return DriverManager.getConnection("jdbc:postgresql://localhost:5432/mintDB",
                        "admin", "admin");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Connection conn) {
            connection = conn;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cierra la conexión a PostgreSQL cuando la actividad es destruida
        new CloseDatabaseTask().execute(connection);
    }

    private static class CloseDatabaseTask extends AsyncTask<Connection, Void, Void> {
        @Override
        protected Void doInBackground(Connection... connections) {
            try {
                if (connections[0] != null && !connections[0].isClosed()) {
                    connections[0].close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

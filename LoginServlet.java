import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        PrintWriter out = response.getWriter();

        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/user_m", "postgres", "Shareef@ 786");

            String sql = "SELECT password FROM users WHERE username = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                // For now, directly comparing plain-text passwords (skip password hashing step)
                if (password.equals(storedPassword)) {
                    // Create a session for the user
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username); // Store username in session

                    out.println("<html><body>");
                    out.println("<h2>Login Successful!</h2>");
                    out.println("<p>Welcome, " + username + "!</p>");
                    out.println("</body></html>");
                } else {
                    out.println("<html><body>");
                    out.println("<h2>Login Failed!</h2>");
                    out.println("<p>Invalid username or password.</p>");
                    out.println("</body></html>");
                }
            } else {
                out.println("<html><body>");
                out.println("<h2>Login Failed!</h2>");
                out.println("<p>User does not exist.</p>");
                out.println("</body></html>");
            }

            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database Error");
        }
    }
}

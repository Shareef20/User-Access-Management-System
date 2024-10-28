import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class ProfileServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get the session
        HttpSession session = request.getSession(false); // Get existing session, do not create a new one

        if (session != null && session.getAttribute("username") != null) {
            String username = (String) session.getAttribute("username");

            out.println("<html><body>");
            out.println("<h2>Profile Page</h2>");
            out.println("<p>Welcome, " + username + "!</p>");
            out.println("</body></html>");
        } else {
            // No session found, user is not logged in
            out.println("<html><body>");
            out.println("<h2>Access Denied</h2>");
            out.println("<p>You must be logged in to view this page.</p>");
            out.println("</body></html>");
        }
    }
}

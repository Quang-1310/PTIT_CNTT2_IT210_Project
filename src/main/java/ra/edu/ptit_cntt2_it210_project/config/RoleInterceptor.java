package ra.edu.ptit_cntt2_it210_project.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object roleObj = session.getAttribute("role");
        String uri = request.getRequestURI();

        if (roleObj == null) {
            response.sendRedirect("/auth/login");
            return false;
        }

        String role = roleObj.toString();

        if (uri.startsWith("/admin") && !role.equals("ADMIN")) {
            response.sendRedirect("/auth/login");
            return false;
        }

        if (uri.startsWith("/lecturer") && !role.equals("LECTURER")) {
            response.sendRedirect("/auth/login");
            return false;
        }

        if (uri.startsWith("/student") && !role.equals("STUDENT")) {
            response.sendRedirect("/auth/login");
            return false;
        }

        return true;
    }
}

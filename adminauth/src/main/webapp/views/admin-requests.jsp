<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.example.demo.AccessRequest" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Requests</title>
</head>
<body>
    <h2>Pending Access Requests</h2>

    <table border="1">
        <tr>
            <th>Username</th>
            <th>Action</th>
        </tr>

        <%
            List<AccessRequest> requests = (List<AccessRequest>) request.getAttribute("requests");
            if (requests != null && !requests.isEmpty()) {
                for (AccessRequest req : requests) {
        %>
            <tr>
                <td><%= req.getUsername() %></td>
                <td>
                    <form action="/admin/approve-request" method="post" style="display:inline;">
                        <input type="hidden" name="requestId" value="<%= req.getId() %>">
                        <button type="submit">Approve</button>
                    </form>

                    <form action="/admin/reject-request" method="post" style="display:inline;">
                        <input type="hidden" name="requestId" value="<%= req.getId() %>">
                        <button type="submit" style="background-color: red; color: white;">Reject</button>
                    </form>
                </td>
            </tr>
        <%
                }
            } else {
        %>
            <tr>
                <td colspan="2" style="text-align:center;">No pending requests</td>
            </tr>
        <%
            }
        %>
    </table>
</body>
</html>

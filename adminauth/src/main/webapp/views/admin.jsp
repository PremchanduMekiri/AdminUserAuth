<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.example.demo.Entity.userCreationRequest" %>
<%@ page import="com.example.demo.Entity.AccessRequest" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #F4F7F8;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            background: #FFFFFF;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
            width: 90%;
            max-width: 800px;
            text-align: center;
        }
        h2 {
            margin-bottom: 20px;
            color: #333333;
        }
        .message {
            margin-top: 10px;
            padding: 10px;
            border-radius: 5px;
            font-size: 14px;
            text-align: center;
            width: 100%;
        }
        .success {
            background-color: #D4EDDA;
            color: #155724;
            border: 1px solid #C3E6CB;
        }
        .error {
            background-color: #FAD2D2;
            color: #721C24;
            border: 1px solid #F5C6CB;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background: #fff;
            border-radius: 8px;
            overflow: hidden;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: center;
        }
        th {
            background-color: #4A90E2;
            color: white;
        }
        .btn {
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            transition: 0.3s ease;
        }
        .approve {
            background-color: #27AE60;
            color: white;
        }
        .reject {
            background-color: #D0021B;
            color: white;
        }
        .approve:hover {
            background-color: #219150;
        }
        .reject:hover {
            background-color: #A0001A;
        }
        .link-btn {
            display: inline-block;
            margin-top: 15px;
            padding: 10px 15px;
            background-color: #4A90E2;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: 0.3s;
        }
        .link-btn:hover {
            background-color: #357ABD;
        }
    </style>
</head>
<body>

    <div class="container">
        <h2>Admin Dashboard</h2>

        <%-- Display Success or Error Messages --%>
        <% if (request.getAttribute("success") != null) { %>
            <div class="message success"><%= request.getAttribute("success") %></div>
        <% } %>

        <% if (request.getAttribute("error") != null) { %>
            <div class="message error"><%= request.getAttribute("error") %></div>
        <% } %>
<h3>Pending Access Requests</h3>
<table>
    <tr>
        <th>Username</th>
        <th>Action</th>
    </tr>
    <%
        List<AccessRequest> accessRequests = (List<AccessRequest>) request.getAttribute("accessRequests");
        if (accessRequests != null && !accessRequests.isEmpty()) {
            for (AccessRequest req : accessRequests) {
    %>
        <tr>
            <td><%= req.getUsername() %></td>
            <td>
                <form action="/admin/approveAccess/<%= req.getId() %>" method="post" style="display:inline;">
                    <button type="submit" class="btn approve">Approve</button>
                </form>

                <form action="/admin/rejectAccess" method="post" style="display:inline;">
                    <input type="hidden" name="username" value="<%= req.getUsername() %>">
                    <button type="submit" class="btn reject">Reject</button>
                </form>
            </td>
        </tr>
    <%
            }
        } else {
    %>
        <tr>
            <td colspan="2">No pending access requests</td>
        </tr>
    <%
        }
    %>
</table>


        <a href="/admin/users" class="link-btn">View All Users</a>
        <a href="/logout" style="padding: 10px 20px; background-color: #e74c3c; color: white; text-decoration: none; border-radius: 5px; display: inline-block;">
    Logout
</a>
        
    </div>

</body>
</html>




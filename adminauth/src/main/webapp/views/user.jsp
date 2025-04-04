<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Objects" %>
<html>
<head>
    <title>User Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7f8;
            text-align: center;
            margin: 0;
            padding: 20px;
        }
        .container {
            background: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
            width: 50%;
            margin: auto;
        }
        h2 {
            color: #333;
        }
        .success {
            color: green;
            font-weight: bold;
        }
        .error {
            color: red;
            font-weight: bold;
        }
        .message {
            font-weight: bold;
            margin-bottom: 10px;
        }
        .btn {
            padding: 10px 15px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            transition: 0.3s ease;
            text-decoration: none;
            display: inline-block;
            margin-top: 10px;
        }
        .request-btn {
            background-color: #4A90E2;
            color: white;
        }
        .view-users {
            background-color: #27AE60;
            color: white;
        }
    </style>
</head>
<body>

    <div class="container">
        <h2>Welcome, <%= Objects.toString(session.getAttribute("username"), "Guest") %>!</h2>

        <!-- Display Flash Messages -->
        <% if (request.getAttribute("message") != null) { %>
            <p class="success message"><%= request.getAttribute("message") %></p>
        <% } %>

        <% if (request.getAttribute("error") != null) { %>
            <p class="error message"><%= request.getAttribute("error") %></p>
        <% } %>

        <!-- Request Access Form -->
        <h3>Request Access to View Users</h3>
        <form action="/access/request" method="post">
            <input type="hidden" name="username" value="<%= session.getAttribute("username") %>">
            <button type="submit" class="btn request-btn">Request Access</button>
        </form>

        <!-- Check if Access is Approved -->
        <h3>Access Status</h3>
        <%
            Boolean isApproved = (Boolean) request.getAttribute("isApproved");
            if (isApproved != null && isApproved) {
        %>
            <p class="success">Your request is approved! You can now view the users.</p>
            <form action="/access/view-users" method="get">
                <input type="hidden" name="username" value="<%= session.getAttribute("username") %>">
                <button type="submit" class="btn view-users">View Users</button>
            </form>
        <%
            } else {
        %>
            <p class="error">Your request is still pending or has been rejected.</p>
        <%
            }
        %>
    </div>
    <a href="/logout" style="padding: 10px 20px; background-color: #e74c3c; color: white; text-decoration: none; border-radius: 5px; display: inline-block;">
    Logout
</
    

</body>
</html>


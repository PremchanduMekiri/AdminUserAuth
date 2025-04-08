<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.demo.Entity.User" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All Users - Admin View</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f2f5;
            padding: 40px;
            text-align: center;
        }

        .container {
            background: #ffffff;
            padding: 30px;
            border-radius: 10px;
            width: 85%;
            margin: auto;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        h2 {
            color: #333;
            margin-bottom: 25px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 14px 18px;
            border: 1px solid #e0e0e0;
        }

        th {
            background-color: #007BFF;
            color: #fff;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        .back-btn {
            margin-top: 25px;
            padding: 10px 20px;
            background-color: #007BFF;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            display: inline-block;
            transition: background-color 0.3s ease;
        }

        .back-btn:hover {
            background-color: #0056b3;
        }

        .info-msg {
            color: red;
            font-weight: bold;
            margin-top: 20px;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>All Registered Users</h2>

    <%
        List<User> users = (List<User>) request.getAttribute("users");
        if (users != null && !users.isEmpty()) {
    %>
        <table>
            <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Role</th>
            </tr>
            <% for (User user : users) { %>
                <tr>
                    <td><%= user.getId() %></td>
                    <td><%= user.getUsername() %></td>
                    <td><%= user.getRole() %></td>
                </tr>
            <% } %>
        </table>
    <% } else { %>
        <p>No users found in the system.</p>
    <% } %>

    <%
        Boolean accessByToken = (Boolean) session.getAttribute("accessByToken");
        if (accessByToken == null || !accessByToken) {
    %>
        <a href="/admin" class="back-btn">⬅ Back to Dashboard</a>
    <%
        } else {
    %>
        <p class="info-msg">You are viewing via token-based access. Dashboard link is disabled.</p>
    <%
        }
    %>
</div>

</body>
</html>




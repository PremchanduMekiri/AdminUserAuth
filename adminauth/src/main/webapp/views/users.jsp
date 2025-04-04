<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.demo.Entity.User" %>

<html>
<head>
    <title>All Users</title>
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
            width: 70%;
            margin: auto;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #4A90E2;
            color: white;
        }
        .error {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>

    <div class="container">
        <h2>All Users</h2>

        <!-- Display Flash Messages -->
        <% if (request.getAttribute("error") != null) { %>
            <p class="error"><%= request.getAttribute("error") %></p>
        <% } %>

        <!-- ✅ Retrieve the users list correctly -->
        <% List<User> users = (List<User>) request.getAttribute("users"); %>

        <% if (users != null && !users.isEmpty()) { %>
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
            <p class="error">No users found.</p>
        <% } %>
    </div>
    <a href="/admin/dashboard" style="padding: 10px 20px; background-color: #4A90E2; color: white; text-decoration: none; border-radius: 5px; display: inline-block;">
    Go to  Dashboard
</a>

</body>
</html>



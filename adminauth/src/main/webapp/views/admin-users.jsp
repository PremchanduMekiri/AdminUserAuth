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
            font-family: Arial, sans-serif;
            background-color: #f4f7f8;
            padding: 30px;
            text-align: center;
        }

        .container {
            background: #ffffff;
            padding: 25px;
            border-radius: 8px;
            margin: auto;
            width: 80%;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
        }

        h2 {
            color: #333;
            margin-bottom: 20px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 12px 15px;
            border: 1px solid #ddd;
        }

        th {
            background-color: #4A90E2;
            color: white;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        .back-btn {
            margin-top: 20px;
            padding: 10px 15px;
            background-color: #4A90E2;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            display: inline-block;
        }

        .back-btn:hover {
            background-color: #357ABD;
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

    <a href="/admin" class="back-btn">⬅ Back to Dashboard</a>
</div>

</body>
</html>



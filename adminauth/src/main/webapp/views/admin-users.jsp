<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.example.demo.Admin" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All Users</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            text-align: center;
        }

        h2 {
            color: #333;
        }

        table {
            width: 60%;
            margin: 20px auto;
            border-collapse: collapse;
            background: white;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        th, td {
            padding: 12px;
            border: 1px solid #ddd;
            text-align: center;
        }

        th {
            background-color: #007bff;
            color: white;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        tr:hover {
            background-color: #f1f1f1;
        }

        .back-btn {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 15px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }

        .back-btn:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

    <h2>Registered Users</h2>

    <table>
        <tr>
            <th>Username</th>
            <th>Role</th>
        </tr>

        <%
            List<Admin> users = (List<Admin>) request.getAttribute("users");
            if (users != null && !users.isEmpty()) {
                for (Admin user : users) {
        %>
            <tr>
                <td><%= user.getUsername() %></td>
                <td><%= user.getId()%></td>
            </tr>
        <%
                }
            } else {
        %>
            <tr>
                <td colspan="2">No users found</td>
            </tr>
        <%
            }
        %>
    </table>

    <a href="/admin" class="back-btn">Back to Dashboard</a>

</body>
</html>


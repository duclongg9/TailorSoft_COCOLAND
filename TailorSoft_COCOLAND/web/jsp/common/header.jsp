
<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${pageTitle}" /></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet"/>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script defer src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <style>
        body{font-family:'Roboto',sans-serif;}
        #sidebar{position:fixed;top:0;left:0;min-height:100vh;width:220px;transition:left .3s;}
        #sidebar.collapsed{left:-220px;}
        #mainContent{margin-left:220px;transition:margin-left .3s;}
    </style>
</head>
<body>
<button class="btn btn-dark position-fixed m-2" id="sidebarToggle" style="z-index:1100;"><i class="fas fa-bars"></i></button>
<div class="d-flex">
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const sidebar = document.getElementById('sidebar');
        const main = document.getElementById('mainContent');
        const toggle = document.getElementById('sidebarToggle');
        if (sidebar && main && toggle) {
            toggle.addEventListener('click', function () {
                sidebar.classList.toggle('collapsed');
                if (sidebar.classList.contains('collapsed')) {
                    main.style.marginLeft = '0';
                } else {
                    main.style.marginLeft = '220px';
                }
            });
        }
    });
</script>

<%@ page import="com.luxsoft.sw4.rh.Incapacidad" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>Incapacidades</title>
	
</head>
<body>
	<content tag="header">
		<h2> Registro de incapacidades </h2>
	</content>
	
	<content tag="gridTitle">Incapacidades  (${session.ejercicio})</content>
	
	<content tag="gridPanel">
		<g:render template="gridPanel"/>
	</content>
	
	
	
</body>
</html>
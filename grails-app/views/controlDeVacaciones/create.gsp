<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="createForm"/>
	<title>Alta Otras deducciones</title>
	<g:set var="entity" value="${controlDeVacacionesInstance}" scope="request" />
	<g:set var="editable" value="${true}" scope="request" />
	<g:set var="imprimible" value="${false}" scope="request" />
</head>
<body>

<content tag="header">
	Alta de control de vacaciones ${session.ejercicio }
</content>

<content tag="formFields">
	<f:with bean="controlDeVacacionesInstance">
		<f:field property="empleado" />
		<f:display property="ejercicio"/>
	</f:with>
</content>
	
</body>
</html>



	
	
</body>
</html>


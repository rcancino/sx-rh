
<!doctype html>
<html>
	<head>
		<meta name="layout" content="createForm">
		<g:set var="entity" value="${puestoInstance}" scope="request"/>
		<g:set var="entityName" value="Puesto" scope="request"/>
		<title>Alta de puesto</title>
	</head>
	<body>
		<content tag="formFields">
			<f:with bean="${entity}">
				<f:field property="clave" widget-class="form-control"/>
				<f:field property="descripcion" widget-class="form-control"/>
			</f:with>
			
		</content>
		
	</body>
</html>

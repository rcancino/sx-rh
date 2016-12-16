
<!doctype html>
<html>
	<head>
		<meta name="layout" content="showForm">
		<g:set var="entity" value="${finiquitoInstance}" scope="request"/>
		<g:set var="entityName" value="Finiquito" scope="request"/>
		<g:set var="editable" value="true" scope="request"/>
		<g:set var="delete" value="true" scope="request"/>
		<title>Finiquito: ${entity.empleado}</title>
	</head>
	<body>

		<content tag="header">
			Finiquito: ${entity.empleado}
		</content>
		<content tag="form">
			<f:with bean="${entity}">
				<f:display property="empleado" widget-class="form-control"/>
				<f:display property="baja.fecha" widget-class="form-control" label="Baja"/>
				<f:display property="diasPorPagar" widget-class="form-control" />
				
				
			</f:with>
			
		</content>
		
	</body>
</html>

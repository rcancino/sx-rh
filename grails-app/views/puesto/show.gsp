
<!doctype html>
<html>
	<head>
		<meta name="layout" content="showForm">
		<g:set var="entity" value="${puestoInstance}" scope="request"/>
		<g:set var="entityName" value="Puesto" scope="request"/>
		<g:set var="editable" value="true" scope="request"/>
		<title>Puesto ${entity.clave}</title>
	</head>
	<body>
		<content tag="form">
			<f:with bean="${entity}">
				<f:display property="clave" widget-class="form-control"/>
				<f:display property="descripcion" widget-class="form-control"/>
			</f:with>
			
		</content>
		
	</body>
</html>

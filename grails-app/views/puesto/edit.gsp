
<!doctype html>
<html>
	<head>
		<meta name="layout" content="editForm">
		<g:set var="entity" value="${puestoInstance}" scope="request"/>
		<g:set var="entityName" value="Puesto" scope="request"/>
		<g:set var="editable" value="true" scope="request"/>
		<title>Puesto ${entity.clave}</title>
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

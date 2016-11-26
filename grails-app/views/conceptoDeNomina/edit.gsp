
<!doctype html>
<html>
	<head>
		<meta name="layout" content="editForm">
		<g:set var="entity" value="${conceptoDeNominaInstance}" scope="request"/>
		<g:set var="entityName" value="Concepto de nómina" scope="request"/>
		<g:set var="editable" value="true" scope="request"/>
		<title>Concepto de nómina ${entity.clave}</title>
	</head>
	<body>
		<content tag="formFields">
			<f:with bean="${entity}">
				<f:field property="clave" widget-class="form-control"/>
				<f:field property="descripcion" widget-class="form-control" />
				<f:field property="claveSat" widget-class="form-control" />
				<f:field property="tipo" widget-class="form-control" />
				<f:field property="general" widget-class="form-control" />
			</f:with>
			
		</content>
		
	</body>
</html>


<!doctype html>
<html>
	<head>
		<meta name="layout" content="showForm">
		<g:set var="entity" value="${diaFestivoInstance}" scope="request"/>
		<g:set var="entityName" value="Concepto de nómina" scope="request"/>
		<g:set var="editable" value="true" scope="request"/>
		<g:set var="delete" value="true" scope="request"/>
		<title>Día festivo ${entity.fecha.text()}</title>
	</head>
	<body>

		<content tag="header">
			Día festivo: ${entity.fecha.text()}
		</content>
		<content tag="form">
			<f:with bean="${entity}">
				<f:display property="id" label="Folio"/>
				<f:display property="ejercicio" widget-class="form-control"/>
				<f:display property="fecha"  />
				<f:display property="descripcion" widget-class="form-control" />
				<f:display property="parcial" widget-class="form-control" />
				<f:display property="salida" widget-class="form-control" />
			</f:with>
			
		</content>
		
	</body>
</html>

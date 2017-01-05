
<!doctype html>
<html>
	<head>
		<meta name="layout" content="showForm">
		<g:set var="entity" value="${conceptoDeNominaInstance}" scope="request"/>
		<g:set var="entityName" value="Concepto de nómina" scope="request"/>
		<g:set var="editable" value="true" scope="request"/>
		<title>Concepto de nómina ${entity.clave}</title>
	</head>
	<body>

		<content tag="header">
			Concepto de nómina: ${entity}
		</content>
		<content tag="form">
			<f:with bean="${entity}">
				<f:display property="clave" widget-class="form-control"/>
				<f:display property="descripcion" widget-class="form-control" />
				<f:display property="claveSat" widget-class="form-control" />
				<f:display property="tipo" widget-class="form-control" />
				<f:display property="general" widget-class="form-control" />
				<f:display property="clase" widget-class="form-control" />
				<f:display property="catalogoSat" widget-class="form-control" />
				<f:display property="catalogoSatClave" widget-class="form-control" />
			</f:with>
			
		</content>
		
	</body>
</html>

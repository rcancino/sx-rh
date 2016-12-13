
<!doctype html>
<html>
	<head>
		<meta name="layout" content="showForm">
		<g:set var="entity" value="${turnoInstance}" scope="request"/>
		<g:set var="entityName" value="Concepto de nómina" scope="request"/>
		<g:set var="editable" value="true" scope="request"/>
		<g:set var="delete" value="true" scope="request"/>
		<title>Turno ${turnoInstance.id}</title>
	</head>
	<body>

		<content tag="header">
			Turno: ${entity.id}
		</content>
		<content tag="form">
			<f:with bean="${entity}">
				<f:display property="id" label="Folio"/>
				<f:display property="descripcion" />

				<f:display property="inicioDeDia"  widget="time"/>
				<f:display property="horaLimiteDeTrabajo" widget="time" />
				<f:display property="inicioDeTiempoExtra" widget="time" />
				<f:display property="horaLimiteSiguienteDia" />
			</f:with>
			<legend>Días</legend>
			<table class="table table-striped table-bordered table-condensed">
					<thead>
						<tr >
							<th class="text-center">Día</th>
							<th class="text-center" colspan="2">Grupo 1</th>
							<th class="text-center" colspan="2">Grupo 2</th>
						</tr>
					</thead>
					
					<tbody>
						<g:each in="${turnoInstance.dias}" var="row">
						<tr>
							<td>${row.dia}</td>
							<td>${row.entrada1}</td>
							<td>${row.salida1}</td>
							<td>${row.entrada2}</td>
							<td>${row.salida2}</td>
							
						</tr>
						</g:each>
					</tbody>
				</table>
		</content>
		
	</body>
</html>



<html>
<head>
	<meta charset="UTF-8">
	<title>Aguinaldo ${aguinaldoInstance.id}</title>
</head>
<body>

<lx:header>
	<h3>Aguinaldo de ${aguinaldoInstance.empleado} ${aguinaldoInstance.ejercicio} 
						(${g.formatDate(date:aguinaldoInstance.fechaInicial)} - ${g.formatDate(date:aguinaldoInstance.fechaFinal)})
		<p>
		<small>
			Salario diario: <g:formatNumber number="${aguinaldoInstance.salario}" type="currency"/> 
			Tipo: ${aguinaldoInstance.empleado.salario.periodicidad}
		</small>
	</h3>
	<lx:warningLabel/>
	<lx:errorsHeader bean="${aguinaldoInstance}"/>
</lx:header>

<div class="wrapper  white-bg animated fadeInRight">

	<div class="row toolbar">
		<div class="col-md-12">
			<div class="button-panel">
				<div class="btn-group">
					<g:link action="index" class="btn btn-default btn-outline">
						<span class="glyphicon glyphicon-arrow-left"></span> Aguinaldos
				    </g:link>
					<g:link action="create" class="btn btn-default btn-outline">
						<span class="glyphicon glyphicon-plus"></span> Agregar
				    </g:link>
				    <g:link action="recalcular" class="btn btn-default btn-outline" id="${aguinaldoInstance.id}">
						<span class="glyphicon glyphicon-cog"></span> Recalcular
				    </g:link>
				    <g:link action="edit" class="btn btn-default btn-outline" id="${aguinaldoInstance.id}">
						<span class="glyphicon glyphicon-pencil"></span> Editar
				    </g:link>
				     %{-- <g:link action="delete" class="btn btn-default btn-outline" id="${aguinaldoInstance.id}"
				     	onclick ="return confirm('Eliminar el registro de aguinaldo ?');">
						<span class="glyphicon glyphicon-trash"></span> Eliminar
				    </g:link> --}%
				</div>
				<div class="btn-group">
					<button type="button" name="reportes"
							class="btn btn-default btn-outline dropdown-toggle" data-toggle="dropdown"
							role="menu">
							Reportes <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							<g:jasperReport jasper="TiempoExtraGeneral"
										format="PDF" name="TiempoExtra">
							</g:jasperReport>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<div class="row">
		<g:render template="aguinaldoResumen" bean="${aguinaldoInstance}"/>
	</div>

	<div class="row">
		<div class="col-md-12">
			<fieldset>
				<legend>Faltas / Incidencias / Permisos</legend>
			</fieldset>
			<table id="aguinaldoGrid" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th>Fecha</th>
						<th>Tipo</th>
						<th>Comentario</th>
					</tr>
				</thead>
				<tbody>
					<g:each in="${asistencias}" var="row">
						<tr>
							<td><g:formatDate date="${row.fecha}" format="dd/MM/yyyy"/></td>
							<td>${fieldValue(bean:row,field:"tipo")}</td>
							<td>${fieldValue(bean:row,field:"comentario")}</td>
						</tr>
					</g:each>
				</tbody>
			</table>
		</div>
	</div>

</div>
	
	
	
</body>
</html>
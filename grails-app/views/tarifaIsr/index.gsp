<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="catalogos"/>
	<title>Tarifa ISR</title>
</head>
<body>
	<content tag="header">
			Tabla de tarifas ISR
	</content>

	<content tag="operaciones">
		<li>
			<g:link action="trasladarTabla" >
				<span class="glyphicon glyphicon-plus"></span> Trasladar Tabla
			</g:link>
		</li>
	</content>
	<content tag="grid">
		
		<table id="grid" class=" table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>Ejercicio</th>
					<th>Tipo</th>
					<th>Límite inferior</th>
					<th>Límite superior</th>
					<th>Cuota fija</th>
					<th>Porcentaje</th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${tarifaIsrInstanceList}" var="row">
					<tr>
						<td>
							<g:link action="show" id="${row.id}">
								<g:formatNumber number="${row.ejercicio}" format="####"/>
							</g:link>
						</td>
						<td>
							<g:link action="show" id="${row.id}">
								${row.tipo}
							</g:link>
						</td>
						<td>
							<g:link action="show" id="${row.id}">
								<g:formatNumber number="${row.limiteInferior}" format="#,###.##"/>
							</g:link>
						</td>
						<td><g:formatNumber number="${row.limiteSuperior}" format="#,###.##"/></td>
						<td><g:formatNumber number="${row.cuotaFija}" format="#,###.##"/></td>
						<td><g:formatNumber number="${row.porcentaje}" format="##.##"/></td>
						
					</tr>
				</g:each>
			</tbody>
		</table>
		<div class="pagination">
			<g:paginate total="${tarifaIsrInstanceCount ?: 0}" />
		</div>
	</content>
</body>
</html>
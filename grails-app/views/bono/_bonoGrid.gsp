<table id="bonoGrid" class="grid gtable table-striped table-bordered table-condensed">
	<thead>
		<tr>
			<th>Id</th>
			<th>Empleado</th>
			<th>Ubicacion</th>
			<th>T</th>
			<th>Salario</th>
			<th>Faltas</th>
			<th>Incap</th>
			<th>Falt (%)</th>

			<th>Ret</th>
			<th>Ret (%)</th>

			<th>Prod</th>
			<th>Prod (%)</th>

			<th>Bono Ini</th>
			<th>Bono Par</th>
			<th>Bono (%)</th>
			<th>Total</th>
			

		</tr>
	</thead>
	<tbody>
		<g:each in="${bonoInstanceList}" var="row">
			<tr>
				<td>${row.id}</td>
				<td>
					<g:link action="show" id="${row.id}">
						${fieldValue(bean:row,field:"empleado.nombre")}
					</g:link>
				</td>
				<td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
				<td>${fieldValue(bean:row,field:"empleado.salario.periodicidad").substring(0,1)}</td>
				<td><g:formatNumber number="${row.empleado.salario.salarioDiario}" format="####.####"/></td>
				<td><g:formatNumber number="${row.faltas}" format="######"/></td>
				<td><g:formatNumber number="${row.incapacidades}" format="######"/></td>
				<td><g:formatNumber number="${row.faltasIncapacidadesFactor}" format="#,###.##"/></td>

				<td><g:formatNumber number="${row.retardos}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.retardoFactor}" format="#,###.##"/></td>

				<td>${fieldValue(bean: row, field: 'productividad')}</td>
				<td><g:formatNumber number="${row.productividadFactor}" format="##.##%"/></td>

				<td><g:formatNumber number="${row.bonoInicial}" format="#,###.##"/></td>
				<td><g:formatNumber number="${row.bonoPreliminar}" format="#,###.##"/></td>

				<td><g:formatNumber number="${row.porcentajeBono}" /></td>
				<td><g:formatNumber number="${row.bono}" type="currency"/></td>
				
			</tr>
		</g:each>
	</tbody>
</table>

